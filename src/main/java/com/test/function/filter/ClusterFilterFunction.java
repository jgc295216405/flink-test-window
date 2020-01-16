package com.test.function.filter;

import com.test.config.DynamicConfig;
import com.test.model.KPIGroupMO;
import com.test.model.NssFlatMO;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 20:58
 * @city of hangzhou
 * @corp of 163.com
 */
public class ClusterFilterFunction extends ServerFilterFunction {
    private long batchTime;

    public ClusterFilterFunction(long batchTime) {
        this.batchTime = batchTime;
    }

    @Override
    public boolean filter(NssFlatMO flatNssMO) {

        KPIGroupMO kpiGroupMo = DynamicConfig.getKpiGroup(flatNssMO.getCluster(), flatNssMO.getModel(), flatNssMO.getMonitor());
        boolean enable = kpiGroupMo.isPredictClusterEnabled();
        boolean batch = batchTime == kpiGroupMo.getCollectInterval();
        return enable && batch;
    }
}
