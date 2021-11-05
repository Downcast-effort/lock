package cn.com.dandelion;

import cn.com.dandelion.config.RedisProperties;
import cn.com.dandelion.service.IRedisService;
import cn.com.dandelion.service.impl.ClusterRedisImpl;
import cn.com.dandelion.service.impl.MasterSlaveRedisImpl;
import cn.com.dandelion.service.impl.SentineRedisImpl;
import cn.com.dandelion.service.impl.StoneRedisImpl;
import cn.com.dandelion.type.RedisConnectionType;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;

/**
 * @author zhanghongwei
 * @version 1.0
 * @date 2021/11/3 14:08
 * @description
 */
@Slf4j
public class RedissonManager {


    private Config config = new Config();

    private Redisson redisson = null;

    public RedissonManager() {
    }

    public RedissonManager(RedisProperties redisProperties) {
        try {
            //通过不同部署方式获得不同cofig实体
            config = RedissonConfigFactory.getInstance().createConfig(redisProperties);
            redisson = (Redisson) Redisson.create(config);
        } catch (Exception e) {
            log.error("Redisson init error", e);
            throw new IllegalArgumentException("please input correct configurations," +
                    "connectionType must in standalone/sentinel/cluster/masterslave");
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }

    /**
     * Redisson连接方式配置工厂
     * 双重检查锁
     */
    static class RedissonConfigFactory {

        private RedissonConfigFactory() {
        }

        private static volatile RedissonConfigFactory factory = null;

        public static RedissonConfigFactory getInstance() {
            if (factory == null) {
                synchronized (Object.class) {
                    if (factory == null) {
                        factory = new RedissonConfigFactory();
                    }
                }
            }
            return factory;
        }


        /**
         * 根据连接类型获取对应连接方式的配置,基于策略模式
         *
         * @param redisProperties redis连接信息
         * @return Config
         */
        Config createConfig(RedisProperties redisProperties) {
            Preconditions.checkNotNull(redisProperties);
            Preconditions.checkNotNull(redisProperties.getAddress(), "redisson.lock.server.address cannot be NULL!");
            Preconditions.checkNotNull(redisProperties.getType(), "redisson.lock.server.password cannot be NULL");
            Preconditions.checkNotNull(redisProperties.getDatabase(), "redisson.lock.server.database cannot be NULL");
            String connectionType = redisProperties.getType();
            //声明配置上下文
            IRedisService redissonConfigService = null;
            if (connectionType.equals(RedisConnectionType.STANDALONE.getConnection_type())) {
                redissonConfigService = new StoneRedisImpl();
            } else if (connectionType.equals(RedisConnectionType.SENTINEL.getConnection_type())) {
                redissonConfigService = new SentineRedisImpl();
            } else if (connectionType.equals(RedisConnectionType.CLUSTER.getConnection_type())) {
                redissonConfigService = new ClusterRedisImpl();
            } else if (connectionType.equals(RedisConnectionType.MASTERSLAVE.getConnection_type())) {
                redissonConfigService = new MasterSlaveRedisImpl();
            } else {
                throw new IllegalArgumentException("创建Redisson连接Config失败！当前连接方式:" + connectionType);
            }
            return redissonConfigService.createRedissonConfig(redisProperties);
        }
    }

}
