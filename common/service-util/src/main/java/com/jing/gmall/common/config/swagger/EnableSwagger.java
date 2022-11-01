package com.jing.gmall.common.config.swagger;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(Swagger2Config.class)
public @interface EnableSwagger {
}
