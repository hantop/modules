/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: TransferInListVO.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.vo.creditassignment 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-23 上午11:20:39 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.model.vo.creditassignment;

import java.util.List;

/**
 * @ClassName: TransferInListVO
 * @author: laubrence
 * @date: 2015-10-23 上午11:20:39
 */
public class TransferOutListVO {

	int curpage;

	int totalpage;

	List<TransferOutInfoVO> transferOutItemList;

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public List<TransferOutInfoVO> getTransferOutItemList() {
		return transferOutItemList;
	}

	public void setTransferOutItemList(List<TransferOutInfoVO> transferOutItemList) {
		this.transferOutItemList = transferOutItemList;
	}
}
