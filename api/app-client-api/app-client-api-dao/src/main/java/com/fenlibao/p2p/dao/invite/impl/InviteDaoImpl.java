/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: InviteDaoImpl.java 
 * @Prject: app-client-api-dao
 * @Package: com.fenlibao.p2p.dao.invite.impl 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-13 下午2:19:11 
 * @version: V1.1   
 */
package com.fenlibao.p2p.dao.invite.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.invite.InviteDao;
import com.fenlibao.p2p.model.entity.invite.MyAwordInfo;
import com.fenlibao.p2p.model.entity.invite.UserInviteInfo;

/** 
 * @ClassName: InviteDaoImpl 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-13 下午2:19:11  
 */
@Repository
public class InviteDaoImpl implements InviteDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "InviteMapper.";
	
	@Override
	public Map<String, Object> getMyInviteInfo(int userId) {
		return sqlSession.selectOne(MAPPER+"getMyInviteInfo", userId);
	}
	
	@Override
	public List<UserInviteInfo> getUserInviteInfoList(int userId, int pageNum){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("startPageIndex", (pageNum-1)*10);
		return sqlSession.selectList(MAPPER+"getUserInviteInfoList", map);
	}
	
	@Override
	public Map<String, Object> countMyInviteInfo(int userId) {
		return sqlSession.selectOne(MAPPER+"countMyInviteInfo", userId);
	}
	
	@Override
	public List<UserInviteInfo> getMyInviteInfoList(int userId, int pageNo, int pagesize){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("pageNo", (pageNo-1)*pagesize);
		map.put("pagesize", pagesize);
		return sqlSession.selectList(MAPPER+"getMyInviteInfoList", map);
	}

	@Override
	public List<MyAwordInfo> getMyAwordInfoList(int userId, int pageNo, int pagesize){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("pageNo", (pageNo-1)*pagesize);
		map.put("pagesize", pagesize);
		return sqlSession.selectList(MAPPER+"getMyAwordInfoList", map);
	}
	
}