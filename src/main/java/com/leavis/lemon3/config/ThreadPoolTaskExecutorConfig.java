package com.leavis.lemon3.config;

import com.leavis.lemon3.mdc.ThreadPoolExecutorMdcWrapper;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: paynejlli
 * @Description: 线程池配置
 * @Date: 2024/8/20 15:18
 */
@Configuration
public class ThreadPoolTaskExecutorConfig {

    // 最大可用的CPU核数
    public static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    @Bean
    public ThreadPoolExecutorMdcWrapper getExecutor() {
        ThreadPoolExecutorMdcWrapper executor = new ThreadPoolExecutorMdcWrapper();
        executor.setCorePoolSize(PROCESSORS * 2);
        executor.setMaxPoolSize(PROCESSORS * 4);
        executor.setQueueCapacity(50);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("Task-A");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
