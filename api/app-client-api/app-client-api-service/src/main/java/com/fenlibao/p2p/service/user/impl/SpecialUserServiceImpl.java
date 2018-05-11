package com.fenlibao.p2p.service.user.impl;

import com.fenlibao.p2p.dao.user.SpecialUserDao;
import com.fenlibao.p2p.model.enums.SpecialUserType;
import com.fenlibao.p2p.service.user.SpecialUserService;
import com.fenlibao.p2p.util.api.redis.RedisFactory;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * Created by zcai on 2016/8/25.
 */
@Service
public class SpecialUserServiceImpl implements SpecialUserService {

    @Resource
    private SpecialUserDao specialUserDao;

    @Override
    public List<String> getUserIds(SpecialUserType type) {
        List<String> userIds;
        try (Jedis jedis = RedisFactory.getResource()) {
            String key = RedisConst.$SPECIAL_USER_TYPE_KEY.concat(String.valueOf(type.getCode()));
            userIds = jedis.lrange(key, 0, -1);
            if (userIds == null || userIds.size() == 0) {
                userIds = specialUserDao.getUserIds(type);
                if (userIds != null && userIds.size() > 0) {
                    jedis.lpush(key, userIds.toArray(new String[userIds.size()]));
                    jedis.expire(key,RedisConst.$SPECIAL_USER_TYPE_KEY_TIMEOUT);
                } else {
                    return null;
                }
            } else {
                Collections.reverse(userIds);
            }
        }
        return userIds;
    }

    @Override
    public boolean isSpecial(String userId, SpecialUserType type) {
        List<String> userIds = this.getUserIds(type);
        if (userIds != null && userIds.size() > 0) {
            return userIds.contains(userId);
        }
        return false;
    }

    @Override
    public void lremSpecialUser(String userId, SpecialUserType type) {
        try (Jedis jedis = RedisFactory.getResource()) {
            String key = RedisConst.$SPECIAL_USER_TYPE_KEY.concat(String.valueOf(type.getCode()));
            jedis.lrem(key,0, userId);
            jedis.expire(key, RedisConst.$SPECIAL_USER_TYPE_KEY_TIMEOUT2);
        }
    }

}
