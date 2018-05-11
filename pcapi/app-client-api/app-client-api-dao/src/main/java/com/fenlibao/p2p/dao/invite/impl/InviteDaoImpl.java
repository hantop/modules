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

import com.fenlibao.p2p.dao.invite.InviteDao;
import com.fenlibao.p2p.model.entity.invite.BeInviterInfo;
import com.fenlibao.p2p.model.entity.invite.MyAwordInfo;
import com.fenlibao.p2p.model.entity.invite.UserInviteInfo;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<BeInviterInfo> getBeinviterInfoList(Integer userId, Date startDate, Date endDate, String beInviterPhonenum, PageBounds pageBounds) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("beInviterPhonenum", beInviterPhonenum);
		return sqlSession.selectList(MAPPER+"getBeinviterInfoList", map, pageBounds);
	}

	@Override
    public List<MyAwordInfo> getMyAwordInfoList(int userId, Date startDate, Date endDate, String beInviterPhonenum, PageBounds pageBounds) {
    	Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("beInviterPhonenum", beInviterPhonenum);
        return sqlSession.selectList(MAPPER+"getMyAwordInfoList", map, pageBounds);
    }

    @Override
	public Map<String, Object> getMyInviteInfo(int userId) {
		return sqlSession.selectOne(MAPPER+"getMyInviteInfo", userId);
	}

	@Override
	public BigDecimal getInviteAwardSum(int userId) {
		return sqlSession.selectOne(MAPPER+"getInviteAwardSum", userId);
	}

	@Override
	public List<UserInviteInfo> getUserInviteInfoList(int userId, int pageNum){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("startPageIndex", (pageNum-1)*10);
		return sqlSession.selectList(MAPPER+"getUserInviteInfoList", map);
	}
	
}