package com.fenlibao.p2p.service.impl;

import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.util.redis.RedisConst;
import com.fenlibao.p2p.util.redis.RedisFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    public boolean checkToken(String token) {
        boolean flag = false;
        String tokenKey = RedisConst.$LOGIN_STATE.concat(token);
        try (Jedis jedis = RedisFactory.getResource()) {
            List<String> list = jedis.hmget(tokenKey, "token");
            if (list != null && !list.isEmpty()) {
                String redisToken = list.get(0);
                if (StringUtils.isNotBlank(redisToken) && redisToken.equals(token)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public boolean isInvalidToken(String token, String userId, String clientType) {
        boolean flag = true;
        String redisToken;
        String clientKey = RedisConst.$LOGIN_CLIENT.concat(userId).concat(":").concat(clientType);
        try (Jedis jedis = RedisFactory.getResource()) {
            redisToken = jedis.get(clientKey);
            if (StringUtils.isNotBlank(redisToken)) {
                if (redisToken.equals(RedisConst.$LOGIN_STATE.concat(token))) {
                    flag = false;
                    jedis.expire(clientKey, RedisConst.$LOGIN_STATE_TIMEOUT);
                }
            }
        }
        return flag;
    }

    public int TokenInterceptor(String token, String userId, String clientType) {
        int flag = 0;
        String redisToken;
        String clientKey = RedisConst.$LOGIN_CLIENT.concat(userId).concat(":").concat(clientType);
        try (Jedis jedis = RedisFactory.getResource()) {
            redisToken = jedis.get(clientKey);
            if (StringUtils.isNotBlank(redisToken)) {
                if (redisToken.equals(RedisConst.$LOGIN_STATE.concat(token))) {
                    flag = 22; //token 验证通过
                    jedis.expire(clientKey, RedisConst.$LOGIN_STATE_TIMEOUT);
                } else {
                    flag = 2;//token 验证不通过
                }
            } else {
                flag= 1; //userId 验证不通过
            }
        }
        return flag;
    }

}
