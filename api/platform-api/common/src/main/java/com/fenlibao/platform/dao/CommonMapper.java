package com.fenlibao.platform.dao;

import com.fenlibao.platform.model.BusinessAgreement;
import com.fenlibao.platform.model.BusinessInfo;
import com.fenlibao.platform.model.IPConfig;
import com.fenlibao.platform.model.vo.MerchantConfigVO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * Created by Lullaby on 2016/2/25.
 */
public interface CommonMapper {

	IPConfig getExistIP(@Param("ip") String ip);

	/**
	 * 根据appid获取秘钥
	 * @param appid
	 * @return
	 */
	String getSecret(String appid);

	/**
	 * 更新秘钥
	 * @param appid
	 * @param secret
	 * @param status 使用状态（1、启用；0、停用）
	 */
	void updateSecret(@Param("appid")String appid, @Param("secret")String secret, @Param("status")Integer status);

	/**
	 * 获取商户ip
	 * @param appid
	 * @return
	 */
	List<String> getIp(String appid);

	/**
	 * 获取商户ID
	 * @param appid
	 * @param secret
	 * @return
	 */
	Integer getMerchantId(@Param("appid")String appid);

	/**
	 * 添加ip
	 * @param appid
	 * @param ip
	 * @throws Exception
	 */
	void addIp(@Param("configId")Integer configId, @Param("ip")String ip) throws Exception;

	/**
	 * 更新ip状态
	 * @param appid
	 * @param ip
	 * @param status
	 * @throws Exception
	 */
	Integer updateIpStatus(@Param("configId")Integer configId,
						   @Param("ip")String ip, @Param("status")Integer status) throws Exception;

	/**
	 * 获取商户配置
	 * @param merchantId
	 * @return
	 */
	MerchantConfigVO getMerchantConfig(Integer merchantId);

	/**
	 * 获取渠道号
	 * @param appid
	 * @return
	 */
	String getChannelCode(Integer merchantId);

	/**
	 * 获取用户ID
	 * @param MerchantId
	 * @param openid
	 * @return
	 */
	String getUserId(@Param("merchantId")String merchantId, @Param("openid")String openid);

	/**
	 * 获取商户的AES密钥
	 * @param appid
	 * @return
	 */
	String getAesSecret(String appid);

	/**
	 * 判断是否委托开户
	 * @param map
	 * @return
	 */
	Integer getBusinessUser(Map<String,Object> map);


	/**
	 * 插入开户请求记录数据
	 * @param map
	 */
	Integer addBusinessRequest(Map<String,Object> map);

	/**
	 * 更新开户请求记录数据状态
	 * @param map
	 * @throws Exception
	 */
	Integer updateBusinessRequest(Map<String,Object> map)  throws Exception;

	/**
	 * 插入开户信息数据
	 */
	Integer addBusinessUserInfo(BusinessInfo businessInfo);

	/**
	 * 判断是否有请求记录
	 * @param map
	 * @return
	 */
	String getBusinessRequest(Map<String,Object> map);

	/**
	 * 插入协议签订数据
	 */
	void addBusinessAgreement(BusinessAgreement businessAgreement);

	/**
	 * 更新开户协议信息
	 * @param map
	 */
	void updateAgreement(Map<String,Object> map);

	/**
	 * 获取用户协议
	 * @param userId
	 * @return
	 */
	Integer getAgreement(Integer userId);

	void addUserExceptionResopnse(Map<String,Object> map);
}
