package com.jing.gmall.common.interceptor;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({UserAuthFeignInterceptor.class})
public @interface EnableUserAuthFeignInterceptor {
}
