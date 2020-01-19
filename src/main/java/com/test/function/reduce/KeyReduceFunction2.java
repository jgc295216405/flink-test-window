package com.test.function.reduce;

import com.test.sink.NssDataMO3;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.ReduceFunction;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 20:03
 * @city of hangzhou
 * @corp of 163.com
 */
public class KeyReduceFunction2 implements ReduceFunction<NssDataMO3> {
    @Override
    public NssDataMO3 reduce(NssDataMO3 a, NssDataMO3 b) {
//        if(!StringUtils.equals(a.getKey(),b.getKey())){
            b.setValue(a.getValue()+b.getValue());
//        }
        return b;
    }
}
