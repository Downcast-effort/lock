package cn.com.dandelion.service.impl;

import cn.com.dandelion.config.RedisProperties;
import cn.com.dandelion.service.IRedisService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

/**
 * @author zhanghongwei
 * @version 1.0
 * @date 2021/11/3 10:45
 * @description
 */
@Slf4j
public class ClusterRedisImpl implements IRedisService {
    /**
     * 集群Redis分布式锁实现
     * cluster方式至少6个节点(3主3从，3主做sharding，3从用来保证主宕机后可以高可用)
     * 格式为: 127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381,127.0.0.1:6382,127.0.0.1:6383,127.0.0.1:6384
     * @param redisProperties
     * @return Config
     */
    @Override
    public Config createRedissonConfig(RedisProperties redisProperties) {
        Config config = new Config();
        String address = redisProperties.getAddress();
        String password = redisProperties.getPassword();
        String []  addresses = address.split(StrUtil.COMMA,-1);
        try{
            for (int i = 0; i < addresses.length; i++) {
                config.useClusterServers().addNodeAddress("redis://" + addresses[i]);
                if (StringUtils.isNotBlank(password)){
                    config.useClusterServers().setPassword(password);
                }
            }
            log.info("【集群模式】初始化Redis集群config,集群地址：" + address);
        }catch (Exception e){
            log.warn("【集群模式】集群初始化失败，信息如下：",e);
            e.printStackTrace();
        }

        return config;
    }
}
