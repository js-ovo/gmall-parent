package com.jing.gmall.item.juc;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissonTest {
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testRedisson(){
        System.out.println(redissonClient);
    }
}
