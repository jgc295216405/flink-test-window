package com.test.function.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.test.config.DynamicConfig;
import com.test.config.StaticConfig;
import com.test.model.NssDataMO;
import com.test.model.NssFlatMO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 11:28
 * @city of hangzhou
 * @corp of 163.com
 */
public class FlatMapRichFunction extends RichFlatMapFunction<NssDataMO, NssFlatMO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlatMapRichFunction.class);

    private StaticConfig staticConfig;

    public FlatMapRichFunction(StaticConfig staticConfig) {
        this.staticConfig = staticConfig;
    }

    @Override
    public void open(Configuration parameters)  {
        DynamicConfig.init(staticConfig);
    }

    @Override
    public void flatMap(NssDataMO nssDataMO, Collector<NssFlatMO> collector) {
        List<Map<String, Object>> dataList;
        try {
            dataList = JSON.parseObject(nssDataMO.getData(), new TypeReference<List<Map<String, Object>>>() {
            });
            if (CollectionUtils.isEmpty(dataList)) {
                return;
            }
            for (Map<String, Object> map : dataList) {
                collector.collect(new NssFlatMO(nssDataMO, map));
            }
        } catch (Exception e) {
            LOGGER.error("nss data parse json error,{}.", this, e);
        }
    }
}
