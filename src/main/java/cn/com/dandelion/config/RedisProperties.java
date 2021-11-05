package cn.com.dandelion.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhanghongwei
 * @version 1.0
 * @date 2021/11/3 10:41
 * @description
 */
@PropertySource({"redisson.properties"})
@ConfigurationProperties(prefix = "redis.lock.server")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisProperties {
    /**
     * redis主机地址，ip：port，有多个用半角逗号分隔
     */
    private String address;

    /**
     * 连接类型，支持standalone-单机节点，sentinel-哨兵，cluster-集群，masterslave-主从
     */
    private String type;

    /**
     * redis 连接密码
     */
    private String password;

    /**
     * 选取那个数据库
     */
    private int database;

    public RedisProperties setPassword(String password) {
        this.password = password;
        return this;
    }

    public RedisProperties setDatabase(int database) {
        this.database = database;
        return this;
    }
}
