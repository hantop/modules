package com.fenlibao.p2p.model.entity.finacing;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资信息实体
 * Created by laubrence on 2016/3/26.
 */
public class InvestInfo {

    /*标Id*/
    int bidId;

    /*标名称*/
    String bidTitle;

    /*债权id*/
    int creditId;

    /*购买时间*/
    Date purchaseTime;

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

    /*是否正在转让*/
    String isTransfer;

    /*债权转让订单id*/
    int transferOrderId;

    /*标状态*/
    String bidStatus;

    /*还款方式*/
    String repaymentMode;

    Date nextRepaymentDate;//下次还款日期    2016-06-28 junda.feng
    
    Date applyTime;//债权转让申请时间
    
    Date successTime;//转让成功日
    
    Date actualRepaymentDate;//到期回款日

    double interestRise;//加息券加息利率

    double bidInterestRise;//加息标加息利率

    int anytimeQuit;//随时退出标：1是、0否

    Date investTime;//投资时间

    Date interestTime;//计息时间

    int itemType;

    String isYq;//是否逾期

    /*加息收益*/
    BigDecimal raiseProfit;

    String isNoviceBid;//是否是新手标 (S:是新手标;F:普通标)

    /* 3.2版本的计划 */
    int newPlan;


    public int getAnytimeQuit() {
        return anytimeQuit;
    }

    public void setAnytimeQuit(int anytimeQuit) {
        this.anytimeQuit = anytimeQuit;
    }

    public double getBidInterestRise() {
        return bidInterestRise;
    }

    public void setBidInterestRise(double bidInterestRise) {
        this.bidInterestRise = bidInterestRise;
    }

    public double getInterestRise() {
        return interestRise;
    }

    public void setInterestRise(double interestRise) {
        this.interestRise = interestRise;
    }

    public Date getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	public Date getActualRepaymentDate() {
		return actualRepaymentDate;
	}

	public void setActualRepaymentDate(Date actualRepaymentDate) {
		this.actualRepaymentDate = actualRepaymentDate;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getNextRepaymentDate() {
		return nextRepaymentDate;
	}

	public void setNextRepaymentDate(Date nextRepaymentDate) {
		this.nextRepaymentDate = nextRepaymentDate;
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

    public Date getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Date investTime) {
        this.investTime = investTime;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getRaiseProfit() {
        return raiseProfit;
    }

    public void setRaiseProfit(BigDecimal raiseProfit) {
        this.raiseProfit = raiseProfit;
    }

    public int getNewPlan() {
        return newPlan;
    }

    public void setNewPlan(int newPlan) {
        this.newPlan = newPlan;
    }

    public Date getInterestTime() {
        return interestTime;
    }

    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }

    public String getIsYq() {
        return isYq;
    }

    public void setIsYq(String isYq) {
        this.isYq = isYq;
    }
}
