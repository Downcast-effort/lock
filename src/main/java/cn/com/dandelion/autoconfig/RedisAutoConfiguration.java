package cn.com.dandelion.autoconfig;

import cn.com.dandelion.RedissonManager;
import cn.com.dandelion.config.RedisProperties;
import cn.com.dandelion.lock.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author zhanghongwei
 * @version 1.0
 * @date 2021/11/5 14:46
 * @description
 */

@Configuration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties(RedisProperties.class)
@Slf4j
public class RedisAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    @Order(value = 2)
    public RedissonLock redissonLock(RedissonManager redissonManager){
        RedissonLock redissonLock = new RedissonLock(redissonManager);
        log.info("锁组装完毕");
        return redissonLock;
    }

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 1)
    public RedissonManager redissonManager(RedisProperties redisProperties){
        RedissonManager redissonManager = new RedissonManager(redisProperties);
        log.info("锁已经组装完毕，当前连接方式：" + redisProperties.getType(),"连接地址：" + redisProperties.getAddress());
        return redissonManager;
    }



}
