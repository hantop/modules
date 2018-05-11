package com.fenlibao.p2p.util.api.redis;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * Created by zcai on 2017/2/12.
 */
public class RedisUtil {

    /**
     * 计数器
     * @param key
     * @param timeout
     * @return
     */
    public static int counter(String key, int timeout) {
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.select(4);
            int count = jedis.incr(key).intValue();
            if (1 == count) {
                jedis.expire(key, timeout);
            }
            return count;
        }
    }

}
