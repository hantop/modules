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

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.creditassignment.TransferInDetail;
import com.fenlibao.p2p.model.entity.creditassignment.TransferInInfo;
import com.fenlibao.p2p.model.entity.creditassignment.TransferInList;
import com.fenlibao.p2p.model.entity.creditassignment.TransferOutInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.OperationTypeEnum;

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
	 * @Title: purchase
	 * @Description: 债权转让购买
	 * @param applyforId
	 * @param userId
	 * @param operationTypeEnum
	 * @return
	 * @return: int
	 * @throws Exception
	 * @throws Throwable
	 */
	void purchase(Connection connection, int applyforId, int userId, OperationTypeEnum operationTypeEnum, int userPlanId) throws Throwable;
	
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

	List<TransferOutInfo> getTransferOutList(int curpage, String timestamp,int cgNum);

	int getTransferOutListTotalPages(int cgNum);

	TransferOutInfo getTransferOutDetail(int applyforId);

	List<TransferInInfo> getTransferInInfoList(int userId, String timestamp);

	TransferInInfo getTransferInInfoDetail(int userId, int creditId);

	String getUserAssignmentAgreementUrl(int userId);

	/**
	 * 获取随时退出标的债权转让列表
	 * @param curpage
	 * @param timestamp
	 * @param anytimeQuit
	 * @return
	 */
    List<TransferOutInfo> getTransferOutAnytimeQuitList(int curpage, long timestamp, int anytimeQuit);

/**
 * 获取预期收益包含差价：预期收益 = 预期本息 — 转让金额
 * */
	Map getExpectedProfit(int zqid);

	/**
	 * 用户债权转让列表（3.2以上版本）
	 * @param userId
	 * @param timestamp
	 * @param status
     * @return
     */
	List<TransferInInfo> getTransferInAndPlanInfoList(int userId, String timestamp, int status, VersionTypeEnum versionTypeEnum);
}
