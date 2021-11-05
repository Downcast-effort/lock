package cn.com.dandelion.service.impl;

import cn.com.dandelion.config.RedisProperties;
import cn.com.dandelion.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

/**
 * @author zhanghongwei
 * @version 1.0
 * @date 2021/11/3 14:02
 * @description
 */
@Slf4j
public class StoneRedisImpl implements IRedisService {
    /**
     * 单机版
     *
     * @param redisProperties
     * @return Config
     */
    @Override
    public Config createRedissonConfig(RedisProperties redisProperties) {
        Config config = new Config();
        try {
            String address = redisProperties.getAddress();
            String password = redisProperties.getPassword();
            int database = redisProperties.getDatabase();
            String redisAddr = "redis://" + address;
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(database);
            //密码可以为空
            if (StringUtils.isNotBlank(password)) {
                config.useSingleServer().setPassword(password);
            }
            log.info("【单机部署】Redis初始化成功，地址:" + address);
        } catch (Exception e) {
            log.error("【单机部署】Redis初始化失败", e);
        }
        return config;
    }
}
