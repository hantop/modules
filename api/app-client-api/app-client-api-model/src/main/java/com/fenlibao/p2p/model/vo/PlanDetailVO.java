package com.fenlibao.p2p.model.vo;

import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.bidinfo.BidExtendGroupVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/7.
 */
public class PlanDetailVO {
    private int planId;
    private String planTitle;
    private int isNoviceBid;
    private double loanAmount;
    private String bidYield;
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
    private List<String> bidLabel;//标的说明
    private String precisionTitle;
    private String interest;
    private int purchasedStatus;
    private String voteAmount;//可投金额
    private int isCG;//是否是存管类型 1：是 0：不是
    private List<String> markArray;//定向计划/标提示语
    private String comment;
    private int planCanQuit;
    private int planType;//1：月升计划  2:省心计划
    private String lowRate;//最低利率
    private String highRate;//最高利率
    private String bonusRate;//月增长利率
    private Long panicBuyingTime;//抢购时间（抢购标）
    private Long timeLeft;//剩余时间
    private Integer countdown;//倒计时配置参数
    private List<Map<String, Object>> planInfo; //计划描述

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

    public int getPlanCanQuit() {
        return planCanQuit;
    }

    public void setPlanCanQuit(int planCanQuit) {
        this.planCanQuit = planCanQuit;
    }

    public String getProgressRate() {
        return progressRate;
    }

    /**
     * @param loanAmount 借款金额
     * @param surplusAmount 剩余可投金额
     */
    public void setProgressRate(BigDecimal loanAmount, BigDecimal surplusAmount) {
        BigDecimal progressRate = ((loanAmount.subtract(surplusAmount))
                .divide(loanAmount, 2, BigDecimal.ROUND_FLOOR)
                .multiply(InterfaceConst.BIGDECIMAL_HUNDRED))
                .setScale(0, BigDecimal.ROUND_HALF_UP);
        if (surplusAmount.compareTo(InterfaceConst.BIGDECIMAL_HUNDRED) < 0) {
            progressRate = InterfaceConst.BIGDECIMAL_HUNDRED;
        }
        this.progressRate = progressRate.toString() + "%";
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

    public String getBidYield() {
        return bidYield;
    }

    public void setBidYield(String bidYield) {
        this.bidYield = bidYield;
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

    public List<String> getBidLabel() {
        return bidLabel;
    }

    public void setBidLabel(List<String> bidLabel) {
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

    public int getPurchasedStatus() {
        return purchasedStatus;
    }

    public void setPurchasedStatus(int purchasedStatus) {
        this.purchasedStatus = purchasedStatus;
    }

    public void setProgressRate(String progressRate) {
        this.progressRate = progressRate;
    }

    public String getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(String voteAmount) {
        this.voteAmount = voteAmount;
    }

    public int getIsCG() {
        return isCG;
    }

    public void setIsCG(int isCG) {
        this.isCG = isCG;
    }

    public List<String> getMarkArray() {
        return markArray;
    }

    public void setMarkArray(List<String> markArray) {
        this.markArray = markArray;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    public String getHighRate() {
        return highRate;
    }

    public void setHighRate(String highRate) {
        this.highRate = highRate;
    }

    public String getLowRate() {
        return lowRate;
    }

    public void setLowRate(String lowRate) {
        this.lowRate = lowRate;
    }

    public String getBonusRate() {
        return bonusRate;
    }

    public void setBonusRate(String bonusRate) {
        this.bonusRate = bonusRate;
    }

    public Long getPanicBuyingTime() {
        return panicBuyingTime;
    }

    public void setPanicBuyingTime(Long panicBuyingTime) {
        this.panicBuyingTime = panicBuyingTime;
    }

    public Long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Integer getCountdown() {
        return countdown;
    }

    public void setCountdown(Integer countdown) {
        this.countdown = countdown;
    }

    public List<Map<String, Object>> getPlanInfo() {
        return planInfo;
    }

    public void setPlanInfo(List<Map<String, Object>> planInfo) {
        this.planInfo = planInfo;
    }
}
