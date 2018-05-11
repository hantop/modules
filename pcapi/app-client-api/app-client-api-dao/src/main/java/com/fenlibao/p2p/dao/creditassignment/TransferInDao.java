/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: TransferInListDao.java 
 * @Prject: app-client-api-dao
 * @Package: com.fenlibao.p2p.dao.creditassignment 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-23 下午1:46:31 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.dao.creditassignment;

import com.fenlibao.p2p.model.entity.creditassignment.*;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

/** 
 * @ClassName: TransferInListDao 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-10-23 下午1:46:31  
 */
public interface TransferInDao {
	
	/** 
	 * @Title: transferInList 
	 * @Description: TODO
	 * @param userId
	 * @param timestamp
	 * @return
	 * @return: List<TransferInList>
	 */
	List<TransferInList> getTransferInList(int userId, Date timestamp);

	/** 
	 * @Title: getTransferInDetail 
	 * @Description: TODO
	 * @param userId
	 * @param zqId
	 * @return
	 * @return: TransferInDetail
	 */
	TransferInDetail getTransferInDetail(int userId, int zqId);

	List<TransferOutInfo> getTransferOutList(Map map,PageBounds pageBounds);
	
	int getTransferOutListTotalPages(String transferStatus, String isTransfer);

	TransferOutInfo getTransferOutDetail(int applyforId, String transferStatus, String isTransfer);

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

	List<TransferInInfo> getTransferInInfoList(int userId, Date timestamp);

	TransferInInfo getTransferInInfoDetail(int userId, int creditId);

	List<UserCoupons> getuserCouponsList(Map map);

	/**
	 * 获取预期收益包含差价：预期收益 = 预期本息 — 转让金额
	 * */
	Map getExpectedProfit(int zqid);

	Integer getBidRecordId(int creditId);

	/**
	 * 获取计划里面的债权数量
	 * @param appforId
	 * @return
	 */
    int getProductCount(Integer appforId);

	String getSuccessTransferDetail(Integer userId,Integer creditId);
}
