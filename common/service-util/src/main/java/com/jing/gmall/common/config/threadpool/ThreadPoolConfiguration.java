package com.jing.gmall.common.config.threadpool;

import com.jing.gmall.common.config.threadpool.properties.ThreadPoolProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolConfiguration {


    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * int corePoolSize, 核心线程数
     * int maximumPoolSize, 最大线程数
     * long keepAliveTime, 活跃时间
     * TimeUnit unit, 单位
     * BlockingQueue<Runnable> workQueue, 阻塞队列
     * ThreadFactory threadFactory, 线程工厂
     * RejectedExecutionHandler handler  拒绝策略
     * @param threadPoolProperties
     * @return
     */

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolProperties threadPoolProperties){
        return new ThreadPoolExecutor(threadPoolProperties.getCorePoolSize(), //核心线程数
                threadPoolProperties.getMaximumPoolSize(), // 最大线程数
                threadPoolProperties.getKeepAliveTime(), // 活跃时间
                TimeUnit.MINUTES, // 时间单位
                new LinkedBlockingQueue<>(threadPoolProperties.getQueueLength()), // 阻塞队列
                new ThreadFactory() { // 自定义线程创建工厂
                    int i = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("[" + applicationName + "-核心线程-" + i++ + "] => ");
                        return thread;
                    }
                },
                //自定义拒绝策略
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
