package com.jing.gmall.item.juc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ThreadPoolExecutor;

@SpringBootTest
public class ThreadPoolTest {
    @Autowired
    private ThreadPoolExecutor executor;

    @Test
    public void test(){
        executor.execute(()->{
            System.out.println(Thread.currentThread().getName() + "哈哈哈哈");
        });
    }
}
