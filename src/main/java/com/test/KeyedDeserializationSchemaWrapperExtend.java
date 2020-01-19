package com.test;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchemaWrapper;

import java.io.IOException;

public class KeyedDeserializationSchemaWrapperExtend<T> extends KeyedDeserializationSchemaWrapper<T> {
    private  DeserializationSchema<T> myDeserializationSchema;
    public KeyedDeserializationSchemaWrapperExtend(DeserializationSchema<T> deserializationSchema) {
        super(deserializationSchema);
        this.myDeserializationSchema=deserializationSchema;
    }

    @Override
    public T deserialize(byte[] messageKey, byte[] message, String topic, int partition, long offset) throws IOException {
        return myDeserializationSchema.deserialize(message);
    }
}
