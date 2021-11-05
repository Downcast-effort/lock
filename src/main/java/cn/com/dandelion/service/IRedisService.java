package cn.com.dandelion.service;

import cn.com.dandelion.config.RedisProperties;
import org.redisson.config.Config;

/**
 * @author zhanghongwei
 * @version 1.0
 * @date 2021/11/3 10:44
 * @description
 */
public interface IRedisService {

    /**
     * 根据不同的Redis配置策略创建对应的Config
     * @param redisProperties
     * @return Config
     */
    Config createRedissonConfig(RedisProperties redisProperties);
}
