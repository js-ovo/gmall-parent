package com.jing.gmall.common.config.threadpool.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.pool")
@Data
public class ThreadPoolProperties {
    /**
     * 核心线程数
     */
    private Integer corePoolSize;
    /**
     * 最大线程数
     */
    private Integer maximumPoolSize;

    /**
     * 活跃时间
     */
    private long keepAliveTime;


    /**
     * 阻塞队列的长度
     */
    private Integer queueLength;



}
