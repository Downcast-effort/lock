package cn.com.dandelion.service.impl;

import cn.com.dandelion.config.RedisProperties;
import cn.com.dandelion.service.IRedisService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author zhanghongwei
 * @version 1.0
 * @date 2021/11/3 13:35
 * @description
 */
@Slf4j
public class MasterSlaveRedisImpl implements IRedisService {
    /**
     * 主从部署配置
     * 连接方式:  主节点,子节点,子节点
     * 格式为:  127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381
     * @param redisProperties
     * @return Config
     */
    @Override
    public Config createRedissonConfig(RedisProperties redisProperties) {
        Config config = new Config();
        String address = redisProperties.getAddress();
        String password = redisProperties.getPassword();
        int database = redisProperties.getDatabase();
        String []  addresses = address.split(StrUtil.COMMA,-1);
        try{
            String masterNodeAddress = addresses[0];
            config.useMasterSlaveServers().setMasterAddress("redis://" + masterNodeAddress);
            if (StringUtils.isNotBlank(password)){
                config.useClusterServers().setPassword(password);
            }
            config.useMasterSlaveServers().setDatabase(database);

            config.useMasterSlaveServers().addSlaveAddress(Arrays.stream(addresses)
                                                                    .skip(1)
                                                                    .map(tmpAddress -> "redis://" + tmpAddress)
                                                                    .collect(Collectors.joining()));

            log.info("【主从连接】初始化Redis集群config,集群地址：" + address);
        }catch (Exception e){
            log.warn("【主从连接】集群初始化失败，信息如下：",e);
            e.printStackTrace();
        }
        return null;
    }
}
