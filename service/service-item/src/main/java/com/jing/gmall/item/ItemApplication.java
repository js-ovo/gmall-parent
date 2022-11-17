package com.jing.gmall.item;

import com.jing.gmall.common.config.exception.annotation.EnableExceptionHandler;
import com.jing.gmall.common.config.threadpool.annotation.EnableThreadPool;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 商品详情
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.jing.gmall.feignclients.product",
        "com.jing.gmall.feignclients.search"})
@EnableAspectJAutoProxy // 开启aop
@EnableThreadPool // 开启使用自定义线程池
@EnableExceptionHandler
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class,args);
    }
}
