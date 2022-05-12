package com.zlh.he_ma_master.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lh
 */
@Configuration
@EnableAsync
public class HeMaExecutorConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(HeMaExecutorConfigurer.class);

    @Resource
    private ThreadPoolConfig threadPoolConfig;

    @Bean
    public Executor indexConfigServiceExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(threadPoolConfig.getCorePoolSize());
        logger.info("{}",threadPoolConfig.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(threadPoolConfig.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(threadPoolConfig.getQueueCapacity());
        //配置活跃时间
        executor.setKeepAliveSeconds(threadPoolConfig.getKeepAliveSeconds());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("indexConfig-service-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
