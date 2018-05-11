package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.UserInfoDao;
import com.fenlibao.p2p.model.entity.SchooleInfo;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserSecurityAuthentication;
import com.fenlibao.p2p.model.entity.WorkInfo;
import com.fenlibao.p2p.model.form.user.Auth;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.vo.channel.RegisterChannelVO;
import com.fenlibao.p2p.model.vo.user.AccountNoVO;
import com.fenlibao.p2p.model.vo.user.UserMemberInfoVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserInfoDaoImpl implements UserInfoDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "UserInfoMapper.";

	@Override
	public UserInfo getUserInfoByPhoneNumOrUsername(Map<String, Object> map) {
		return sqlSession.selectOne(MAPPER+"getUserInfoByPhoneNumOrUsername", map);
	}

	@Override
	public int addUserSecurityAuthentication(
			UserSecurityAuthentication authentication) {
		return sqlSession.insert(MAPPER + "addUserSecurityAuthentication", authentication);
	}
	
	@Override
	public int updateUserName(Map map) {
		return sqlSession.update(MAPPER + "updateUserName", map);
	}
	
	@Override
	public int checkUserCount(Map map) {
		return sqlSession.selectOne(MAPPER + "checkUserCount", map);
	}

	@Override
	public int addUser(UserInfo userInfo) {
		return sqlSession.insert(MAPPER + "addUser", userInfo);
	}
	
	@Override
	public int updatePassword(Map map) {
		return sqlSession.update(MAPPER + "updatePassword", map);
	}
	
	@Override
	public UserAccountInfoVO getUserAccountInfo(Map<String, String> map) {
		return sqlSession.selectOne(MAPPER + "getUserAccountInfo", map);
	}
	
	@Override
	public UserInfo getUserInfo(Map map) {
		return sqlSession.selectOne(MAPPER+"getUserInfo", map);
	}

	@Override
	public int getUserCount(Map map) {
		return sqlSession.selectOne(MAPPER+"getUserCount", map);
	}

	@Override
	public void updateUser(Map map) {
		sqlSession.update(MAPPER+"updateUser", map);
	}

	@Override
	public int modifyNickName(Map<String, Object> params) {
		return sqlSession.update(MAPPER + "modifyNickName", params);
	}
	
	@Override
	public int modifySchooleInfo(Map<String, Object> params) {
		return sqlSession.update(MAPPER + "modifySchooleInfo", params);
	}
	@Override
	public int modifyWorkInfo(Map<String, Object> params) {
		return sqlSession.update(MAPPER + "modifyWorkInfo", params);
	}
	
	@Override
	public int modifyInfoById(Map<String, Object> params) {
		return sqlSession.update(MAPPER + "modifyInfoById", params);
	}


	@Override
	public Integer verify(Map<String, Object> params) {
		return sqlSession.selectOne(MAPPER + "verifyUser", params);
	}

	@Override
	public Integer existUsername(String userName) {
		return sqlSession.selectOne(MAPPER + "existUsername", userName);
	}

	@Override
	public void insertlianLianAuth(Map authMap)throws Exception {
		sqlSession.insert(MAPPER+"addLianLianAuth", authMap);
	}

	/**
	 * 获取保存的实名记录的错误
	 * @param userName
	 */
	@Override
	public int getUserLoginError(String userName)throws Exception {
		Integer count = sqlSession.selectOne(MAPPER + "getUserLoginError", userName);
		if(count != null){
			return count;
		}else {
			return 0;
		}
	}

	/**
	 * 获取连连实名认证的记录次数
	 * @param id
	 */
	public int getUserAuthError(int id) throws Exception{
		return sqlSession.selectOne(MAPPER + "getUserAuthError",id);
	}

	/**
	 * 更新实名记录的错误
	 * @param map
	 */
	public void updateUserLoginError(Map map) throws Exception{
		sqlSession.insert(MAPPER+"updateUserLoginError", map);
	}

	@Override
	public boolean verifyIdentity(String identityNo, String identityName, Integer userId) {
		Map<String, String> params = new HashMap<>();
		params.put("identityNo", identityNo);
		params.put("identityName", identityName);
		Integer result = sqlSession.selectOne(MAPPER + "verifyIdentity",params);
		if (result != null && userId.equals(result)) {
			return true;
		}
		return false;
	}

	@Override
	public UserMemberInfoVO getMemberInfo(String phoneNum) {
		return sqlSession.selectOne(MAPPER + "getMemberInfo", phoneNum);
	}

	@Override
	public void updatePointsAccount(String userId, int pointsAccountId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("pointsAccountId", pointsAccountId);
		sqlSession.update(MAPPER + "updatePointsAccount", params);
	}

	@Override
	public void updatePointsConsumeRecord(String userId, int memberId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("memberId", memberId);
		sqlSession.update(MAPPER + "updatePointsConsumeRecord", params);
	}

	@Override
	public void updateMerchantMemberUserId(String userId, Integer memberId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("memberId", memberId);
		sqlSession.update(MAPPER + "updateMerchantMemberUserId", params);
	}

	@Override
	public Auth getAuthStatus(int userId) {
		return sqlSession.selectOne(MAPPER + "getAuthStatus", userId);
	}

	/**
	 * @Title: addMemberPointsAccount
	 * @Description: 添加用户积分账户
	 * @param paramMap 
	 * @see com.fenlibao.p2p.dao.UserInfoDao#addMemberPointsAccount(java.util.Map) 
	 */
	@Override
	public void addMemberPointsAccount(Map<String, Object> paramMap) {
		sqlSession.insert(MAPPER + "addMemberPointsAccount", paramMap);
	}

	@Override
	public String getUserFirstLoginState(int userId) {
		return sqlSession.selectOne(MAPPER + "getUserFirstLoginState",userId);
	}

	@Override
	public int updateUserFirstLoginState(int userId, String isLoginState) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",userId);
        map.put("isLoginState",isLoginState);
		return sqlSession.update(MAPPER + "updateUserFirstLoginState",map);
	}

	@Override
	public int isBidNewUser(int userId) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId",userId);
		Integer record = sqlSession.selectOne(MAPPER + "isBidNewUser",map);
		if(record==null){
			return 0;
		}
		return (int)record;
	}

	@Override
	public RegisterChannelVO getChannelInfo(int userId) {
		return sqlSession.selectOne(MAPPER + "getChannelInfo", userId);
	}

	@Override
	public int addSchooleInfo(SchooleInfo schooleInfo) {
		return sqlSession.insert(MAPPER + "addSchooleInfo", schooleInfo);
	}

	@Override
	public SchooleInfo getSchooleInfo(Map map) {
		return sqlSession.selectOne(MAPPER + "getSchooleInfo",map);
	}

	@Override
	public int addWorkInfo(WorkInfo workInfo) {
		return sqlSession.insert(MAPPER + "addWorkInfo", workInfo);
	}

	@Override
	public WorkInfo getWorkInfo(Map map) {
		return sqlSession.selectOne(MAPPER + "getWorkInfo",map);
	}

	@Override
	public int isDepository(int userId) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId",userId);
		Integer record = sqlSession.selectOne(MAPPER + "isDepository",map);
		if(record==null){
			return 0;
		}
		return (int)record;
	}

	@Override
	public AccountNoVO getAccountNo(int userId) {
		return sqlSession.selectOne(MAPPER + "getAccountNo", userId);
	}

	/**
	 * 用户上一次投资该计划距离现在时间的分钟数
	 * @param accountId
	 * @param planId
	 * @return
	 */
	@Override
	public int getUserPlanLastOrder(int accountId, int planId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("accountId",accountId);
		map.put("planId",planId);
		return sqlSession.selectOne(MAPPER + "getUserPlanLastOrder", map);
	}

	/**
	 * 用户上一次投资该计划距离现在时间的分钟数
	 * @param accountId
	 * @param planId
	 * @return
	 */
	@Override
	public int getUserInvestPlanLastOrder(int accountId, int planId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("accountId",accountId);
		map.put("planId",planId);
		return sqlSession.selectOne(MAPPER + "getUserInvestPlanLastOrder", map);
	}

	/**
	 * 检查用户是否白名单用户
	 * @param userMap
	 * @return
	 */
	@Override
	public int checkWhiteBoard(Map<String, Object> userMap) {
		return sqlSession.selectOne(MAPPER + "checkWhiteBoard", userMap);
	}

	@Override
	public List<Map<String, Object>> getUserBaofooCardInfo(Map<String, Object> userMap) {
		return sqlSession.selectList(MAPPER+"getUserBaofooCardInfo",userMap);
	}

	@Override
	public String isXWaccount(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "isXWaccount", map);
	}

	@Override
	public int checkEnterpriseCertificate(Map<String, Object> map) {
		return sqlSession.selectOne(MAPPER+"checkEnterpriseCertificate",map);
	}

	/**
	 * 生成企业账号
	 * @return
	 */
	@Override
	public String getZhName() {
		Integer res = sqlSession.selectOne(MAPPER+"getZhName");
		if (res != null && res > 0)
		{
			DecimalFormat df = new DecimalFormat("QYZH00000000000");
			return df.format(res);
		}
		else
		{
			return "QYZH00000000000";
		}
	}

	/**
	 * 企业联系信息
     */
	public void addCompanyContactInfo(Map<String, Object> map){
		sqlSession.insert(MAPPER+"addCompanyContactInfo",map);
	}

	/**
	 * 企业介绍资料
     */
	public void addCompanyProfileInfo(Map<String, Object> map){
		sqlSession.insert(MAPPER+"addCompanyProfileInfo",map);
	}

	/**
	 * 企业基础信息
	 */
	public void addCompanyBaseInfo(Map<String, Object> map){
		sqlSession.insert(MAPPER+"addCompanyBaseInfo",map);
	}

	@Override
	public Map<String,Object> getXwCardCode(Integer userId,String userType) {
		Map<String,Object> map = new HashMap<>();
		map.put("userId",userId);
		map.put("userType",userType);
		return  sqlSession.selectOne(MAPPER + "getXwCardCode", map);
	}

	@Override
	public String isBorrower(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER + "isBorrower", map);
	}

	@Override
	public boolean isEnterpriseUser(Integer userId) {
		boolean b = false;
		Integer count = this.sqlSession.selectOne(MAPPER + "countEnterpriseUser", userId);
		if(count > 0){
			b = true;
		}
		return b;
	}

	@Override
	public Map<String, Object> getEnterpriseInfo(Integer userId) {
		return sqlSession.selectOne(MAPPER + "getEnterpriseInfo", userId);
	}

	@Override
	public Integer isActive(Map<String, Object> param) {
		return sqlSession.selectOne(MAPPER + "isActive", param);
	}

	@Override
	public UserInfo getUserInfoById(Integer userId) {
		return sqlSession.selectOne(MAPPER + "getUserInfoById", userId);
	}

	@Override
	public String getAuthStatusList(Map<String, Object> map) {
		return sqlSession.selectOne(MAPPER + "getAuthStatusList", map);
	}


	public int updateAuthStatus(Map<String, Object> map) {
		return sqlSession.update(MAPPER + "updateAuthStatus",map);
	}
}
