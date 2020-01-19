package com.test;

import com.alibaba.fastjson.JSON;
import com.test.sink.NssDataMO2;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.GenericTypeInfo;

/**
 * @author hzjiaoguangcai
 * @time 2019/8/2 19:57
 * @city of hangzhou
 * @corp of 163.com
 */
public class DataDeserializationSchemaExtend implements DeserializationSchema<NssDataMO2> {
    @Override
    public NssDataMO2 deserialize(byte[] message) {
        return JSON.parseObject(message, NssDataMO2.class);
    }

    @Override
    public boolean isEndOfStream(NssDataMO2 nextElement) {
        return false;
    }

    @Override
    public TypeInformation<NssDataMO2> getProducedType() {
        return new DataTypeInfo(NssDataMO2.class);
    }

    class DataTypeInfo extends GenericTypeInfo {
        public DataTypeInfo(Class typeClass) {
            super(typeClass);
        }
    }
}


