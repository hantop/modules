/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: TransferInListDaoImpl.java 
 * @Prject: app-client-api-dao
 * @Package: com.fenlibao.p2p.dao.creditassignment.impl 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-24 上午10:00:34 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.dao.creditassignment.impl;

import com.fenlibao.p2p.dao.creditassignment.TransferInDao;
import com.fenlibao.p2p.model.entity.creditassignment.*;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @ClassName: TransferInListDaoImpl 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-24 上午10:00:34  
 */

@Repository
public class TransferInDaoImpl implements TransferInDao {
	
	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "CreditassignmentMapper.";

	/**
	 * @Title: transferInList
	 * @param userId
	 * @param timestamp
	 * @return 
	 */
	@Override
	public List<TransferInList> getTransferInList(int userId, Date timestamp) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("timestamp", timestamp);
		
		return sqlSession.selectList(MAPPER+"getTransferInList",map);
	}
	/**
	 * @Title: transferInDetail
	 * @param userId
	 * @param zqId
	 * @return 
	 */
	@Override
	public TransferInDetail getTransferInDetail(int userId, int zqId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("zqId", zqId);
		return sqlSession.selectOne(MAPPER+"transferInDetail",map);
	}

	@Override
	public int getTransferOutListTotalPages(String transferStatus, String isTransfer) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("transferStatus", transferStatus);
		map.put("isTransfer", isTransfer);

		return sqlSession.selectOne(MAPPER+"getTransferOutListTotalPages",map);
	}

	@Override
	public TransferOutInfo getTransferOutDetail(int applyforId, String transferStatus, String isTransfer) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("transferStatus", transferStatus);
		map.put("isTransfer", isTransfer);
		map.put("applyforId",applyforId);

		return sqlSession.selectOne(MAPPER+"getTransferOutDetail",map);
	}

	@Override
	public List<TransferInInfo> getTransferInInfoList(int userId, Date timestamp) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("timestamp", timestamp);

		return sqlSession.selectList(MAPPER+"getTransferInInfoList",map);
	}

	@Override
	public TransferInInfo getTransferInInfoDetail(int userId, int creditId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("creditId", creditId);

		return sqlSession.selectOne(MAPPER+"getTransferInInfoDetail",map);
	}
	@Override
	public List<TransferOutInfo> getTransferOutList(Map map,
			PageBounds pageBounds) {
		return sqlSession.selectList(MAPPER+"getTransferOutList",map,pageBounds);
	}

	@Override
	public List<TransferInInfo> getAllowTransferInInfoList(Map map,
			PageBounds pageBounds) {
		return sqlSession.selectList(MAPPER+"getAllowTransferInInfoList",map,pageBounds);
	}
	@Override
	public List<TransferInInfo> getInTransferInInfoList(Map map,
			PageBounds pageBounds) {
		return sqlSession.selectList(MAPPER+"getInTransferInInfoList",map,pageBounds);
	}
	@Override
	public List<TransferInInfo> getSuccessTransferInInfoList(Map map,
			PageBounds pageBounds) {
		return sqlSession.selectList(MAPPER+"getSuccessTransferInInfoList",map,pageBounds);
	}
	
	@Override
	public List<TransferInInfo> getBuyedTransferList(String userId,
												  PageBounds pageBounds,int cgNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("cgNum", cgNum);
		return sqlSession.selectList(MAPPER+"getBuyedTransferList", map, pageBounds);
	}

	@Override
	public List<UserCoupons> getuserCouponsList(Map map) {
		return sqlSession.selectList(MAPPER+"userCoupus",map);
	}

	@Override
	public Map getExpectedProfit(int zqId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("zqId", zqId);
		return sqlSession.selectOne(MAPPER+"getExpectedProfit",map);
	}

	@Override
	public Integer getBidRecordId(int creditId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("creditId", creditId);
		return sqlSession.selectOne(MAPPER+"getBidRecordId",map);
	}

	@Override
	public int getProductCount(Integer appforId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("appforId", appforId);
		return sqlSession.selectOne(MAPPER+"getProductCount",map);
	}

	@Override
	public String getSuccessTransferDetail(Integer userId, Integer creditId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("creditId", creditId);
		map.put("userId", userId);
		return sqlSession.selectOne(MAPPER+"getSuccessTransferDetail",map);
	}
}
