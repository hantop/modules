package com.fenlibao.p2p.service.xinwang.common.impl;

import com.fenlibao.p2p.service.xinwang.common.RedisUtilService;
import com.fenlibao.p2p.util.api.redis.RedisFactory;
import com.fenlibao.p2p.util.xinwang.RedisUtilConst;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by zcai on 2016/7/12.
 */
@Service
public class RedisUtilServiceImpl implements RedisUtilService {

    @Override
    public boolean existsKey(String key) {
        try (Jedis jedis = RedisFactory.getResource()) {
            String _key = RedisUtilConst.$REQUEST_CACHE_KEY.concat(key);
            if (jedis.exists(_key)) {
                jedis.expire(_key, RedisUtilConst.$REQUEST_CACHE_TIMEOUT);
                return true;
            }
            jedis.setex(_key, RedisUtilConst.$REQUEST_CACHE_TIMEOUT, key);
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void removeKey(String key) {
        try (Jedis jedis = RedisFactory.getResource()) {
            key = RedisUtilConst.$REQUEST_CACHE_KEY.concat(key);
            jedis.del(key);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void pushMsg(String key, String message) {
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.select(RedisUtilConst.$MQ_DATABASE_INDEX);
            jedis.lpush(key, message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setRedis(String key, String value, int second) {
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.select(RedisUtilConst.$MQ_DATABASE_INDEX);
            jedis.setex(key, second, value);
//            jedis.set(key, value);
            //jedis.expire(key,second);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getRedisValue(String key) {
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.select(RedisUtilConst.$MQ_DATABASE_INDEX);
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
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //释放锁
    @Override
    public  void releaseLock(String lockKey) {
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.del(lockKey);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
