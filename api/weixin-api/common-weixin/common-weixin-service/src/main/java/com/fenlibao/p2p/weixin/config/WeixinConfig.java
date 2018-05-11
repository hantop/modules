package com.fenlibao.p2p.weixin.config;

import com.fenlibao.p2p.weixin.component.WeixinInfo;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 开发环境的微信配置
 * Created by Bogle on 2015/11/19.
 */
@EnableAsync
@Configuration
public class WeixinConfig implements AsyncConfigurer {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.weixin")
    public WeixinInfo weixinInfo() {
        WeixinInfo weixinInfo = new WeixinInfo();
        return weixinInfo;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setThreadNamePrefix("WXExecutor-");
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
