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
import com.fenlibao.p2p.model.entity.creditassignment.TransferInDetail;
import com.fenlibao.p2p.model.entity.creditassignment.TransferInInfo;
import com.fenlibao.p2p.model.entity.creditassignment.TransferInList;
import com.fenlibao.p2p.model.entity.creditassignment.TransferOutInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
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
	public List<TransferOutInfo> getTransferOutList(String transferStatus, String isTransfer, int start, int limit, Date transferApplyforTime,int cgNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("transferStatus", transferStatus);
		map.put("isTransfer", isTransfer);
		map.put("start", start);
		map.put("limit", limit);
		map.put("transferApplyforTime", transferApplyforTime);
		map.put("cgNum", cgNum);
		return sqlSession.selectList(MAPPER+"getTransferOutList",map);
	}

	@Override
	public int getTransferOutListTotalPages(String transferStatus, String isTransfer,int cgNum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("transferStatus", transferStatus);
		map.put("isTransfer", isTransfer);
		map.put("cgNum", cgNum);
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
	public List<TransferInInfo> getTransferInInfoList(int userId, Date timestamp, int status, VersionTypeEnum versionTypeEnum) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("timestamp", timestamp);
		map.put("status", status);
		map.put("versionType", versionTypeEnum == null ? VersionTypeEnum.PT.getIndex() : versionTypeEnum.getIndex());
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
	public List<TransferOutInfo> getAnytimeQuitList(String transferStatus, String isTransfer, int limit, Date transferApplyforTime, int anytimeQuit) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("transferStatus", transferStatus);
		map.put("isTransfer", isTransfer);
		map.put("limit", limit);
		map.put("transferApplyforTime", transferApplyforTime);
		map.put("anytimeQuit", anytimeQuit);
		return sqlSession.selectList(MAPPER+"getAnytimeQuitList",map);
	}

	@Override
	public Map getExpectedProfit(int zqId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("zqId", zqId);
		return sqlSession.selectOne(MAPPER+"getExpectedProfit",map);
	}


	@Override
	public List<TransferInInfo> getTransferInAndPlanInfoList(int userId, Date timestamp, int status) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("timestamp", timestamp);
		map.put("status",status);
		return sqlSession.selectList(MAPPER+"getTransferInAndPlanInfoList",map);
	}
}
