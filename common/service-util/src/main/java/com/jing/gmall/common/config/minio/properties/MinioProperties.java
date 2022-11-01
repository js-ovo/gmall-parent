package com.jing.gmall.common.config.minio.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="app.minio")
public class MinioProperties{
    // minio的地址
    private String endpoint;

    // 登录账户
    private String accessKey;

    // 登录密码
    private String secretKey;

    // 桶的名称
    private String bucketName;

}