package com.test.function.reduce;

import com.test.sink.NssDataMO2;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 20:03
 * @city of hangzhou
 * @corp of 163.com
 */
public class KeyReduceFunction implements ReduceFunction<NssDataMO2> {
    @Override
    public NssDataMO2 reduce(NssDataMO2 a, NssDataMO2 b) {
         b.setValue(a.getValue()+b.getValue());
        return b;
    }
}
