package com.test.function.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
public class ServerKVMapFunction implements MapFunction<NssFlatMO, KVPointMO> {
    @Override
    public KVPointMO map(NssFlatMO flatNssMO) {
        Map<String, Double> indicators = new HashMap<>();
        Map<String, Double> dataMap = flatNssMO.getInnerDataMap();
        for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
            Double value = entry.getValue();
            String key = entry.getKey();
            if (value != null) {
                indicators.put(key, new BigDecimal(value).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        KPIKeyMO metricInfo = new KPIKeyMO(flatNssMO.kpiGroup().getGroupId(), flatNssMO
                .getServer(), flatNssMO.getHostId(), flatNssMO.getInstance(), flatNssMO.getInnerKeyMap(), 1);
        return new KVPointMO(metricInfo.hash(), JSON.toJSONString(indicators, SerializerFeature.SortField), flatNssMO.getTime(), metricInfo);
    }
}
