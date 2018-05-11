package com.fenlibao.p2p.service.base.db;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by LouisWang on 2015/8/5.
 * Spring Bean容器环境获取工具类
 */
public final class ContextHelper {
    private static ClassPathXmlApplicationContext _ctx;

    static {
        _ctx = new ClassPathXmlApplicationContext(new String[]{"classpath:spring-config.xml"});
    }

    private ContextHelper() {
    }

    public static ClassPathXmlApplicationContext getContext() {
        return _ctx;
    }
}