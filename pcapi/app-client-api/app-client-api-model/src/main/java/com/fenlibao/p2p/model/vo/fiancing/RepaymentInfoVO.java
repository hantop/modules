package com.fenlibao.p2p.model.vo.fiancing;

import java.util.List;

/**
 * Created by laubrence on 2016/3/29.
 */
public class RepaymentInfoVO {

    String collectInterest;//代收本息

    int leftPeriod; //剩余期数

    String repaymentMode;//还款方式(DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清)

    long nextRepaymentDate;//下次还款日期

    long expireRepaymentDate;//到期日期

    String totalRepaymentStatus;//还款状态（HKZ:还款中;YJQ:已还清）

    List<RepaymentItemVO> repaymentItemList;//还款计划列表


    
    public String getCollectInterest() {
		return collectInterest;
	}

	public void setCollectInterest(String collectInterest) {
		this.collectInterest = collectInterest;
	}

	public int getLeftPeriod() {
		return leftPeriod;
	}

	public void setLeftPeriod(int leftPeriod) {
		this.leftPeriod = leftPeriod;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}

	public long getNextRepaymentDate() {
		return nextRepaymentDate;
	}

	public void setNextRepaymentDate(long nextRepaymentDate) {
		this.nextRepaymentDate = nextRepaymentDate;
	}

	public long getExpireRepaymentDate() {
		return expireRepaymentDate;
	}

	public void setExpireRepaymentDate(long expireRepaymentDate) {
		this.expireRepaymentDate = expireRepaymentDate;
	}

	public String getTotalRepaymentStatus() {
		return totalRepaymentStatus;
	}

	public void setTotalRepaymentStatus(String totalRepaymentStatus) {
		this.totalRepaymentStatus = totalRepaymentStatus;
	}

	public List<RepaymentItemVO> getRepaymentItemList() {
        return repaymentItemList;
    }

    public void setRepaymentItemList(List<RepaymentItemVO> repaymentItemList) {
        this.repaymentItemList = repaymentItemList;
    }

}
