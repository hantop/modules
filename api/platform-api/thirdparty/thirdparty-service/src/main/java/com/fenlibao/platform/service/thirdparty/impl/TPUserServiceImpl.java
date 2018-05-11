package com.fenlibao.platform.service.thirdparty.impl;

import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.fenlibao.platform.common.enums.SystemBoolean;
import com.fenlibao.platform.common.exception.BusinessException;
import com.fenlibao.platform.common.util.PasswordCryptUtil;
import com.fenlibao.platform.dao.thirdparty.TPUserMapper;
import com.fenlibao.platform.model.RedisConst;
import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.model.thirdparty.entity.TPUserEntity;
import com.fenlibao.platform.service.RedisService;
import com.fenlibao.platform.service.thirdparty.TPUserService;

import redis.clients.jedis.Jedis;

public class TPUserServiceImpl implements TPUserService {

	@Inject
	private TPUserMapper tpUserMapper;
	@Inject
    private RedisService redisService;

	@Override
	public String getToken(String username, String password) throws BusinessException {
		String token = UUID.randomUUID().toString().replaceAll("-", "");
		TPUserEntity user = tpUserMapper.get(username, PasswordCryptUtil.crypt(password));
		if (user == null) {
			throw new BusinessException(Response.TP_USER_NOT_EXIST);
		}
		if (SystemBoolean.FALSE.getCode() == user.getStatus()) {
			throw new BusinessException(Response.TP_USER_DISABLED);
		}
		try (Jedis jedis = redisService.getInstance_10()) {
			String tokenKey = RedisConst.$THIRD_PARTY_USER_TOKEN_PREFIX.concat(username);
			String usernameKey = RedisConst.$THIRD_PARTY_USER_TOKEN_PREFIX.concat(token);
			String oldToken = jedis.get(tokenKey);
			if (StringUtils.isNotBlank(oldToken)) {
				String oldUsernameKey = RedisConst.$THIRD_PARTY_USER_TOKEN_PREFIX.concat(oldToken);
				jedis.del(oldUsernameKey);
			}
			jedis.set(RedisConst.$THIRD_PARTY_USER_RESOURCE_URI_PREFIX.concat(username), user.getResourceURI());
			jedis.setex(tokenKey, RedisConst.$THIRD_PARTY_USER_TOKEN_TIMEOUT, token);
			jedis.setex(usernameKey, RedisConst.$THIRD_PARTY_USER_TOKEN_TIMEOUT, username);
		}
		return token;
	}

	@Override
	public boolean validateToken(String token, String uri) {
		boolean flag = false;
		if (StringUtils.isNoneBlank(token, uri)) {
			try (Jedis jedis = redisService.getInstance_10()) {
				String usernameKey = RedisConst.$THIRD_PARTY_USER_TOKEN_PREFIX.concat(token);
				String username = jedis.get(usernameKey);
				if (StringUtils.isNotBlank(username)) {
					String userURI = jedis.get(RedisConst.$THIRD_PARTY_USER_RESOURCE_URI_PREFIX.concat(username));
					if (StringUtils.isBlank(userURI)) {
						userURI = tpUserMapper.getResourceURI(username);
					}
					if (uri.startsWith(userURI)) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}
	
}
