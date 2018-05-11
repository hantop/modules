package com.fenlibao.platform.service;

import redis.clients.jedis.Jedis;

/**
 * Created by Lullaby on 2016/2/25.
 */
public interface RedisService {

    Jedis getInstance();
    
    /**
     * redis 10号库（platform使用这个）
     * @return
     */
    Jedis getInstance_10();

    Jedis getInstance(int dbIndex);

}
