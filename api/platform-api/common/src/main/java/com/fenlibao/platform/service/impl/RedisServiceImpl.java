package com.fenlibao.platform.service.impl;

import com.fenlibao.platform.common.redis.RedisFactory;
import com.fenlibao.platform.model.RedisConst;
import com.fenlibao.platform.service.RedisService;
import redis.clients.jedis.Jedis;

/**
 * Created by Lullaby on 2016/2/25.
 */
public class RedisServiceImpl implements RedisService {

    public Jedis getInstance() {
        return RedisFactory.getResource();
    }

    public Jedis getInstance(int dbIndex) {
        Jedis jedis = RedisFactory.getResource();
        jedis.select(dbIndex);
        return jedis;
    }

    @Override
    public Jedis getInstance_10() {
        Jedis jedis = RedisFactory.getResource();
        jedis.select(RedisConst.DEFAULT_PLATFORM_REDIS_INDEX);
        return jedis;
    }
}
