package cn.bixin.sona.server.im.config;

import cn.bixin.sona.common.hbase.HBaseRepository;
import com.alibaba.hbase.client.AliHBaseUEConnection;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ConfigurationProperties(prefix = "spring.hbase.client")
public class HbaseConfig {

    private String endpoint;

    private String userName;

    private String password;

    /**
     * 操作超时时间
     */
    private String timeout;

    /**
     * 消息存储时间（默认90天）
     */
    private long storeTime = 15552000000L;

    private Connection hBaseConnection() throws IOException {
        // 新建一个Configuration
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        // 集群的连接地址
        conf.set("hbase.client.endpoint", endpoint);
        // 设置用户名密码
        conf.set("hbase.client.username", userName);
        conf.set("hbase.client.password", password);
        conf.set("hbase.client.operation.timeout", timeout);
        /**
         *  todo 测试环境 使用 阿里hbase，需要使用 AliHBaseUEConnection 才能链接
         *  相关依赖 alihbase-connector
         */
        conf.set("hbase.client.connection.impl", AliHBaseUEConnection.class.getName());
        return ConnectionFactory.createConnection(conf);
    }

    @Bean
    public HBaseRepository hBaseRepository() throws IOException {
        return HBaseRepository.getInstance(hBaseConnection());
    }

    public long getHbaseLimitTime(){
        return System.currentTimeMillis() - storeTime;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public long getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(long storeTime) {
        this.storeTime = storeTime;
    }
}
