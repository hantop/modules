package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.SchooleInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserSecurityAuthentication;
import com.fenlibao.p2p.model.entity.WorkInfo;
import com.fenlibao.p2p.model.form.user.Auth;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.vo.channel.RegisterChannelVO;
import com.fenlibao.p2p.model.vo.user.AccountNoVO;
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
     *  修改用户基本信息
     * @param userId
     * @param nickname
     * @param schoole
     * @param companyIndustry
     * @param companySize
     * @param position
     * @param income
     * @return
     */
	int modifyInfoById(Map<String, Object> params);
	  /**
	   * 学校信息
	   */
	public int addSchooleInfo(SchooleInfo userInfo);
	public SchooleInfo getSchooleInfo(Map map);
	int modifySchooleInfo(Map<String, Object> params);
	/**
	   * 工作信息
	   */
	public int addWorkInfo(WorkInfo userInfo);
	public WorkInfo getWorkInfo(Map map);
	int modifyWorkInfo(Map<String, Object> params);
	
	
	
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

	UserMemberInfoVO getMemberInfo(String phoneNum);

	void updatePointsAccount(String userId, int pointsAccountId);

	void updatePointsConsumeRecord(String userId, int memberId);

	void updateMerchantMemberUserId(String userId, Integer memberId);

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
	 * 是否新用户
	 * @param userId
	 * @return
     */
	int isBidNewUser(int userId) throws Exception;

	/**
	 * 获取用户注册渠道信息
	 * @param userId
	 * @return
	 */
	RegisterChannelVO getChannelInfo(int userId);

	/**
	 * 是否是存管账户
	 * @param userId
	 * @return
	 */
	int isDepository(int userId) throws Exception;

	/**
	 * 获取e账户信息
	 * @param userId
	 * @return
     */
	AccountNoVO getAccountNo(int userId);

	/**
	 * 用户上一次投资该计划距离现在时间的分钟数
	 * @param accountId
	 * @param planId
     * @return
     */
	int getUserPlanLastOrder(int accountId, int planId);

	/**
	 * 用户上一次投资该计划距离现在时间的分钟数
	 * @param accountId
	 * @param planId
     * @return
     */
	int getUserInvestPlanLastOrder(int accountId, int planId);

	/**
	 * 检查用户是否白名单用户
	 * @param userMap
	 * @return
	 */
	int checkWhiteBoard(Map<String, Object> userMap);

	/**
	 * 用户的baofoo银行卡信息
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
	 * 检查企业三证或统一社会信用代码是否重复
	 * @param userMap
	 * @return
	 */
	int checkEnterpriseCertificate(Map<String, Object> userMap);

	/**
	 * 生成企业账号
	 * @return
     */
	String getZhName();

	/**
	 * 企业联系信息
	 */
	void addCompanyContactInfo(Map<String, Object> map);

	/**
	 * 企业介绍信息
	 */
	void addCompanyProfileInfo(Map<String, Object> map);

	/**
	 * 企业基础信息
	 */
	void addCompanyBaseInfo(Map<String, Object> map);

	/**
	 * 获取新网开户银行卡编码
	 * @param userId
	 * @return
     */
	Map<String,Object> getXwCardCode(Integer userId,String userType);

	/**
	 * 判断y用户是否借款
	 * @param map
	 * @return
	 */
	String isBorrower(Map<String, Object> map);

	/**
	 * 判断是否有企业信息
	 * @param integer
	 * @return
	 */
    boolean isEnterpriseUser(Integer userId);

	/**
	 * 获取企业信息
	 * @param userId
	 * @return
	 */
	Map<String,Object> getEnterpriseInfo(Integer userId);

	/**
	 * 用户是否激活
	 * @param param
	 * @return
	 */
    Integer isActive(Map<String, Object> param);

	/**
	 *
	 * @param userId
	 * @return
	 */
	UserInfo getUserInfoById(Integer userId);


	String getAuthStatusList(Map<String,Object> map);

	int updateAuthStatus(Map<String,Object> map);
}
