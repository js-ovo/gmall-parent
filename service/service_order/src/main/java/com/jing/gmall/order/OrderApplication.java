package com.jing.gmall.order;

import com.jing.gmall.common.config.exception.annotation.EnableExceptionHandler;
import com.jing.gmall.common.interceptor.EnableUserAuthFeignInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringCloudApplication
@MapperScan("com.jing.gmall.order.mapper")
@EnableFeignClients({"com.jing.gmall.feignclients.cart",
        "com.jing.gmall.feignclients.product",
        "com.jing.gmall.feignclients.user",
        "com.jing.gmall.feignclients.ware"})
@EnableUserAuthFeignInterceptor
@EnableTransactionManagement
@EnableExceptionHandler
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
