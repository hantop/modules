package com.fenlibao.p2p.service.user.impl;

import com.fenlibao.p2p.common.util.token.TokenUtil;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.Constant;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.service.user.LoginStateService;
import com.fenlibao.p2p.util.redis.RedisConst;
import com.fenlibao.p2p.util.redis.RedisFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录态相关服务类
 * Created by Lullaby on 2015-10-22 17:02
 */
@Service
public class LoginStateServiceImpl implements LoginStateService {

    /**
     * 使用redis存储登录态
     *
     * @param clientType 客户端类型
     * @param userVO     用户信息对象
     * @return 返回用户token
     */
    @Override
    public String saveLoginState(String clientType, UserAccountInfoVO userVO) {
        String result;
        Map<String, String> userInfo = new HashMap<>(5);
        String userId = userVO.getUserId();
        String username = userVO.getUsername() == null ? "" : userVO.getUsername();
        String phoneNum = userVO.getPhone();
        String token = TokenUtil.createToken();
        userInfo.put("userId", userId);
        userInfo.put("username", username);
        userInfo.put("phoneNum", phoneNum);
        userInfo.put("clientType", clientType);
        userInfo.put("token", token);
        try (Jedis jedis = RedisFactory.getResource()) {
            String tokenKey = RedisConst.$LOGIN_STATE.concat(token);
            String clientKey = RedisConst.$LOGIN_CLIENT.concat(userId).concat(":").concat(clientType);
            if (String.valueOf(Constant.CLIENTTYPE_WAP).equals(clientType)
                    || String.valueOf(Constant.CLIENTTYPE_WEIXIN).equals(clientType)) {
                String exist = jedis.get(clientKey);
                if (StringUtils.isNotBlank(exist)) {
                    jedis.del(exist);
                    jedis.del(clientKey);
                }
            } else {
                String prefix = RedisConst.$LOGIN_CLIENT.concat(userId).concat(":");
                String iphone = prefix.concat(String.valueOf(Constant.CLIENTTYPE_IPHONE));
                String android = prefix.concat(String.valueOf(Constant.CLIENTTYPE_ANDROID));
                String ipad = prefix.concat(String.valueOf(Constant.CLIENTTYPE_IPAD));
                String wp = prefix.concat(String.valueOf(Constant.CLIENTTYPE_WP));
                List<String> list = jedis.mget(iphone, android, ipad, wp);
                for (String key : list) {
                    if (StringUtils.isNotBlank(key)) {
                        jedis.del(key);
                        String tokenExist = jedis.get(key);
                        if (StringUtils.isNotBlank(tokenExist)) {
                            jedis.del(tokenExist);
                        }
                    }
                }
                jedis.del(iphone, android, ipad, wp);
            }
            jedis.set(clientKey, tokenKey);
            jedis.hmset(tokenKey, userInfo);
            jedis.expire(clientKey, RedisConst.$LOGIN_STATE_TIMEOUT);
            jedis.expire(tokenKey, RedisConst.$LOGIN_STATE_TIMEOUT);
            result = token;
        }
        return result;
    }

    @Override
    public String saveLoginToken(String clientType, String userId) throws Exception{
        String result = null;
        if(StringUtils.isEmpty(clientType) || StringUtils.isEmpty(userId)){
            return result;
        }
        String token = TokenUtil.createToken();
        try (Jedis jedis = RedisFactory.getResource()) {
            String clientKey = RedisConst.$LOGIN_CLIENT.concat(userId).concat(":").concat(clientType);
            String tokenKey = RedisConst.$LOGIN_STATE.concat(token);
            if (String.valueOf(Constant.CLIENTTYPE_WAP).equals(clientType)
                    || String.valueOf(Constant.CLIENTTYPE_WEIXIN).equals(clientType)) {
                jedis.del(clientKey);
            } else {
                String prefix = RedisConst.$LOGIN_CLIENT.concat(userId).concat(":");
                String iphone = prefix.concat(String.valueOf(Constant.CLIENTTYPE_IPHONE));
                String android = prefix.concat(String.valueOf(Constant.CLIENTTYPE_ANDROID));
                String ipad = prefix.concat(String.valueOf(Constant.CLIENTTYPE_IPAD));
                String wp = prefix.concat(String.valueOf(Constant.CLIENTTYPE_WP));
                jedis.del(iphone, android, ipad, wp);
            }
            jedis.set(clientKey, tokenKey);
            jedis.expire(clientKey, RedisConst.$LOGIN_STATE_TIMEOUT);
            result = token;
        }
        return result;
    }

}
