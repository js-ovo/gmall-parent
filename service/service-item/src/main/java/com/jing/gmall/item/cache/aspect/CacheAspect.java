package com.jing.gmall.item.cache.aspect;

import com.alibaba.fastjson.JSON;
import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.item.cache.servie.CacheService;
import com.jing.gmall.item.vo.SkuDetailVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 缓存切面
 */
@Aspect
@Component
public class CacheAspect {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CacheService cacheService;


    @Pointcut("execution(* *..*(..))")
    public void pointcut(){
    }

    @Around("@annotation(com.jing.gmall.item.cache.annotation.MallCache)")
    public Object aroundCache(ProceedingJoinPoint joinPoint){

        //TODO前置通知
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();

        Signature signature = joinPoint.getSignature();


        // 获取缓存的key
        String cacheKey = "sku:info:" + args[0];

        // 从缓存中查找 是否存在
        SkuDetailVo cacheData = getCacheData(cacheKey,joinPoint);
        if (cacheData != null){
            // 缓存命中,直接返回
            return cacheData;
        }
        // 缓存未命中,去bitmap中查找是否有
        boolean isExit = exitBitMap(RedisConst.SKUID_BITMAP_KEY, (Long) args[0]);
        if (!isExit){
            // bitmap中没有
            return null;
        }
        // 去远程查询 开始回源
        // 分布式锁, 防止缓存击穿
        RLock lock = redissonClient.getLock(RedisConst.LOCK_PREFIX + cacheKey);
        // 尝试获取锁,只会有一个拿到锁
        try {
            if (lock.tryLock()) {
                // 获取到锁,去数据库查询

                Object retVal = joinPoint.proceed();
                //TODO 返回通知
                // 存入缓存
                cacheService.saveCache(cacheKey, (SkuDetailVo) retVal);
                return retVal;
            } else {
                // 加锁失败
                // 等待一会直接去缓存查询
                TimeUnit.MILLISECONDS.sleep(100);
                // 查询缓存
                return getCacheData(cacheKey, joinPoint);
            }

        } catch (Throwable e){
            //TODO 异常通知
            e.printStackTrace();
            throw new RuntimeException();
        }finally {
            //TODO 后置通知
            //解锁
            lock.unlock();
        }
    }


    /**
     *  查询bitmap中是否存在该信息
     * @param bitmapKey
     * @param skuId
     * @return
     */
    private boolean exitBitMap(String bitmapKey, Long skuId) {
        return stringRedisTemplate.opsForValue().getBit(bitmapKey,skuId);
    }


    /**
     * 从缓存中获取数据
     * @param joinPoint
     * @return
     */
    private SkuDetailVo getCacheData(String cacheKey,ProceedingJoinPoint joinPoint) {
        String json = stringRedisTemplate.opsForValue().get(cacheKey);
        if (! StringUtils.isEmpty(json)){
            return JSON.parseObject(json, SkuDetailVo.class);
        }
        return null;
    }


}
