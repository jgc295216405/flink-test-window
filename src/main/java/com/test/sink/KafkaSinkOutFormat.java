package com.test.sink;

import com.alibaba.fastjson.JSON;
import com.test.config.StaticConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author hzjiaoguangcai
 * @time 2019/8/14 19:30
 * @city of hangzhou
 * @corp of 163.com
 */
public class KafkaSinkOutFormat extends HBaseOutputFormat {
    private KafkaProducer<String, String> kafkaProducer;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSinkOutFormat.class);
    private boolean sinkHBase;

    public KafkaSinkOutFormat(boolean sinkHBase, long id, StaticConfig staticConfig) {
        super(id, staticConfig);
        this.sinkHBase = sinkHBase;
    }


    @Override
    public void open(int taskNumber, int numTasks) throws IOException {
//        if (this.sinkHBase) {
//            super.open(taskNumber, numTasks);
//        }
//
//        if (this.kafkaProducer == null) {
//            this.kafkaProducer = new KafkaProducer<>(staticConfig.producerProperties());
//        }

    }

    @Override
    public void writeRecord(NssDataMO3 kvPointMo) throws IOException {
//        if (this.sinkHBase) {
//            super.writeRecord(kvPointMo);
//        }
//        String key = kvPointMo.getKey();
//        String key2=Joiner.on(StrConstant.JIN).join(kvPointMo.getKey(), fixHours(kvPointMo.getTs()));
//        kvPointMo.setKey(key2);
//        String value = JSON.toJSONString(kvPointMo);
//        ProducerRecord<String, String> record = new ProducerRecord<>(staticConfig.producerTopic(),key, JSON.toJSONString(kvPointMo));
//        this.kafkaProducer.send(record);
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("output:{},kafka sink:{}", this.id, value);
//        }
//        kvPointMo.setKey(  StringUtils.split(kvPointMo.getKey(),"#")[0]);
        LOGGER.info("aaa:output:{}",JSON.toJSONString(kvPointMo));
    }

    @Override
    public void close() throws IOException {
        if (this.sinkHBase) {
            super.close();
        }
        if (this.kafkaProducer != null) {
            this.kafkaProducer.close();
        }
    }
}
