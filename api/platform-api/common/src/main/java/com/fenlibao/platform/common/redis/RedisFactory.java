package com.fenlibao.platform.common.redis;

import com.fenlibao.platform.common.config.RedisConfig;
import org.aeonbits.owner.ConfigFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis连接池
 *
 * @author Lullaby
 */
public class RedisFactory {

    private static final JedisPool POOL;

    public static Jedis getResource() {
        return POOL.getResource();
    }

    static {
        RedisConfig redisConfig = ConfigFactory.create(RedisConfig.class);
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisConfig.maxTotal());
        poolConfig.setMaxIdle(redisConfig.maxIdle());
        poolConfig.setMaxWaitMillis(redisConfig.maxWaitMillis());
        poolConfig.setTestOnBorrow(redisConfig.testOnBorrow());
        poolConfig.setTestOnReturn(redisConfig.testOnReturn());
        POOL = new JedisPool(
                poolConfig,
                redisConfig.host(),
                redisConfig.port(),
                redisConfig.timeout(),
                redisConfig.password());
    }

}
