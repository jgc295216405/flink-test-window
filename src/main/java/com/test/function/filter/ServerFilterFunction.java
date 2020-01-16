package com.test.function.filter;

import com.test.config.DynamicConfig;
import com.test.model.KPIGroupMO;
import com.test.model.NssFlatMO;
import org.apache.flink.api.common.functions.FilterFunction;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 20:58
 * @city of hangzhou
 * @corp of 163.com
 */
public class ServerFilterFunction implements FilterFunction<NssFlatMO> {

    @Override
    public boolean filter(NssFlatMO flatNssMO) {
        KPIGroupMO kpiGroupMo = DynamicConfig.getKpiGroup(flatNssMO.getCluster(), flatNssMO.getModel(), flatNssMO.getMonitor());
        return kpiGroupMo.isPredictServerEnabled();
    }
}
