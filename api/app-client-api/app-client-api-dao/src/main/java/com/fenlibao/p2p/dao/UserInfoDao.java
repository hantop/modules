package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserSecurityAuthentication;
import com.fenlibao.p2p.model.form.user.Auth;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.vo.channel.RegisterChannelVO;
import com.fenlibao.p2p.model.vo.user.UserMemberInfoVO;

import java.util.List;
import java.util.Map;

public interface UserInfoDao {
	/**
	 * 根据手机号或用户名获取用户信息
	 * @param map
	 * @return
	 */
	public UserInfo getUserInfoByPhoneNumOrUsername(Map<String, Object> map);

	/**
	 * 新增用户安全认证
	 * @param authentication
	 * @return
	 */
	public int addUserSecurityAuthentication(UserSecurityAuthentication authentication);
	
	/**
	 * 修改用户名
	 */
	public int updateUserName(Map map);
	
	/**
	 * 新增用户基本信息
	 */
	public int addUser(UserInfo userInfo);
	
	/**
	 * 根据手机号查询用户是否存在 
	 */
	public int checkUserCount(Map map);
	
	/**
	 * 修改密码
	 */
	public int updatePassword(Map map);
	
	/**
	 * 根据用户名或手机号或用户id获取用户账户信息 
	 */
	public UserAccountInfoVO getUserAccountInfo(Map<String, String> map);
	
	public UserInfo getUserInfo(Map map);
	
	public int getUserCount(Map map);
	
	public void updateUser(Map map);

	/**
	 * 修改昵称
	 * @param userId
	 * @param nickname
	 * @return
	 */
	int modifyNickName(Map<String, Object> params);
	
	/**
	 * 验证接口(银行卡删除,手机邮箱第三方账号解绑验证)
	 * @param username
	 * @param password
	 * @return
	 */
	Integer verify(Map<String, Object> params);
	
	/**
	 * 根据用户名判断是否存在该用户
	 * @param username
	 * @return userId ? userId : null
	 */
	Integer existUsername(String username);

	/**
	 * 连连 实名认证 记录保存
	 * @param authMap
	 */
	void insertlianLianAuth(Map authMap) throws Exception;

	/**
	 * 获取保存的实名记录的错误
	 * @param userName
	 */
	int getUserLoginError(String userName) throws Exception;

	/**
	 * 获取保存的实名记录的错误
	 * @param id
	 */
	int getUserAuthError(int id) throws Exception;

	/**
	 * 更新实名记录的错误
	 * @param map
	 */
	void updateUserLoginError(Map map) throws Exception;

	/**
	 * 验证身份证
	 * @param identityNo 身份证号码
	 * @param identityName 身份证姓名
	 * @return
	 */
	boolean verifyIdentity(String identityNo, String identityName, Integer userId);
	
	/**
	 * 获取用户认证状态
	 * <p>手机认证、实名认证、是否设置交易密码
	 * @param userId
	 * @return
	 */
	Auth getAuthStatus(int userId);

	/** 
	 * @Title: addMemberPointsAccount 
	 * @Description: 添加用户积分账户
	 * @param paramMap
	 * @return: void
	 */
	public void addMemberPointsAccount(Map<String, Object> paramMap);

	/**
	 * 获取用户第一次登陆状态
	 * @param userId
	 * @return
     */
	String getUserFirstLoginState(int userId);

	/**
	 * 更新用户是否第一次登录状态
	 * @param userId
	 * @param isLoginState
     * @return
     */
	int updateUserFirstLoginState(int userId, String isLoginState);

	/**
	 * 获取用户注册渠道信息
	 * @param userId
	 * @return
	 */
	RegisterChannelVO getChannelInfo(int userId);
	/**
	 * 校验是否可随时退出标白名单用户
	 * @param userMap
	 * @return
	 */
    int checkWhiteBoard(Map<String, Object> userMap);

	UserMemberInfoVO getMemberInfo(String phoneNum);

	void updatePointsAccount(String userId, int pointsAccountId);

	void updatePointsConsumeRecord(String userId, int memberId);

	void updateMerchantMemberUserId(String userId, int memberId);

	/**
	 * 获取用户的Baofoo银行卡的信息
	 * @param userMap
	 * @return
	 */
    List<Map<String,Object>> getUserBaofooCardInfo(Map<String, Object> userMap);

	/**
	 * 判断y用户是否开通存管
	 * @param map
	 * @return
	 */
	String isXWaccount(Map<String, Object> map);

	/**
	 * 获取企业信息
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getEnterpriseInfo(Integer userId);
}
