package com.jing.gmall.common.config.minio.annotation;

import com.jing.gmall.common.config.minio.MinioAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启Minio
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MinioAutoConfiguration.class)
public @interface EnableMinio {
}
