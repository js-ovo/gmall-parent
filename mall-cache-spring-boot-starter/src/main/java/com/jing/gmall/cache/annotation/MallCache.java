package com.jing.gmall.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MallCache {

    /**
     * 存入redis中的key  SpEl 支持动态的获取参数,可以和方法中的参数绑定
     * #{方法参数名}
     * @return
     */
    String cacheKey() default "";


    /**
     * 使用bitmap时所使用的key  如果没有填写代表不用查询
     * @return
     */
    String bitMapKey() default "";


    /**
     * 使用bitmap时,指定对应的索引  跟方法参数绑定使用 SpEl表达式计算方法参数仅支持 Long和int
     * @return
     */
    String bitIndex() default "";


    /**
     * 缓存的过期时间,默认永不过期
     * @return
     */
    long timeout() default -1L;


    /**
     * 过期时间单位,默认是分钟
     * @return
     */
    TimeUnit unit() default TimeUnit.MINUTES;


}
