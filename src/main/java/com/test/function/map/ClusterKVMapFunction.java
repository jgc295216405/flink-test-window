package com.test.function.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Preconditions;
import com.test.model.KPIKeyMO;
import com.test.model.KVPointMO;
import com.test.model.NssFlatMO;
import org.apache.flink.api.common.functions.MapFunction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 20:54
 * @city of hangzhou
 * @corp of 163.com
 */
public class ClusterKVMapFunction implements MapFunction<NssFlatMO, KVPointMO> {
    private long second;

    public ClusterKVMapFunction(long batchSecond) {
        Preconditions.checkArgument(batchSecond % 60 == 0);
        this.second = batchSecond;
    }

    @Override
    public KVPointMO map(NssFlatMO flatNssMO) {
        long offset = flatNssMO.getTime() % second;//相同的时间窗口的数据
        long time = flatNssMO.getTime() - offset + 1;//避免临界值产生,临界值msg模块会有个临界问题(timeWindow前开后闭，会把最后一个点遗漏掉),所以这里加上1,避免临界值
        Map<String, Double> map = new HashMap<>();
        Map<String, Double> dataMap = flatNssMO.getInnerDataMap();
        for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
            Double value = new BigDecimal(entry.getValue()).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            map.put(entry.getKey() + ".sum", value);
        }
        KPIKeyMO metricInfo = new KPIKeyMO(flatNssMO.kpiGroup().getGroupId(), flatNssMO.getInnerKeyMap(), 2);
        return new KVPointMO(metricInfo.hash(), JSON.toJSONString(map, SerializerFeature.MapSortField), time, metricInfo);
    }


}
