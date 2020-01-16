package com.test.function.filter;

import com.test.model.NssFlatMO;
import org.apache.flink.api.common.functions.FilterFunction;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 19:55
 * @city of hangzhou
 * @corp of 163.com
 */
public class FocusFilterFunction implements FilterFunction<NssFlatMO> {
    @Override
    public boolean filter(NssFlatMO flatNssMO) {
        if (flatNssMO == null) {
            return false;
        }
        return flatNssMO.isFocus();
    }
}
