package com.test.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Joiner;
import com.test.model.KPIGroupMO;
import com.test.model.Page;
import com.test.model.StrConstant;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/31 13:29
 * @city of hangzhou
 * @corp of 163.com
 */
public class DynamicConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicConfig.class);
    private static volatile boolean init = false;
    private static final int DELAY = 120;

    public static final String NAMESPACE = "aiops.flink";
    public static Map<String, KPIGroupMO> groups = new HashMap<>();
    public static Map<String, Map<String, Pattern>> modelFilter = new HashMap<>();

    public static void init(StaticConfig staticConfig)  {
        if (!init) {
//            final ConfigClient configClient = DefaultConfigClient.getInstance();
//            configClient.start();
            // 配置中心——key
            String key = staticConfig.configKey();
//            String value = configClient.getConfigValue(NAMESPACE, key);
//            new ModelFilterNotify().callback(value);
//            configClient.setNotifyListener(NAMESPACE, key, Type.json, new ModelFilterNotify());
            Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new SyncConfig(staticConfig.getHost(), staticConfig.getPort()), 0, DELAY, TimeUnit.SECONDS);
            init = true;
        }
    }

    public static KPIGroupMO getKpiGroup(String cluster, String model, String monitor) {

        KPIGroupMO kpiGroup = groups.get(buildKey(cluster, model, monitor));
        if (kpiGroup != null) {
            return kpiGroup;
        }
        //做兼容,再判断一次
        return groups.get(buildKey(cluster, model, null));
    }

    public static String buildKey(String cluster, String model, String monitor) {
        if (null == monitor) {
            monitor = StrConstant.XIA_HUA_XIAN;
        }
        return Joiner.on(StrConstant.JIN).join(cluster, model, monitor);
    }

    private static class SyncConfig implements Runnable {

        private String host;
        private int port;

        public SyncConfig(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {
            try {
//                Page<KPIGroupMO> kpiGroupsPage;
//                int page = 0, size = 2000;
//                Map<String, KPIGroupMO> map = new HashMap<>();
//                do {
//                    URIBuilder builder = new URIBuilder().setScheme("http").setHost(host).setPort(port).setPath("/api/db/focus").setParameter("page", String.valueOf(page)).setParameter("size", String.valueOf(size));
//                    String response = HttpClientUtil.doGet(builder.build().toString());
//                    kpiGroupsPage = JSON.parseObject(response, new TypeReference<Page<KPIGroupMO>>() {
//                    });
//                    for (KPIGroupMO kpiGroupMO : kpiGroupsPage.getContent()) {
//                        if (kpiGroupMO == null) continue;
//                        String key = DynamicConfig.buildKey(kpiGroupMO.getCluster(), kpiGroupMO.getModelName(), kpiGroupMO.getMonitor());
//                        map.put(key, kpiGroupMO);
//                    }
//                    page = kpiGroupsPage.getNumber() + 1;
//                } while (!kpiGroupsPage.isLast());
//                DynamicConfig.groups = map;
                LOGGER.info("Load config,size:{}", groups.size());
            } catch (Exception e) {
                LOGGER.error("Sync config error.", e);
            }
        }
    }

//
//    private static class ModelFilterNotify implements NotifyListener<String> {
//
//        @Override
//        public void callback(String value) {
//            try {
//                Map<String, Map<String, String>> configMap = JSON.parseObject(value, new TypeReference<Map<String, Map<String, String>>>() {
//                });
//                Map<String, Map<String, Pattern>> newPatternMap = new HashMap<>();
//                configMap.forEach((k, v) ->
//                {
//                    Map<String, Pattern> valuePattern = v.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> Pattern.compile(entry.getValue()), (a, b) -> a));
//                    newPatternMap.put(k, valuePattern);
//                });
//                DynamicConfig.modelFilter = newPatternMap;
//                LOGGER.info("Load ModelFilter {}", JSON.toJSONString(configMap));
//            } catch (Exception e) {
//                LOGGER.error("Load ModelFilter.", e);
//            }
//        }
//    }
}
