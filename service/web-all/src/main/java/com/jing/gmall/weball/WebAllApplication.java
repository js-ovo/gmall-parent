package com.jing.gmall.weball;

import com.jing.gmall.common.interceptor.EnableUserAuthFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EnableDiscoveryClient
//@EnableCircuitBreaker
@SpringCloudApplication
@EnableFeignClients(basePackages = "com.jing.gmall.feignclients")
@EnableUserAuthFeignInterceptor // 开启 feign调用拦截器
public class WebAllApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAllApplication.class,args);
    }
}
