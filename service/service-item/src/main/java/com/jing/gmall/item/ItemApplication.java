package com.jing.gmall.item;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 商品详情
 */
@SpringCloudApplication
@EnableFeignClients
@EnableAspectJAutoProxy // 开启aop
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class,args);
    }
}
