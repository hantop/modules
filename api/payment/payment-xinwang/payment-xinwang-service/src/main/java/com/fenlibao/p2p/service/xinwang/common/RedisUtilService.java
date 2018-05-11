package com.fenlibao.p2p.service.xinwang.common;

/**
 * Created by zcai on 2016/7/12.
 */
public interface RedisUtilService {

    boolean existsKey(String key);

    void removeKey(String key);

    void pushMsg(String key, String message);

    void setRedis(String key, String value, int second);

    String getRedisValue(String key);

    boolean acquireLock(String lockKey, long expired);

    void releaseLock(String lockKey);
}
