package com.fenlibao.platform.dao.member;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.fenlibao.platform.model.Restriction;
import com.fenlibao.platform.model.member.MemberConsumeRecord;
import com.fenlibao.platform.model.member.MemberPoints;
import com.fenlibao.platform.model.member.MerchantMember;
import com.fenlibao.platform.model.p2p.model.UserInfo;
import com.fenlibao.platform.model.p2p.model.UserRedPacketInfo;
import com.fenlibao.platform.model.p2p.model.UserSecurityAuthentication;

public interface MemberMapper {

	/**
	 * 通过手机号码获取用户ID
	 * @param phoneNum
	 * @return
	 */
	Integer getIdByPhoneNum(String phoneNum);
	
	/**
	 * 根据用户ID获取openid
	 * @param userId
	 * @return
	 */
	String getOpenid(String userId);
	
	/**
	 * 创建会员信息
	 * @param businessId
	 * @param openid
	 * @param userId
	 * @throws Exception
	 */
	void createMemberInfo(Map<String, String> params) throws Exception;
	
	/**
	 * 新增用户信息
	 * @param userInfo
	 * @throws Exception
	 */
	void addUser(UserInfo userInfo) throws Exception;
	
	/**
	 * 新增用户安全认证表
	 * @param authentication
	 * @throws Exception
	 */
	void addUserSecurityAuthentication(UserSecurityAuthentication authentication) throws Exception;
	
	/**
	 *  新增用户基础信息，兴趣类型为理财
	 * @param userId
	 * @throws Exception
	 */
	void addUserBaseInfo(String userId) throws Exception;
	
	/**
	 * 分别新增资金账户(往来账户、风险保证金账户、锁定账户)
	 * @param params
	 * @throws Exception
	 */
	void addUserAccount(Map<String, String> params) throws Exception;
	
	/**
	 * 用户信用账户表
	 * @param userId
	 */
	void addUserCredit(String userId);
	
	/**
	 * 用户信用账户表
	 * @param userId
	 */
	void addUserFinancing(String userId);
	/**
	 * 优选理财统计表
	 * @param userId
	 */
	void addUserBestFinancing(String userId);
	
	/**
	 * 获取信用认证项(T5123)
	 * @return
	 */
	List<Integer> getCreditAuthItem();
	
	/**
	 * 新增用户认证信息(T6120)
	 * @param userId
	 * @param creditAuthInfoId
	 */
	void addUserCreditAuthInfo(@Param("userId")String userId, 
			@Param("creditAuthInfoId")Integer creditAuthInfoId);
	
	/**
	 * 新增用户信用档案表(T6144)
	 * @param userId
	 */
	void addUserCreditArchive(String userId);
	
	/**
	 * 添加用户来源
	 * @param userId
	 * @param channelCode 渠道编码
	 * @param clientTypeId 客户端类型
	 */
	void addUserOrigin(@Param("userId")String userId, @Param("channelCode")String channelCode, 
			@Param("clientTypeId")Integer clientTypeId);
	
	/**
	 * 初始化积分账号
	 * @param userId
	 */
	void initIntegralAccount(Map<String, String> params);

	MerchantMember getMerchantMember(@Param("openid") String openid);

	int saveIntegralRecord(MemberPoints points);

	int saveConsumeRecord(MemberConsumeRecord record);

	Restriction getAppidAndSecret(@Param("appid") String appid, @Param("secret") String secret);
	
	/**
	 * 根据appid获取响应秘钥
	 * @param appid
	 * @return
	 */
	String getSecret(String appid);
	
	/**
	 * 获取活动红包类型
	 * @param type
	 * @return
	 */
	List<UserRedPacketInfo> getActivityRedPacketByType(Integer type);
	
	/**
	 * 添加用户红包
	 * @param validTime
	 * @param redPacketId
	 * @param userId
	 */
	void addUserRedpacket(@Param("validTime")String validTime, @Param("redPacketId")int redPacketId, @Param("userId")String userId);

	void addMemberPhoneNum(@Param("phoneNum") String phoneNum, @Param("memberId") int memberId,
						   @Param("pointsAccountId") int pointsAccountId, @Param("channelCode") String channelCode) throws Exception;

	String getOpenidByPhoneNum(@Param("phoneNum") String phoneNum);
}
