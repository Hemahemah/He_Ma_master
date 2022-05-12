package com.zlh.he_ma_master.config;

import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lh
 */
@Component
@Data
@ConfigurationProperties(prefix = "pool")
public class ThreadPoolConfig {

    /**
     * 核心线程数
     */
    private int corePoolSize;

    /**
     * 最大线程数
     */
    private int maxPoolSize;

    /**
     * 队列最大容量
     */
    private int queueCapacity;

    /**
     * 活跃时间
     */
    private int keepAliveSeconds;


}
