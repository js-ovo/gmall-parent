package com.jing.gmall.cache;

import com.jing.gmall.cache.aspect.CacheAspect;
import com.jing.gmall.cache.servie.impl.CacheServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CacheAspect.class, CacheServiceImpl.class})
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class MallCacheAutoConfiguration {

}
