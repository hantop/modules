package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.UserInfoDao;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserSecurityAuthentication;
import com.fenlibao.p2p.model.form.user.Auth;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.vo.channel.RegisterChannelVO;
import com.fenlibao.p2p.model.vo.user.UserMemberInfoVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
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
	public RegisterChannelVO getChannelInfo(int userId) {
		return sqlSession.selectOne(MAPPER + "getChannelInfo", userId);
	}

	@Override
	public int checkWhiteBoard(Map<String, Object> userMap) {
		return sqlSession.selectOne(MAPPER + "checkWhiteBoard", userMap);
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
	public void updateMerchantMemberUserId(String userId, int memberId) {
		Map<String, Object> params = new HashMap<>(2);
		params.put("userId", userId);
		params.put("memberId", memberId);
		sqlSession.update(MAPPER + "updateMerchantMemberUserId", params);
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
	public Map<String, Object> getEnterpriseInfo(Integer userId) {
		return sqlSession.selectOne(MAPPER + "getEnterpriseInfo", userId);
	}
}
