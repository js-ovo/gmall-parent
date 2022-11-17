package com.jing.gmall.product;

import com.jing.gmall.common.config.exception.annotation.EnableExceptionHandler;
import com.jing.gmall.common.config.minio.annotation.EnableMinio;
import com.jing.gmall.common.config.threadpool.annotation.EnableThreadPool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringCloudApplication
@MapperScan(basePackages = "com.jing.gmall.product.mapper")
@EnableMinio
@EnableTransactionManagement
@EnableFeignClients(basePackages = "com.jing.gmall.feignclients.search")
@EnableThreadPool
@EnableExceptionHandler
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }
}
