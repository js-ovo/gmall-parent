package com.jing.gmall.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.auth")
@Data
public class AuthUrlProperties {
    // 静态资源等,首页无需 鉴权
    private List<String> noAuthUrl;

    // 需要登录的请求
    private List<String> authUrl;

    // 远程内部调用等,不能外部直接调用
    private List<String> denyUrl;

    private String loginPage;
}
