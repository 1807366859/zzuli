package com.troublemaker.clockin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @BelongsProject: zzuli
 * @BelongsPackage: com.troublemaker.clockin.config
 * @Author: troublemaker
 * @CreateTime: 2022-06-08  17:04
 * @Version: 1.0
 */
@Configuration
@Slf4j
public class MyThreadPoolConfig {
    // corePoolSize     核心线程数
    // maximumPoolSize  最大线程数
    // keepAliveTime    空闲线程存活时间
    // unit             时间单位
    // workQueue        任务队列
    // threadFactory    线程工厂
    // handler          线程拒绝策略
    @Value("${pool.corePoolSize}")
    private Integer corePoolSize;
    @Value("${pool.maximumPoolSize}")
    private Integer maximumPoolSize;
    @Value("${pool.keepAliveTime}")
    private Integer keepAliveTime;
    @Value("${pool.capacity}")
    private Integer capacity;

    @Bean
    public ThreadPoolExecutor getThreadPoolExecutor() {
        log.info("-----------------初始化线程池-------------------");
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<>(capacity), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}

