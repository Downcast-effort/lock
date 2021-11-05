package cn.com.dandelion.annoation;

import cn.com.dandelion.lock.RedissonLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhanghongwei
 * @version 1.0
 * @date 2021/11/5 16:42
 * @description
 */
@Component
@Aspect
@Slf4j
public class RedisLockHandler {

    @Autowired
    private RedissonLock redissonLock;

    @Around("@annotation(RedisLock)")
    public void handler(ProceedingJoinPoint joinPoint,RedisLock redisLock){
        log.info("开始进行Redis环绕通知，获取redis分布式锁开始");
        String name = redisLock.value();
        int leaseTime = redisLock.leaseTime();
        redissonLock.lock(name,leaseTime);
        try{

            log.info("获取分布式锁成功，正在执行相关业务逻辑");
            joinPoint.proceed();
        }catch (Throwable throwable){
            log.info("获取分布式锁异常：",throwable);
            throwable.printStackTrace();
        }finally {
            if (redissonLock.isHeldByCurrentThread(name)){
                redissonLock.unlock(name);
            }
        }
        log.info("释放分布式锁成功，结束业务逻辑");

    }
}
