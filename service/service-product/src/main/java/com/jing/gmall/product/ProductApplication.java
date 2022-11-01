package com.jing.gmall.product;

import com.jing.gmall.common.config.minio.annotation.EnableMinio;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
@MapperScan(basePackages = "com.jing.gmall.product.mapper")
@EnableMinio
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }
}
