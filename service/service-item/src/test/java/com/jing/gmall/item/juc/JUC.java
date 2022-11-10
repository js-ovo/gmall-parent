package com.jing.gmall.item.juc;

import com.jing.gmall.cache.servie.CacheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * juc学习
 */
@SpringBootTest
public class JUC{


    @Autowired
    private CacheService cacheService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1(){
        List list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            int finalI = i;
            new Thread(()->{
                list.add(finalI);
                System.out.println(list);
            }).start();
        }
    }

}
