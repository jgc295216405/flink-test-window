package com.test;

import com.alibaba.fastjson.JSON;
import com.test.config.StaticConfig;
import com.test.function.DataDeserializationSchema;
import com.test.function.DataTimestampExtractor;
import com.test.function.DataTimestampExtractor2;
import com.test.function.keyby.HashKeyByFunction;
import com.test.function.reduce.KeyReduceFunction;
import com.test.function.reduce.KeyReduceFunction2;
import com.test.sink.KafkaSinkOutFormat;
import com.test.sink.NssDataMO2;
import com.test.sink.NssDataMO3;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;

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
                , new DataDeserializationSchema()
                , staticConfig.consumerProperties())
                .assignTimestampsAndWatermarks(new DataTimestampExtractor());//自定义业务时间
        //60s聚合
        env.addSource(consumer)
                .keyBy(new HashKeyByFunction(batch60))
                .timeWindow(Time.seconds(10))
                .allowedLateness(Time.seconds(60))
                .reduce(new KeyReduceFunction())
                .map(v -> {
                    NssDataMO3 nssDataMO3 = JSON.parseObject(JSON.toJSONString(v), NssDataMO3.class);
                    return nssDataMO3;
                })
                .keyBy(value -> {
                    return StringUtils.split(value.getKey(), "#")[0];
                })
                .timeWindow(Time.seconds(10))
                .allowedLateness(Time.seconds(60))
                .reduce((v1,v2)->v2)
                .writeUsingOutputFormat(new KafkaSinkOutFormat(sinkHBase, batch60, staticConfig)).name("sink-cluster60");

//
        env.execute("FLink-kafka-agg-HBase");
    }
}
