package com.test.sink;
import com.test.config.StaticConfig;
import org.apache.flink.api.common.io.OutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.BufferedMutatorParams;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @author hzjiaoguangcai
 * @time 2019/7/8 16:02
 * @city of hangzhou
 * @corp of 163.com
 */
public class HBaseOutputFormat implements OutputFormat<NssDataMO3> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseOutputFormat.class);
    private static final String TABLE_FAMILY = "d";
    private Connection conn;
    protected long id;
    protected StaticConfig staticConfig;
    private BufferedMutatorParams params;
    private BufferedMutator mutator;
    private int count=0;
    private int outputBatch = 1;

    public HBaseOutputFormat(long id, StaticConfig staticConfig) {
        this.id = id;
        this.staticConfig = staticConfig;
    }


    @Override
    public void configure(org.apache.flink.configuration.Configuration configuration) {

    }

    @Override
    public void open(int taskNumber, int numTasks) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        if (this.conn == null) {
            this.conn = ConnectionFactory.createConnection(configuration);
        }
        this.params = new BufferedMutatorParams(TableName.valueOf(staticConfig.hBaseTableName()));
        this.mutator = this.conn.getBufferedMutator(params);
        this.params.writeBufferSize(1024 * 1024 * 2); //设置缓存1m，当达到2m时数据会自动刷到hbase
        this.count = 0;
        this.outputBatch = staticConfig.countOutPut();
    }

    @Override
    public void writeRecord(NssDataMO3 kvPointMo) throws IOException {
//        Long hours = this.fixHours(kvPointMo.getTs());
//        String column = this.formatColumn(kvPointMo.getTs());
//        String rowKey = Joiner.on(StrConstant.JIN).join(kvPointMo.getKey(), hours);
//        Put put = new Put(Bytes.toBytes(rowKey));
//        put.addColumn(Bytes.toBytes(TABLE_FAMILY), Bytes.toBytes(column), Bytes.toBytes(kvPointMo.getValue()));
//        this.mutator.mutate(put);
//        if (count >= outputBatch) {//每满1000条刷新一下数据
//            this.mutator.flush();
//            count = 0;
//        }
//        count++;
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("output:{},HBase sink:{}", this.id, JSON.toJSONString(kvPointMo));
//        }
    }

    @Override
    public void close() throws IOException {

        if (this.conn != null) {
            this.conn.close();
        }
        if (this.mutator != null) {
            this.mutator.close();
        }
    }

    /**
     * 时间取整
     */
    protected long fixHours(long time) {
        return time - (time % 3600);
    }

    /**
     * 精确到分钟
     *
     * @param time
     * @return
     */
    private String formatColumn(long time) {
        long column = fixMinutes(time) * 100;
        return String.format("%04d", column);
    }

    /**
     * 时间取整
     */
    private long fixMinutes(long time) {
        return (time % 3600) / 60;
    }
}
