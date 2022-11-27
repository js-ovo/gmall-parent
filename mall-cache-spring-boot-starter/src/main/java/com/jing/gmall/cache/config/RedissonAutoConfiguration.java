package com.jing.gmall.cache.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson自动配置
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)  // 在redis配置好之后进行配置
public class RedissonAutoConfiguration {
    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties){
        Config config = new Config();
        // redisson连接需要设置协议  redis:// 或 rediss://
        config.useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" +
                        redisProperties.getPort())
                .setPassword(redisProperties.getPassword());
        // 设置redisson看门狗的时间,默认未30000ms 30s
        //config.setLockWatchdogTimeout(50000);
        return Redisson.create(config);
    }
}
