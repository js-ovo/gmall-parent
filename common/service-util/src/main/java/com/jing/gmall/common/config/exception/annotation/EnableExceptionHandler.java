package com.jing.gmall.common.config.exception.annotation;


import com.jing.gmall.common.config.exception.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(GlobalExceptionHandler.class)
public @interface EnableExceptionHandler {
}
