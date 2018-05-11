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

import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;

import java.math.BigDecimal;
import java.util.List;

/** 
 * @ClassName: TransferInListVO 
 * @author: laubrence
 * @date: 2015-10-23 上午11:20:39  
 */
public class TransferInInfoDetailVO extends TransferInInfoVO{

	int bidId;//标id

	long investDate;//投资时间戳

	long expireDate;//到期时间戳

	String assignmentAgreementUrl;//债权出让及受让人协议

	String borrowerUrl;//借款人信息url

	String[] lawFiles;//法律文书页面链接

	String lawFileUrl;//法律文书url

	String remark;//项目描述

	List<BidExtendGroupVO> groupInfoList; //借款自定义信息

	//2016-06-28 junda.feng
	private String repaymentMode;//还款方式(DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清)

	private String interestPaymentType;// 付息方式,ZRY:自然月;GDR:固定日;

	private Long interestTime;// 计息时间

	private String[] assetTypes;//标的资产类型
	
	//junda.feng 20160802
	private String creditAmount;// 债权金额
	private String expectedAmount;//预期本息
	private String totallAmount;//待收本息

	private Boolean jxFlag;//是否已加息

	private double bidInterestRise;

	public Boolean getJxFlag() {
		return jxFlag;
	}

	public void setJxFlag(Boolean jxFlag) {
		this.jxFlag = jxFlag;
	}

	public String getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(String creditAmount) {
		this.creditAmount = creditAmount;
	}

	public String getExpectedAmount() {
		return expectedAmount;
	}

	public void setExpectedAmount(String expectedAmount) {
		this.expectedAmount = expectedAmount;
	}

	public String getTotallAmount() {
		return totallAmount;
	}

	public void setTotallAmount(String totallAmount) {
		this.totallAmount = totallAmount;
	}

	public String[] getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(String[] assetTypes) {
		this.assetTypes = assetTypes;
	}

	public String getInterestPaymentType() {
		return interestPaymentType;
	}

	public void setInterestPaymentType(String interestPaymentType) {
		this.interestPaymentType = interestPaymentType;
	}

	public Long getInterestTime() {
		return interestTime;
	}

	public void setInterestTime(Long interestTime) {
		this.interestTime = interestTime;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
	}

	public String getAssignmentAgreementUrl() {
		return assignmentAgreementUrl;
	}

	public void setAssignmentAgreementUrl(String assignmentAgreementUrl) {
		this.assignmentAgreementUrl = assignmentAgreementUrl;
	}

	public int getBidId() {
		return bidId;
	}

	public void setBidId(int bidId) {
		this.bidId = bidId;
	}

	public String getBorrowerUrl() {
		return borrowerUrl;
	}

	public void setBorrowerUrl(String borrowerUrl) {
		this.borrowerUrl = borrowerUrl;
	}

	public long getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(long expireDate) {
		this.expireDate = expireDate;
	}

	public List<BidExtendGroupVO> getGroupInfoList() {
		return groupInfoList;
	}

	public void setGroupInfoList(List<BidExtendGroupVO> groupInfoList) {
		this.groupInfoList = groupInfoList;
	}

	public long getInvestDate() {
		return investDate;
	}

	public void setInvestDate(long investDate) {
		this.investDate = investDate;
	}

	public String[] getLawFiles() {
		return lawFiles;
	}

	public void setLawFiles(String[] lawFiles) {
		this.lawFiles = lawFiles;
	}

	public String getLawFileUrl() {
		return lawFileUrl;
	}

	public void setLawFileUrl(String lawFileUrl) {
		this.lawFileUrl = lawFileUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getBidInterestRise() {
		return bidInterestRise;
	}

	public void setBidInterestRise(double bidInterestRise) {
		this.bidInterestRise = bidInterestRise;
	}
}
