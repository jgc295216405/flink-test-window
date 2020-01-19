package com.test;

import com.alibaba.fastjson.JSON;
import com.test.config.StaticConfig;
import com.test.function.DataDeserializationSchema;
import com.test.function.DataTimestampExtractor;
import com.test.function.DataTimestampExtractor2;
import com.test.function.keyby.HashKeyByFunction;
import com.test.function.reduce.KeyReduceFunction;
import com.test.function.reduce.KeyReduceFunction2;
import com.test.model.NssDataMO;
import com.test.sink.KafkaSinkOutFormat;
import com.test.sink.NssDataMO2;
import com.test.sink.NssDataMO3;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.shaded.curator.org.apache.curator.shaded.com.google.common.collect.Iterables;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class MultiWindow {
    public static void main(String[] args) throws Exception {
        StaticConfig staticConfig = StaticConfig.init(args);
        long batch60 = 60, batch120 = 120;
        boolean sinkHBase = StaticConfig.getConfig().sinkHBase();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(60000);//60s
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);//退出后保存checkpoint
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);//
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);//时间类型
        RestartStrategies.RestartStrategyConfiguration restartStrategyConfiguration = RestartStrategies.fixedDelayRestart(120, org.apache.flink.api.common.time.Time.of(30, TimeUnit.SECONDS));
        env.getConfig().setRestartStrategy(restartStrategyConfiguration);
        //kafka数据流
        FlinkKafkaConsumerBase<NssDataMO2> consumer = new FlinkKafkaConsumer<>(staticConfig.consumerTopic()
//                , new DataDeserializationSchema()
                ,new KeyedDeserializationSchemaWrapperExtend<NssDataMO2>(new DataDeserializationSchema())
                , staticConfig.consumerProperties());//自定义业务时间
        //60s聚合

        OutputTag<NssDataMO3> late=new OutputTag<NssDataMO3>("late"){};
        SingleOutputStreamOperator<NssDataMO3> out=env.addSource(consumer).assignTimestampsAndWatermarks(new DataTimestampExtractor())
                .setParallelism(5)
                .map(v -> {
                    v.setKey(v.getKey() + "#"+RandomUtils.nextInt(0,2));
//                    v.setKey(v.getKey() );
                    return v;
                })
                .keyBy(new HashKeyByFunction(batch60))
                .timeWindow(Time.seconds(10))
                .allowedLateness(Time.seconds(60))
                .reduce(new KeyReduceFunction())
                .map(v -> {
                    NssDataMO3 nssDataMO3 = JSON.parseObject(JSON.toJSONString(v), NssDataMO3.class);
                    return nssDataMO3;
                })
                .keyBy(value ->
                     StringUtils.split(value.getKey(), "#")[0])
                .timeWindow(Time.seconds(10))
                .sideOutputLateData(late)
                .reduce(new KeyReduceFunction2());
        out.writeUsingOutputFormat(new KafkaSinkOutFormat(sinkHBase, batch60, staticConfig)).name("sink-cluster60");
        // print late data
        out.getSideOutput(late).print();
//
        env.execute("FLink-kafka-agg-HBase");
    }
}
