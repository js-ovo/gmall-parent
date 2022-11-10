package com.jing.gmall.common.config.threadpool.annotation;

import com.jing.gmall.common.config.threadpool.ThreadPoolConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(ThreadPoolConfiguration.class)
public @interface EnableThreadPool {
}
