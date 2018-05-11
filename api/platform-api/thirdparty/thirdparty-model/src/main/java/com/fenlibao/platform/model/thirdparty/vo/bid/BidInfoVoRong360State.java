package com.fenlibao.platform.model.thirdparty.vo.bid;

import java.math.BigDecimal;

import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;

/**
 * 借款标信息状态查询--融360
 * 
 * @author junda.feng
 * @date 2016年6月12日
 * 
 */
public class BidInfoVoRong360State  {
	private String projectId;
	private int status;// 1=正在投标 2=已满标（未满标但已放款的也返回此状态） 3=已流标


	public BidInfoVoRong360State(BidInfoEntity bid) throws Throwable {
		this.projectId=String.valueOf(bid.getProjectId());
		if("TBZ".equals(bid.getState()))this.status=1;
		if("YLB".equals(bid.getState()))this.status=3;
		if(!"TBZ".equals(bid.getState())&&!"YLB".equals(bid.getState()))this.status=2;
	}


	public String getProjectId() {
		return projectId;
	}


	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


}
