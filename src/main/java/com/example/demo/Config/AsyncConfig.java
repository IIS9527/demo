package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

// 1. 增加异步配置类
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean("fileTaskExecutor")
    public Executor fileTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(32);  // 根据CPU核心数调整
        executor.setMaxPoolSize(300);
        executor.setQueueCapacity(10000);
        executor.setThreadNamePrefix("FileAsync-");
        executor.initialize();
        return executor;
    }
}