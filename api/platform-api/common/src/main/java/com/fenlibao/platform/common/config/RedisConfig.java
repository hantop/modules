package com.fenlibao.platform.common.config;

import org.aeonbits.owner.Config;

/**
 * Created by Lullaby on 2016/2/25.
 */
@Config.Sources("classpath:redis.properties")
public interface RedisConfig extends Config {

    @Config.Key("redis.host")
    String host();

    @Config.Key("redis.port")
    int port();

    @Config.Key("redis.password")
    String password();

    @Config.Key("redis.timeout")
    int timeout();

    @Config.Key("redis.pool.maxTotal")
    int maxTotal();

    @Config.Key("redis.pool.maxIdle")
    int maxIdle();

    @Config.Key("redis.pool.maxWaitMillis")
    int maxWaitMillis();

    @Config.Key("redis.pool.testOnBorrow")
    boolean testOnBorrow();

    @Config.Key("redis.pool.testOnReturn")
    boolean testOnReturn();

}
