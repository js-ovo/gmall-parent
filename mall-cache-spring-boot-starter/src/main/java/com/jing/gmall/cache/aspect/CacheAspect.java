package com.jing.gmall.cache.aspect;


import com.fasterxml.jackson.core.type.TypeReference;
import com.jing.gmall.cache.annotation.MallCache;
import com.jing.gmall.cache.servie.CacheService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * 缓存切面
 */
@Aspect
@Component
public class CacheAspect {


    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CacheService cacheService;

    // 获取SpEl表达式对象
    private ExpressionParser parser = new SpelExpressionParser();




    @Pointcut("execution(* *..*(..))")
    public void pointcut(){
    }

    /**
     * 缓存业务切面
     * @param joinPoint
     * @return
     */
    @Around("@annotation(com.jing.gmall.cache.annotation.MallCache)") // 作用于使用@MallCache的方法
    public Object aroundCache(ProceedingJoinPoint joinPoint){

        // 锁的状态 拿到锁为true
        boolean lockStatus = false;
        // 锁
        RLock lock = null;

        try {
            //TODO 从注解中动态获取缓存key
            // 获取缓存的key  动态获取,从注解中获取
            String cacheKey = getCacheKey(joinPoint);

            //TODO  从缓存中查找 是否存在 返回值类型,动态的
            Object cacheDataObj = getCacheDataObj(cacheKey, joinPoint);
            if (cacheDataObj != null) {
                // 缓存命中,直接返回
                return cacheDataObj;
            }
            //TODO 缓存未命中,判断是否使用bitmap 如果bitmap的key是空就不需要
            // 防止 随机值穿透攻击
            // 获取注解上面的bitmapKey
            MallCache annotation = getAnnotation(MallCache.class, joinPoint);
            String bitMapKeyExpr = annotation.bitMapKey();
            if (!StringUtils.isEmpty(bitMapKeyExpr)) {
                // 去bitmap中查找
                // 根据计算得出bitmap的key
                String bitMapKey = evaluationValue(joinPoint, bitMapKeyExpr);
                // 得出bit map中的索引
                String indexExpr = annotation.bitIndex();
                String index = evaluationValue(joinPoint, indexExpr);
                // 判断bitmap中是否有
                boolean isExit = cacheService.exitBitMap(bitMapKey, Long.parseLong(index));
                if (!isExit) {
                    // bitmap中没有
                    return null;
                }
            }
            // 查找缓存,数据回源
            // 分布式锁, 防止缓存击穿  lock 的key 就是当前的缓存key 细粒度
            lock = redissonClient.getLock("lock:" + cacheKey);
            lockStatus = lock.tryLock();
            // 尝试获取锁,只会有一个拿到锁
            if (lockStatus) {
                // 获取到锁,去数据库查询  执行目标方法
                Object retVal = joinPoint.proceed();
                //TODO 返回通知
                // 存入缓存  可以防止 穿透攻击
                // 动态指定缓存过期时间
                long timeout = annotation.timeout();
                if (timeout < 0){
                    // 永不过期
                    cacheService.saveCache(cacheKey, retVal);
                } else {
                    TimeUnit unit = annotation.unit();
                    cacheService.saveCache(cacheKey,retVal,timeout,unit);
                }

                return retVal;
            } else {
                // 加锁失败
                // 等待一会直接去缓存查询
                TimeUnit.MILLISECONDS.sleep(100);
                // 查询缓存  缓存中是否有都直接返回
                return getCacheDataObj(cacheKey, joinPoint);
            }
        } catch (Throwable e) {
            //TODO 异常通知
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            //TODO 后置通知
            //解锁
            if (lock != null && lockStatus){
                lock.unlock();
            }
        }
    }


    /**
     * 从缓存中获取数据,根据方法的返回值类型不同 返回类型不同
     * @param cacheKey
     * @param joinPoint
     * @return
     */
    private Object getCacheDataObj(String cacheKey, ProceedingJoinPoint joinPoint) {
        return cacheService.getCacheDataObj(cacheKey,new TypeReference<Object>(){
            @Override
            public Type getType() {
                return getMethodReturnType(joinPoint);
            }
        });
    }


    /**
     * 获取方法的完成返回类型,包含复杂泛型
     * @param joinPoint
     * @return
     */
    private Type getMethodReturnType(ProceedingJoinPoint joinPoint) {
        // 获取目标方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 获取方法的返回值类型,含复杂泛型
        return method.getGenericReturnType();
    }


    /**
     * 根据注解中的表达式,计算出缓存key
     * @param joinPoint
     * @return
     */
    private String getCacheKey(ProceedingJoinPoint joinPoint) {
        // 获取缓存注解 用来解析注解中的参数
        MallCache mallCache = getAnnotation(MallCache.class, joinPoint);
        String cacheKeyExpression = mallCache.cacheKey();
        return evaluationValue(joinPoint, cacheKeyExpression);
    }


    /**
     * 通过表达式动态获取对应方法参数的值
     * @param joinPoint
     * @param expr 表达式
     * @return
     */
    private String evaluationValue(ProceedingJoinPoint joinPoint, String expr) {
        // 解析表达式
        Expression expression = parser.parseExpression(expr,new TemplateParserContext());
        // 获取方法的参数列表
        Object[] params = joinPoint.getArgs();
        // 获取方法的参数名称
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        // 创建一个容器存放 方法的参数名称 和 值  一一对应
        // 上下文容器
        EvaluationContext context = new StandardEvaluationContext();
        // 将方法参数名称和值意义放入容器
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i],params[i]);
        }
        // 获取表达式中所含有的方法参数的值
        return expression.getValue(context,String.class);
    }


    /**
     * 获取方法上的注解
     * @param tClass 注解类型
     * @param joinPoint
     * @param <T> 返回类型
     * @return
     */
    private <T extends Annotation> T getAnnotation(Class<T> tClass, ProceedingJoinPoint joinPoint) {
        // 获取切入点的方法的完整签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取到当前方法
        Method method = methodSignature.getMethod();
        // 获取方法上的指定注解
        return method.getAnnotation(tClass);
    }


}
