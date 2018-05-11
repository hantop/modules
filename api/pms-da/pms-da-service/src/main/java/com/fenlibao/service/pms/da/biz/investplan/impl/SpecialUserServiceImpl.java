package com.fenlibao.service.pms.da.biz.investplan.impl;

import com.fenlibao.common.pms.util.constant.RedisConst;
import com.fenlibao.common.pms.util.redis.RedisFactory;
import com.fenlibao.dao.pms.da.biz.investplan.PlanBidMapper;
import com.fenlibao.model.pms.da.global.SpecialUserType;
import com.fenlibao.service.pms.da.biz.investplan.SpecialUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;

/**
 * Created by zeronx on 2017/11/29.
 */
@Service
public class SpecialUserServiceImpl implements SpecialUserService {

    @Autowired
    private PlanBidMapper planBidMapper;

    @Override
    public List<String> getUserIds(SpecialUserType type) {
        List<String> userIds;
        try (Jedis jedis = RedisFactory.getResource()) {
            String key = RedisConst.$PMS_SPECIAL_USER_TYPE_KEY.concat(String.valueOf(type.getCode()));
            userIds = jedis.lrange(key, 0, -1);
            if (userIds == null || userIds.size() == 0) {
                userIds = planBidMapper.getUserIds(type.getCode());
                if (userIds != null && userIds.size() > 0) {
                    jedis.lpush(key, userIds.toArray(new String[userIds.size()]));
                    jedis.expire(key, RedisConst.$SPECIAL_USER_TYPE_KEY_TIMEOUT);
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
            String key = RedisConst.$PMS_SPECIAL_USER_TYPE_KEY.concat(String.valueOf(type.getCode()));
            jedis.lrem(key,0, userId);
            jedis.expire(key, RedisConst.$SPECIAL_USER_TYPE_KEY_TIMEOUT2);
        }
    }

}
