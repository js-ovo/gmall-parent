package com.jing.gmall.product;

import com.jing.gmall.common.config.minio.annotation.EnableMinio;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringCloudApplication
@MapperScan(basePackages = "com.jing.gmall.product.mapper")
@EnableMinio
@EnableTransactionManagement
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }
}
