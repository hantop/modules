package com.fenlibao.p2p.util.api.redis;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.util.api.annotations.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 更新、删除缓存自行写去
 * 这里只是对一些几乎不变的数据实现简单的缓存
 * Created by zcai on 2017/2/27.
 */
@Aspect
@Component
public class RedisCacheAspect {

    private static final Logger log = LogManager.getLogger(RedisCacheAspect.class);
    private static final String DELIMITER = ":";

    @Around("execution(* com.fenlibao.p2p.dao.ShopTreasureDao.getCloseShopTreasure(..))"
            + "|| execution(* com.fenlibao.p2p.dao.ShopTreasureDao.getBidInfoList(..))"
            + "|| execution(* com.fenlibao.p2p.dao.bid.BidInfoDao.getBidBorrowerInfo(..))")
    public Object cache(ProceedingJoinPoint jp) throws Throwable {
        String className = jp.getTarget().getClass().getName();
        String methodName = jp.getSignature().getName();
        Object[] args = jp.getArgs();

        String key = genKey(className, methodName, args);
        log.debug("缓存key:{}", key);
        Method method = ((MethodSignature)jp.getSignature()).getMethod();
        Class modelType = method.getAnnotation(RedisCache.class).type();
        String cacheFlag = method.getAnnotation(RedisCache.class).cacheFlag();
        if (StringUtils.isNotBlank(cacheFlag) && key.indexOf(cacheFlag) < 0) {
            return jp.proceed(args);
        }
        String value;
        Object result;
        try (Jedis jedis = RedisFactory.getResource()) {
            jedis.select(7);
            value = jedis.get(key);
            if (value == null) {
                log.debug("缓存未命中,key:{}", key);
                result = jp.proceed(args);
                String json = serialize(result);
                jedis.set(key, json);
                jedis.expire(key, 1800);
                return result;
            }
            log.debug("缓存命中,value={}", value);
            Class returnType = ((MethodSignature) jp.getSignature()).getReturnType();
            result = deserialize(value, returnType, modelType);
        }
        return result;
    }

    private String genKey(String className, String menthodName, Object[] args) {
        StringBuilder sb = new StringBuilder(className);
        sb.append(DELIMITER)
                .append(menthodName)
                .append(DELIMITER);
        for (Object obj : args) {
            sb.append(JSON.toJSONString(obj).replaceAll(DELIMITER, "").replaceAll("\"", "")).append(DELIMITER);
        }
        return sb.toString();
    }

    private String serialize(Object target) {
        return JSON.toJSONString(target);
    }

    private Object deserialize(String jsonStr, Class clazz, Class modelType) {
        if (clazz.isAssignableFrom(List.class)) {
            return JSON.parseArray(jsonStr, modelType);
        }
        return JSON.parseObject(jsonStr, clazz);
    }

}
