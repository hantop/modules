/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: TransferInService.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service.creditassignment 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-22 下午2:01:07 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.service.creditassignment;

import com.fenlibao.p2p.model.entity.creditassignment.*;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.util.List;
import java.util.Map;

/** 
 * @ClassName: TransferInService 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-22 下午2:01:07  
 */
public interface TransferInService {
	
	/** 
	 * @Title: purchase 
	 * @Description: 债权转让购买
	 * @param applyforId
	 * @param userId
	 * @return 
	 * @return: int
	 * @throws Exception 
	 * @throws Throwable 
	 */
	void purchase(int applyforId, int userId) throws Throwable;
	
	/** 
	 * @Title: transferInList 
	 * @Description: 我的转入债权列表
	 * @param userId
	 * @param timestamp
	 * @return
	 * @return: List<TransferInListVO>
	 */
	List<TransferInList> getTransferInList(int userId, String timestamp);
	
	/** 
	 * @Title: transferInDetail 
	 * @Description: 我的转入债权详情
	 * @param userId
	 * @param timestamp
	 * @return
	 * @return: TransferInDetailVO
	 */
	TransferInDetail getTransferInDetail(int userId, int zqId);

	List<TransferOutInfo> getTransferOutList(Map map,PageBounds pageBounds,int cgNum);

	int getTransferOutListTotalPages();

	TransferOutInfo getTransferOutDetail(int applyforId);

	/**
	 * 4.4.2.1.	可转让的债权列表
	 * @param map
	 * @param pageBounds
	 * @return
	 */
	List<TransferInInfo> getAllowTransferInInfoList(Map map,PageBounds pageBounds);

	/**
	 * 4.4.2.2.	转让中的债权列表
	 * @param map
	 * @param pageBounds
	 * @return
	 */
	List<TransferInInfo> getInTransferInInfoList(Map map,PageBounds pageBounds);

	/**
	 * 4.4.2.3.	已转让的债权列表
	 * @param map
	 * @param pageBounds
	 * @return
	 */
	List<TransferInInfo> getSuccessTransferInInfoList(Map map,PageBounds pageBounds);

	/**
	 * 4.4.2.4.	已购买的债权列表
	 * @param userId
	 * @param pageBounds
	 * @return
	 */
	List<TransferInInfo> getBuyedTransferList(String userId, PageBounds pageBounds,int cgNum);

	/**
	 * 
	 * @param userId
	 * @param timestamp
	 * @return
	 */
	List<TransferInInfo> getTransferInInfoList(int userId, String timestamp);

	TransferInInfo getTransferInInfoDetail(int userId, int creditId);

	String getUserAssignmentAgreementUrl(int userId);

	List<UserCoupons> getUserCoupons(Map map);

	/**
	 * 获取预期收益包含差价：预期收益 = 预期本息 — 转让金额
	 * */
	Map getExpectedProfit(int zqid);

	Integer getBidRecordId(int creditId);

	/**
	 * 判断是否是计划里面的债权
	 * @param integer
	 * @return
	 */
    boolean isValidPlanZq(Integer appforId);

	String getSuccessTransferDetail(int userId, int creditId);
}
