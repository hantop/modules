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
    private String fwxyUrl;//对应标服务协议
    private int loanMonths;//借款月数
    private int loanDays; //借款天数
    private double bidYield;//原始标年化率
    private boolean historyInfoFlag;//借款人信息
    private String base;//基础信息
    private String credit;//征信信息
    private String bankTransaction;//银行流水
    private String risk;//风控审核项目（用“|”分割）
    private String infoMsg; //借款人描述信息
    private String identify; //身份证号码
    private String phone; //手机号码
    private String company; //工作单位
    private String income; //收入描述
    private int isHouseCertified; //是否房产认证(0:无;1:有)
    private int isCarCertified; //是否车产认证(0:无;1:有)

    private List<Map<String, Object>> projectInfo; //标描述
    private List<Map<String, Object>> borrowerInfo; //借款人信息

    public String getFwxyUrl() {
        return fwxyUrl;
    }

    public void setFwxyUrl(String fwxyUrl) {
        this.fwxyUrl = fwxyUrl;
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

    public int getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(int loanDays) {
        this.loanDays = loanDays;
    }

    public int getLoanMonths() {
        return loanMonths;
    }

    public void setLoanMonths(int loanMonths) {
        this.loanMonths = loanMonths;
    }

    public double getBidYield() {
        return bidYield;
    }

    public void setBidYield(double bidYield) {
        this.bidYield = bidYield;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getInfoMsg() {
        return infoMsg;
    }

    public void setInfoMsg(String infoMsg) {
        this.infoMsg = infoMsg;
    }

    public int getIsCarCertified() {
        return isCarCertified;
    }

    public void setIsCarCertified(int isCarCertified) {
        this.isCarCertified = isCarCertified;
    }

    public int getIsHouseCertified() {
        return isHouseCertified;
    }

    public void setIsHouseCertified(int isHouseCertified) {
        this.isHouseCertified = isHouseCertified;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
