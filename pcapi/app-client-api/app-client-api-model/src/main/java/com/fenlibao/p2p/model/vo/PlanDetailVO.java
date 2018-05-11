package com.fenlibao.p2p.model.vo;

import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;

import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
public class PlanDetailVO {

    private int planId;

    private String planTitle;

    private int isNoviceBid;

    private double loanAmount;

    private double yearYield;

    private double raisedRate;

    private int loanMonths;

    private int loanDays;

    private String remark;

    private String repaymentMode;

    private String interestPaymentType;

    private String purchaseTotal;

    private String purchasedTotal;

    private int planStatus;

    private String progressRate;

    private String fwxyUrl;

    private String planLimit;

    private String  investLimit;

    private int itemType;//区分数组的item类型(0:标  1:计划)

    private String[] lawFiles;

    private String lawFileUrl;

    private List<BidExtendGroupVO> groupInfoList;

    private String guaranteeFileUrl;

    private String[] bidLabel;

    private String precisionTitle;

    private String interest;

    public String getFwxyUrl() {
        return fwxyUrl;
    }

    public void setFwxyUrl(String fwxyUrl) {
        this.fwxyUrl = fwxyUrl;
    }

    public String getInterestPaymentType() {
        return interestPaymentType;
    }

    public void setInterestPaymentType(String interestPaymentType) {
        this.interestPaymentType = interestPaymentType;
    }

    public String getInvestLimit() {
        return investLimit;
    }

    public void setInvestLimit(String investLimit) {
        this.investLimit = investLimit;
    }

    public int getIsNoviceBid() {
        return isNoviceBid;
    }

    public void setIsNoviceBid(int isNoviceBid) {
        this.isNoviceBid = isNoviceBid;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
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

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanLimit() {
        return planLimit;
    }

    public void setPlanLimit(String planLimit) {
        this.planLimit = planLimit;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getProgressRate() {
        return progressRate;
    }

    public void setProgressRate(String progressRate) {
        this.progressRate = progressRate;
    }

    public String getPurchasedTotal() {
        return purchasedTotal;
    }

    public void setPurchasedTotal(String purchasedTotal) {
        this.purchasedTotal = purchasedTotal;
    }

    public String getPurchaseTotal() {
        return purchaseTotal;
    }

    public void setPurchaseTotal(String purchaseTotal) {
        this.purchaseTotal = purchaseTotal;
    }

    public double getRaisedRate() {
        return raisedRate;
    }

    public void setRaisedRate(double raisedRate) {
        this.raisedRate = raisedRate;
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

    public double getYearYield() {
        return yearYield;
    }

    public void setYearYield(double yearYield) {
        this.yearYield = yearYield;
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

    public String getGuaranteeFileUrl() {
        return guaranteeFileUrl;
    }

    public void setGuaranteeFileUrl(String guaranteeFileUrl) {
        this.guaranteeFileUrl = guaranteeFileUrl;
    }

    public String[] getBidLabel() {
        return bidLabel;
    }

    public void setBidLabel(String[] bidLabel) {
        this.bidLabel = bidLabel;
    }

    public String getPrecisionTitle() {
        return precisionTitle;
    }

    public void setPrecisionTitle(String precisionTitle) {
        this.precisionTitle = precisionTitle;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
