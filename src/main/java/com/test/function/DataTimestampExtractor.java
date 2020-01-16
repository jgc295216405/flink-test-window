package com.test.function;

import com.test.sink.NssDataMO2;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.concurrent.TimeUnit;

/**
 * @author hzjiaoguangcai
 * @time 2019/8/2 19:39
 * @city of hangzhou
 * @corp of 163.com
 */
public class DataTimestampExtractor extends BoundedOutOfOrdernessTimestampExtractor<NssDataMO2> {
    private static final int MAX_EVENT_DELAY = 10;

    public DataTimestampExtractor() {
        super(Time.seconds(MAX_EVENT_DELAY));
    }

    @Override
    public long extractTimestamp(NssDataMO2 element) {
        return TimeUnit.MILLISECONDS.toMillis(element.getTime());
    }
}
