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

import java.util.List;
import java.util.Map;

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
	double originalAmount;//	原始债权金额(原本的价值)
	double collectInterest;// 待收本息
    //2016-06-28 junda.feng
    private String repaymentMode;//还款方式(DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清) 
	private String interestPaymentType;// 付息方式,ZRY:自然月;GDR:固定日;
	private Long interestTime;// 计息时间
	private String fwxyUrl;//对应标服务协议
	private boolean historyInfoFlag;//借款人信息
	private String base;//基础信息
	private String credit;//征信信息
	private String bankTransaction;//银行流水
	private String risk;//风控审核项目（用“|”分割）
	private List<Map<String, Object>> projectInfo; //标描述
	private List<Map<String, Object>> borrowerInfo; //借款人信息

	public String getFwxyUrl() {
		return fwxyUrl;
	}

	public void setFwxyUrl(String fwxyUrl) {
		this.fwxyUrl = fwxyUrl;
	}

	public String getRepaymentMode() {
		return repaymentMode;
	}

	public void setRepaymentMode(String repaymentMode) {
		this.repaymentMode = repaymentMode;
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

	List<BidExtendGroupVO> groupInfoList; //借款自定义信息

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

	public double getCollectInterest() {
		return collectInterest;
	}

	public void setCollectInterest(double collectInterest) {
		this.collectInterest = collectInterest;
	}

	public double getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(double originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getBankTransaction() {
		return bankTransaction;
	}

	public void setBankTransaction(String bankTransaction) {
		this.bankTransaction = bankTransaction;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public boolean isHistoryInfoFlag() {
		return historyInfoFlag;
	}

	public void setHistoryInfoFlag(boolean historyInfoFlag) {
		this.historyInfoFlag = historyInfoFlag;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public List<Map<String, Object>> getProjectInfo() {
		return projectInfo;
	}

	public void setProjectInfo(List<Map<String, Object>> projectInfo) {
		this.projectInfo = projectInfo;
	}

	public List<Map<String, Object>> getBorrowerInfo() {
		return borrowerInfo;
	}

	public void setBorrowerInfo(List<Map<String, Object>> borrowerInfo) {
		this.borrowerInfo = borrowerInfo;
	}
}
