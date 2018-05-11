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

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @ClassName: TransferInListVO
 * @author: laubrence
 * @date: 2015-10-23 上午11:20:39
 */
public class TransferOutDetailVO {

	int applyforId; //转让申请id

    int bidId;//标id

	String transferTitle;//转让标题

    double originalCreditAmount;//原始债权金额

	double transferOutValue;//转出价格

	double yearYield;//标的年化收益率

	int	surplusDays;//债权剩余天数

    double collectInterest;//待收本息

    String assignmentAgreementUrl;//债权转让协议

    String repaymentMode;//还款方式

	String[] assetTypes;//标的资产类型

    private String remark;//借款描述

    private String lawFileUrl;//法律文件HTML页面URL

    private String[] lawFiles;//法律文件链接

    private String borrowerUrl;//借款人信息html页面url

    private List<BidExtendGroupVO> groupInfoList; //标扩展信息

    private String interestPaymentType;// 付息方式,ZRY:自然月;GDR:固定日;

    private double bidInterestRise;//标加息利率+加息券利率

    private LinkedHashMap<String,Object> projectInfo;//项目描述

    private LinkedHashMap<String,Object> borrowerInfo;//借款人信息

    private int isShowFile;//是否展示贷后文件
    
    
    public String getInterestPaymentType() {
		return interestPaymentType;
	}

	public void setInterestPaymentType(String interestPaymentType) {
		this.interestPaymentType = interestPaymentType;
	}

	public int getApplyforId() {
        return applyforId;
    }

    public void setApplyforId(int applyforId) {
        this.applyforId = applyforId;
    }

    public String[] getAssetTypes() {
        return assetTypes;
    }

    public void setAssetTypes(String[] assetTypes) {
        this.assetTypes = assetTypes;
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

    public double getCollectInterest() {
        return collectInterest;
    }

    public void setCollectInterest(double collectInterest) {
        this.collectInterest = collectInterest;
    }

    public List<BidExtendGroupVO> getGroupInfoList() {
        return groupInfoList;
    }

    public void setGroupInfoList(List<BidExtendGroupVO> groupInfoList) {
        this.groupInfoList = groupInfoList;
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

    public double getOriginalCreditAmount() {
        return originalCreditAmount;
    }

    public void setOriginalCreditAmount(double originalCreditAmount) {
        this.originalCreditAmount = originalCreditAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    public int getSurplusDays() {
        return surplusDays;
    }

    public void setSurplusDays(int surplusDays) {
        this.surplusDays = surplusDays;
    }

    public double getTransferOutValue() {
        return transferOutValue;
    }

    public void setTransferOutValue(double transferOutValue) {
        this.transferOutValue = transferOutValue;
    }

    public String getTransferTitle() {
        return transferTitle;
    }

    public void setTransferTitle(String transferTitle) {
        this.transferTitle = transferTitle;
    }

    public double getYearYield() {
        return yearYield;
    }

    public void setYearYield(double yearYield) {
        this.yearYield = yearYield;
    }

    public double getBidInterestRise() {
        return bidInterestRise;
    }

    public void setBidInterestRise(double bidInterestRise) {
        this.bidInterestRise = bidInterestRise;
    }

    public LinkedHashMap<String, Object> getBorrowerInfo() {
        return borrowerInfo;
    }

    public void setBorrowerInfo(LinkedHashMap<String, Object> borrowerInfo) {
        this.borrowerInfo = borrowerInfo;
    }

    public LinkedHashMap<String, Object> getProjectInfo() {
        return projectInfo;
    }

    public void setProjectInfo(LinkedHashMap<String, Object> projectInfo) {
        this.projectInfo = projectInfo;
    }

    public int getIsShowFile() {
        return isShowFile;
    }

    public void setIsShowFile(int isShowFile) {
        this.isShowFile = isShowFile;
    }
}
