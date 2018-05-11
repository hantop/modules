package com.fenlibao.p2p.util.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * redis连接池
 *
 * @author Lullaby
 *
 */
public class RedisFactory {

    private static final JedisPool POOL;

    private static final int SESSION_TIMEOUT;

    public static final JedisPool getPool() {
        return POOL;
    }

    public static final int getSessionTimeout() {
        return SESSION_TIMEOUT;
    }

    public static Jedis getResource() {
        return POOL.getResource();
    }

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("redis", new Locale("zh", "CN"));
        if (bundle == null) {
            throw new IllegalArgumentException(
                    "[redis.properties] is not found!");
        }
        SESSION_TIMEOUT = Integer.valueOf(bundle.getString("redis.session.timeout"));
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(bundle.getString("redis.pool.maxActive")));
        config.setMaxIdle(Integer.valueOf(bundle.getString("redis.pool.maxIdle")));
        config.setMaxWaitMillis(Long.valueOf(bundle.getString("redis.pool.maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(bundle.getString("redis.pool.testOnBorrow")));
        config.setTestOnReturn(Boolean.valueOf(bundle.getString("redis.pool.testOnReturn")));
        POOL = new JedisPool(
                config, bundle.getString("redis.ip"),
                Integer.valueOf(bundle.getString("redis.port")),
                Integer.valueOf(bundle.getString("redis.timeout")),
                bundle.getString("redis.password"));
    }

}
