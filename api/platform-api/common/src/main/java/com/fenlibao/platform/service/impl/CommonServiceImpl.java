package com.fenlibao.platform.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import com.fenlibao.platform.model.BusinessAgreement;
import com.fenlibao.platform.model.BusinessInfo;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fenlibao.platform.common.enums.SystemBoolean;
import com.fenlibao.platform.common.exception.BusinessException;
import com.fenlibao.platform.common.util.AES;
import com.fenlibao.platform.common.util.MD5Util;
import com.fenlibao.platform.dao.CommonMapper;
import com.fenlibao.platform.model.RedisConst;
import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.model.vo.MerchantConfigVO;
import com.fenlibao.platform.service.CommonService;
import com.fenlibao.platform.service.RedisService;

import redis.clients.jedis.Jedis;

/**
 * Created by Lullaby on 2016/2/25.
 */
public class CommonServiceImpl implements CommonService {

	@Inject
	private CommonMapper commonMapper;

	@Inject
	private RedisService redisService;







	private static final Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

	public boolean isIPAuthorized(String ip, String appid) {
		logger.info("Client IP -> {}, appid -> {}", ip, appid);
		try (Jedis jedis = redisService.getInstance(RedisConst.DEFAULT_PLATFORM_REDIS_INDEX)) {
			String key = RedisConst.$LEGAL_IP_PREFIX.concat(appid);
			List<String> ips = jedis.lrange(key, 0, -1);
			if (ips == null || ips.size() < 1) {
				ips = commonMapper.getIp(appid);
				if (ips != null && ips.size() > 0) {
					jedis.lpush(key, ips.toArray(new String[ips.size()]));
					jedis.expire(key, RedisConst.$LEGAL_IP_TIMEOUT);
				} else {
					return false;
				}
			}
			if (ips.contains(ip)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean verifySign(Map<String, String> params) {
		boolean flag = false;
		String targetSign = params.get("sign");
		String appid = params.get("appid");
		if (StringUtils.isNoneBlank(appid, targetSign)) {
			String secret = "";
			try (Jedis jedis = redisService.getInstance(RedisConst.DEFAULT_PLATFORM_REDIS_INDEX)) {
				String key = RedisConst.$MERCHANT_SECRET_PREFIX.concat(appid);
				secret = jedis.get(key);
				if (StringUtils.isBlank(secret)) {
					secret = commonMapper.getSecret(appid);
					if (StringUtils.isNotBlank(secret)) {
						secret = AES.getInstace().decrypt2(secret);
						jedis.setex(key, RedisConst.$MERCHANT_SECRET_TIMEOUT, secret);
					} else {
						logger.info("appid[{}] can't not find secret", appid);
						return false;
					}
				}
				params.remove("sign");
				String sign = MD5Util.getSignature(params, secret);
				if (targetSign.equals(sign)) {
					flag = true;
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return flag; //true flag
	}

	@Override
	public void updateSecret(String appid, String secret, Integer status) throws Exception {
		String secret_aes = null;
		if (StringUtils.isNotBlank(secret))
			secret_aes = AES.getInstace().encrypt(secret);
		commonMapper.updateSecret(appid, secret_aes, status);
		try (Jedis jedis = redisService.getInstance(RedisConst.DEFAULT_PLATFORM_REDIS_INDEX)) {
			String key = RedisConst.$MERCHANT_SECRET_PREFIX.concat(appid);
			if (SystemBoolean.FALSE.getCode().equals(status)) {
				jedis.del(key);
			} else if (StringUtils.isNotBlank(secret)) {
				jedis.setex(key, RedisConst.$MERCHANT_SECRET_TIMEOUT, secret);
			}
		}
	}

	@Override
	public boolean updateIp(String appid, String ip) throws Exception {
		List<String> ipList = commonMapper.getIp(appid);
		boolean exist = ipList.contains(ip);
		Integer merchantId = commonMapper.getMerchantId(appid);
		if (merchantId == null || merchantId < 1) {
			throw new BusinessException(Response.MERCHANT_NOT_EXIST);
		}
		MerchantConfigVO config = commonMapper.getMerchantConfig(merchantId);
		if (config == null) {
			throw new BusinessException(Response.MERCHANT_CONFIG_NOT_EXIST);
		}
		try (Jedis jedis = redisService.getInstance(RedisConst.DEFAULT_PLATFORM_REDIS_INDEX)) {
			String key = RedisConst.$LEGAL_IP_PREFIX.concat(appid);
			if (exist) {
				jedis.lrem(key, 0, ip);
				commonMapper.updateIpStatus(config.getId(), ip, SystemBoolean.FALSE.getCode());
			} else {
				jedis.lpush(key, ip);
				Integer relsult = commonMapper.updateIpStatus(config.getId(), ip, SystemBoolean.TRUE.getCode());
				if (relsult == null || relsult < 1) {
					commonMapper.addIp(config.getId(), ip);
				}
			}
		}
		return exist;
	}

	@Override
	public boolean existsKey(String key) {
		try (Jedis jedis = redisService.getInstance(RedisConst.DEFAULT_PLATFORM_REDIS_INDEX)) {
			String _key = RedisConst.$REQUEST_CACHE_KEY.concat(key);
			if (jedis.exists(_key)) {
				return true;
			}
			jedis.setex(_key, RedisConst.$REQUEST_CACHE_TIMEOUT, key);
			return false;
		}
	}

	@Override
	public void removeKey(String key) {
		try (Jedis jedis = redisService.getInstance(RedisConst.DEFAULT_PLATFORM_REDIS_INDEX)) {
			key = RedisConst.$REQUEST_CACHE_KEY.concat(key);
			jedis.del(key);
		}
	}

	@Override
	public String getMerchantId(String appid) throws Exception {
		String merchantId = null;
		String key = RedisConst.$MERCHANT_ID_PREFIX.concat(appid);
		try (Jedis jedis = redisService.getInstance(RedisConst.DEFAULT_PLATFORM_REDIS_INDEX)) {
			merchantId = jedis.get(key);
			if (StringUtils.isBlank(merchantId)) {
				Integer id = commonMapper.getMerchantId(appid);
				if (id == null || id < 1) {
					throw new BusinessException(Response.MERCHANT_NOT_EXIST);
				}
				jedis.set(key, id.toString());
				merchantId = id.toString();
			}
		}
		return merchantId;
	}

	//TODO 应该将每个商户的信息(ip/secret/appid等)封装起来放到缓存里
	@Override
	public String getAesSecret(String appid) throws Exception {
		String secret = "";
		String key = RedisConst.$MERCHANT_SECRET_AES_PREFIX.concat(appid);
		try (Jedis jedis = redisService.getInstance_10()) {
			secret = jedis.get(key);
			if (StringUtils.isBlank(secret)) {
				secret = commonMapper.getAesSecret(appid);
				if (StringUtils.isBlank(secret)) {
					throw new BusinessException("can not find aes secret");
				}
				secret = AES.getInstace().decrypt2(secret);
				jedis.setex(key, RedisConst.$MERCHANT_SECRET_TIMEOUT, secret);
			}
		}
		return secret;
	}

	@Override
	public int getBusinessUser(Map<String,Object> map) {
		return commonMapper.getBusinessUser(map);
	}

	@Override
	public int addBusinessRequest(Map<String,Object> map) {
		return commonMapper.addBusinessRequest(map);
	}

	@Override
	public int updateBusinessRequest(Map<String,Object> map) throws Exception {
		return commonMapper.updateBusinessRequest(map);
	}

	@Override
	public int addBusinessUserInfo(BusinessInfo businessInfo) {
		return commonMapper.addBusinessUserInfo(businessInfo);
	}

	@Override
	public String getBusinessRequest(Map<String, Object> map) {
		return commonMapper.getBusinessRequest(map);
	}

	@Override
	public void addBusinessAgreement(BusinessAgreement businessAgreement) {
		commonMapper.addBusinessAgreement(businessAgreement);
	}

	@Transactional
	@Override
	public void registerAndAgreement(BusinessInfo businessInfo, BusinessAgreement businessAgreement) {
		commonMapper.addBusinessUserInfo(businessInfo);
		//businessAgreement.setBusinessId(businessInfo.getId());
		Map<String,Object> map =new HashMap<>();
		map.put("userId",businessAgreement.getUserId());
		map.put("businessId",businessInfo.getId());
		commonMapper.updateAgreement(map);
	}

	@Override
	public void updateAgreement(Integer userId, Integer businessId) {
		Map<String,Object> map =new HashMap<>();
		map.put("userId",userId);
		map.put("businessId",businessId);
		commonMapper.updateAgreement(map);
	}

	@Override
	public int getAgreement(Integer userId) {
		return commonMapper.getAgreement(userId);
	}

	@Override
	public void addUserExceprionResopnse(Map<String, Object> map) {
		commonMapper.addUserExceptionResopnse(map);
	}
}
