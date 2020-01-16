package com.test.function.keyby;

import com.google.common.base.Preconditions;
import com.test.sink.NssDataMO2;
import org.apache.flink.api.java.functions.KeySelector;


/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 19:58
 * @city of hangzhou
 * @corp of 163.com
 */
public class HashKeyByFunction implements KeySelector<NssDataMO2, String> {
    private long seconds;

    public HashKeyByFunction(long batchSecond) {
        Preconditions.checkArgument(batchSecond % 60 == 0);
        this.seconds = batchSecond;
    }

    @Override
    public String getKey(NssDataMO2 flatNssMO) {
        if(flatNssMO.getKey()==null){
            return "null";
        }
        return flatNssMO.getKey();
    }
}
