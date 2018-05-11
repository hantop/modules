package com.fenlibao.p2p.model.entity.finacing;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资信息实体
 * Created by laubrence on 2016/3/26.
 */
public class InvestInfo implements Comparable<InvestInfo> {

    /*标Id*/
    int bidId;

    /*标名称*/
    String bidTitle;

    /*债权id*/
    int creditId;

    /*购买时间*/
    Date purchaseTime;

    /*到期日期*/
    Date expireDate;

    /*投资金额*/
    BigDecimal investAmount;

    /*年化收益率*/
    double yearYield;

    /*借款周期（单位：月）*/
    int loanMonths;

    /*借款周期（单位：天）*/
    int loanDays;

    /*预期收益*/
    BigDecimal expectedProfit;

    /*预期收益*/
    BigDecimal rasiedExpectedProfit;

    /*是否正在转让*/
    String isTransfer;

    /*是否逾期*/
    String isYq;

    /*债权转让订单id*/
    int transferOrderId;

    /*标状态*/
    String bidStatus;

    /*还款方式*/
    String repaymentMode;

    /*计划1、标0*/
    int itemType;
    /*加息标加息利率*/
    BigDecimal bidInterestRise;
    /*随时退出标：1是、0否*/
    int anytimeQuit;

    String isNoviceBid;//是否是新手标 (S:是新手标;F:普通标)

    BigDecimal totalRepaymentAmount; //总还款金额

    int zrId; //债权转让订单id

    boolean jxFlag; //是否已加息

    int isDepository;//是否是存管标 1：普通标  2：存管标


    /*计划Id*/
    int planId;

    /*计划名称*/
    String planTitle;

    int newPlan;

    /*投资金额*/
    BigDecimal surplusAmount;

    public boolean getJxFlag() {
        return jxFlag;
    }

    public void setJxFlag(boolean jxFlag) {
        this.jxFlag = jxFlag;
    }

    public BigDecimal getTotalRepaymentAmount() {
        return totalRepaymentAmount;
    }

    public void setTotalRepaymentAmount(BigDecimal totalRepaymentAmount) {
        this.totalRepaymentAmount = totalRepaymentAmount;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getBidTitle() {
        return bidTitle;
    }

    public void setBidTitle(String bidTitle) {
        this.bidTitle = bidTitle;
    }

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    public BigDecimal getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(BigDecimal expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public String getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(String isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getIsYq() {
        return isYq;
    }

    public void setIsYq(String isYq) {
        this.isYq = isYq;
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

    public Date getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Date purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public int getTransferOrderId() {
        return transferOrderId;
    }

    public void setTransferOrderId(int transferOrderId) {
        this.transferOrderId = transferOrderId;
    }

    public double getYearYield() {
        return yearYield;
    }

    public void setYearYield(double yearYield) {
        this.yearYield = yearYield;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    public String getIsNoviceBid() {
        return isNoviceBid;
    }

    public void setIsNoviceBid(String isNoviceBid) {
        this.isNoviceBid = isNoviceBid;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public int getZrId() {
        return zrId;
    }

    public void setZrId(int zrId) {
        this.zrId = zrId;
    }

    public int getIsDepository() {
        return isDepository;
    }

    public void setIsDepository(int isDepository) {
        this.isDepository = isDepository;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getBidInterestRise() {
        return bidInterestRise;
    }

    public void setBidInterestRise(BigDecimal bidInterestRise) {
        this.bidInterestRise = bidInterestRise;
    }

    public int getAnytimeQuit() {
        return anytimeQuit;
    }

    public void setAnytimeQuit(int anytimeQuit) {
        this.anytimeQuit = anytimeQuit;
    }

    public BigDecimal getRasiedExpectedProfit() {
        return rasiedExpectedProfit;
    }

    public void setRasiedExpectedProfit(BigDecimal rasiedExpectedProfit) {
        this.rasiedExpectedProfit = rasiedExpectedProfit;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public int getNewPlan() {
        return newPlan;
    }

    public void setNewPlan(int newPlan) {
        this.newPlan = newPlan;
    }

    @Override
    public int compareTo(InvestInfo o) {
        //按购买时间排序
        if (this.purchaseTime.compareTo(o.getPurchaseTime()) > 0) {
            return -1;
        }
        if (this.purchaseTime.compareTo(o.getPurchaseTime()) < 0) {
            return 1;
        }

        return 0;
    }

    public BigDecimal getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(BigDecimal surplusAmount) {
        this.surplusAmount = surplusAmount;
    }
}
