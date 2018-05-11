package com.fenlibao.p2p.service.common.impl;

import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.util.api.redis.RedisFactory;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by zcai on 2016/7/12.
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Override
    public boolean existsKey(String key) {
        try (Jedis jedis = RedisFactory.getResource()) {
            String _key = RedisConst.$REQUEST_CACHE_KEY.concat(key);
            if (jedis.exists(_key)) {
                jedis.expire(_key, RedisConst.$REQUEST_CACHE_TIMEOUT);
                return true;
            }
            jedis.setex(_key, RedisConst.$REQUEST_CACHE_TIMEOUT, key);
            return false;
        }
    }

    @Override
    public void removeKey(String key) {
        try (Jedis jedis = RedisFactory.getResource()) {
            key = RedisConst.$REQUEST_CACHE_KEY.concat(key);
            jedis.del(key);
        }
    }

    @Override
    public void pushMsg(String key, String message) {
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.select(RedisConst.$MQ_DATABASE_INDEX);
            jedis.lpush(key, message);
        }
    }

    @Override
    public void setRedis(String key, String value, int second) {
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.select(RedisConst.$MQ_DATABASE_INDEX);
            jedis.setex(key, second, value);
//            jedis.set(key, value);
            //jedis.expire(key,second);
        }
    }

    @Override
    public String getRedisValue(String key) {
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.select(RedisConst.$MQ_DATABASE_INDEX);
            return  jedis.get(key);
//            jedis.set(key, value);
            //jedis.expire(key,second);
        }
    }

    @Override
    public  boolean acquireLock(String lockKey ,long expired) {
        //  通过SETNX试图获取一个lock
        try (Jedis jedis = RedisFactory.getResource()) {
            boolean success = false;
            long value = System.currentTimeMillis() + expired + 1;
            long acquired = jedis.setnx(lockKey, String.valueOf(value));
            //SETNX成功，则成功获取一个锁
            if (acquired == 1) {
                success = true;
            }else { //SETNX失败，说明锁仍然被其他对象保持，检查其是否已经超时
                long oldValue = Long.valueOf(jedis.get(lockKey));
                //超时
                if (oldValue < System.currentTimeMillis()) {
                    String getValue = jedis.getSet(lockKey, String.valueOf(value));
                    // 获取锁成功
                    if (Long.valueOf(getValue) == oldValue) {
                        success = true;
                    } else {// 已被其他进程捷足先登了
                        success = false;
                    }
                }else{//未超时，则直接返回失败
                    success = false;
                }
            }
            return success;
        }
    }

    //释放锁
    @Override
    public  void releaseLock(String lockKey) {
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.del(lockKey);
        }
    }
}
