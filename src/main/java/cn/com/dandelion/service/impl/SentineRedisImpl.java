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
 * @date 2021/11/3 13:59
 * @description
 */
@Slf4j
public class SentineRedisImpl implements IRedisService {
    /**
     * 哨兵模式
     *
     * @param redisProperties
     * @return Config
     */
    @Override
    public Config createRedissonConfig(RedisProperties redisProperties) {
        Config config = new Config();
        String address = redisProperties.getAddress();
        String password = redisProperties.getPassword();
        int database = redisProperties.getDatabase();
        String[] addrTokens = address.split(StrUtil.COMMA,-1);
        try {

            String sentinelAliasName = addrTokens[0];
            //设置redis配置文件sentinel.conf配置的sentinel别名
            config.useSentinelServers().setMasterName(sentinelAliasName);
            config.useSentinelServers().setDatabase(database);
            if (StringUtils.isNotBlank(password)) {
                config.useSentinelServers().setPassword(password);
            }
            //设置sentinel节点的服务IP和端口
            for (int i = 1; i < addrTokens.length; i++) {
                config.useSentinelServers().addSentinelAddress("redis://" + addrTokens[i]);
            }
            log.info("【哨兵部署】初始化化rediss Config:" + address);
        } catch (Exception e) {
            log.error("【哨兵部署】Redis初始化失败", e);
            e.printStackTrace();
        }
        return config;
    }

}
