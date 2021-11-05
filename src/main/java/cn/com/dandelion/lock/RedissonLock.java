package cn.com.dandelion.lock;

import cn.com.dandelion.RedissonManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author zhanghongwei
 * @version 1.0
 * @date 2021/11/5 14:48
 * @description
 */
@Slf4j
public class RedissonLock {
    private RedissonManager redissonManager;
    private Redisson redisson;
    public RedissonLock (RedissonManager redissonManager){
        this.redissonManager = redissonManager;
        this.redisson = redissonManager.getRedisson();
    }

    public RedissonLock(){

    }

    public void lock(String name,long leaseTime){
        RLock rLock = redisson.getLock(name);
        rLock.lock(leaseTime, TimeUnit.SECONDS);
    }


    public void lock(String name){
        RLock rLock = redisson.getLock(name);
        rLock.lock();
    }

    public boolean tryLock(String name,long leaseTime){
        RLock rLock = redisson.getLock(name);
        boolean lockFlag = false;
        try {
            lockFlag = rLock.tryLock(leaseTime,TimeUnit.SECONDS);
        }catch (InterruptedException e){
            log.warn("【获取Redis锁】发生异常，名称：%s，异常信息",name,e);
            e.printStackTrace();
            return false;
        }
        return lockFlag;
    }

    public  boolean tryLock(String name, long leaseTime,long waitTime) {

        RLock rLock = redisson.getLock(name);
        boolean lockFlag = false;
        try {
            lockFlag = rLock.tryLock( waitTime,leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.warn("【获取Redis锁】发生异常，名称：%s，异常信息",name,e);
            e.printStackTrace();
            return false;
        }
        return lockFlag;
    }

    public void unlock(String lockName) {
        redisson.getLock(lockName).unlock();
    }


    public boolean isLock(String lockName) {
        RLock rLock = redisson.getLock(lockName);
        return rLock.isLocked();
    }
    public boolean isHeldByCurrentThread(String lockName) {
        RLock rLock = redisson.getLock(lockName);
        return rLock.isHeldByCurrentThread();
    }
    public RedissonManager getRedissonManager() {
        return redissonManager;
    }

    public void setRedissonManager(RedissonManager redissonManager) {
        this.redissonManager = redissonManager;
    }

}
