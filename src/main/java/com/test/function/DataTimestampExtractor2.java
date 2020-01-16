package com.test.function;

import com.test.sink.NssDataMO3;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.concurrent.TimeUnit;

/**
 * @author hzjiaoguangcai
 * @time 2019/8/2 19:39
 * @city of hangzhou
 * @corp of 163.com
 */
public class DataTimestampExtractor2 extends BoundedOutOfOrdernessTimestampExtractor<NssDataMO3> {
    private static final int MAX_EVENT_DELAY = 20;

    public DataTimestampExtractor2() {
        super(Time.seconds(MAX_EVENT_DELAY));
    }

    @Override
    public long extractTimestamp(NssDataMO3 element) {
        return TimeUnit.MILLISECONDS.toMillis(element.getTime())-2*1000;
    }
}
