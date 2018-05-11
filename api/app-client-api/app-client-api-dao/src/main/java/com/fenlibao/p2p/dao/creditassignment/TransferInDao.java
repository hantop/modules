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

import com.fenlibao.p2p.model.entity.creditassignment.TransferInDetail;
import com.fenlibao.p2p.model.entity.creditassignment.TransferInInfo;
import com.fenlibao.p2p.model.entity.creditassignment.TransferInList;
import com.fenlibao.p2p.model.entity.creditassignment.TransferOutInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;

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

	List<TransferOutInfo> getTransferOutList(String transferStatus, String isTransfer, int start, int limit, Date nowDate,int cgNum);

	int getTransferOutListTotalPages(String transferStatus, String isTransfer,int cgNum);

	TransferOutInfo getTransferOutDetail(int applyforId, String transferStatus, String isTransfer);

	List<TransferInInfo> getTransferInInfoList(int userId, Date timestamp, int status, VersionTypeEnum versionTypeEnum);

	TransferInInfo getTransferInInfoDetail(int userId, int creditId);

	/**
	 * 获取随时退出标的债权转让列表
	 * @param transferStatus
	 * @param isTransfer
	 * @param limit
	 * @param transferApplyforTime
	 * @param anytimeQuit
	 * @return
	 */
    List<TransferOutInfo> getAnytimeQuitList(String transferStatus, String isTransfer,  int limit, Date transferApplyforTime, int anytimeQuit);
	/**
	 * 获取预期收益包含差价：预期收益 = 预期本息 — 转让金额
	 * */
	Map getExpectedProfit(int zqid);

	/**
	 * 用户债权列表（3.2版本）
	 * @param userId
	 * @param timestamp
	 * @param status
     * @return
     */
	List<TransferInInfo> getTransferInAndPlanInfoList(int userId, Date timestamp,int status);
}
