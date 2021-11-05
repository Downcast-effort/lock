package cn.com.dandelion.annoation;

import java.lang.annotation.*;

/**
 * @author zhanghongwei
 * @version 1.0
 * @date 2021/11/5 14:43
 * @description
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisLock {

    /**
     * 名称
     * @return
     */
    String value() default "redission";

    /**
     * 有效时间
     * @return
     */
    int leaseTime() default 10;
}
