package com.fenlibao.p2p.model.vo.fiancing;

import java.math.BigDecimal;

/**
 * 投资信息VO
 * Created by laubrence on 2016/3/26.
 */
public class InvestInfoVO implements Comparable<InvestInfoVO> {
    int status;//列表状态
    /*标Id*/
    int bidId;
    /*标名称*/
    String bidTitle;
    /*债权id*/
    int creditId;
    /*购买时间*/
    long purchaseTime;
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
    /*投资状态(1:已投资2:收益中;3:转让中 4:已转出 5:已逾期 6: 已回款)*/

    String expectedProfitString;

    int investStatus;

    private int isNoviceBid;//否是新手标 (1:是新手标;0:普通开店宝标)
    
    Long nextRepaymentDate;//下次还款日期    2016-06-28 junda.feng
    Long applyTime;//债权转让申请时间
    Long successTime;//转让成功日
    Long actualRepaymentDate;//到期回款日

    double interestRise;//加息券加息利率
    double bidInterestRise;//加息标加息利率
    int anytimeQuit;//随时退出标：1是、0否


    int itemType;//0:标  1:计划

    /*计划Id*/
    int planId;
    /*计划名称*/
    String planTitle;

    /*j加息收益*/
    BigDecimal raiseProfit;

    /*月升计划的当前月利率*/
    double presentRate;

    /** 计划类型 */
    int planType;
    /* 3.2版本的计划 */
    int newPlan;
    /* 判断计划是否可以退出 */
    int planCanQuit;

    String repaymentMode;

    public double getInterestRise() {
        return interestRise;
    }

    public void setInterestRise(double interestRise) {
        this.interestRise = interestRise;
    }

    public Long getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Long successTime) {
		this.successTime = successTime;
	}

	public Long getActualRepaymentDate() {
		return actualRepaymentDate;
	}

	public void setActualRepaymentDate(Long actualRepaymentDate) {
		this.actualRepaymentDate = actualRepaymentDate;
	}

	public Long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Long applyTime) {
		this.applyTime = applyTime;
	}

	public Long getNextRepaymentDate() {
		return nextRepaymentDate;
	}

	public void setNextRepaymentDate(Long nextRepaymentDate) {
		this.nextRepaymentDate = nextRepaymentDate;
	}

	public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
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

    public int getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(int investStatus) {
        this.investStatus = investStatus;
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

    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public double getYearYield() {
        return yearYield;
    }

    public void setYearYield(double yearYield) {
        this.yearYield = yearYield;
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

    public double getBidInterestRise() {
        return bidInterestRise;
    }

    public void setBidInterestRise(double bidInterestRise) {
        this.bidInterestRise = bidInterestRise;
    }

    public int getAnytimeQuit() {
        return anytimeQuit;
    }

    public void setAnytimeQuit(int anytimeQuit) {
        this.anytimeQuit = anytimeQuit;
    }

    public BigDecimal getRaiseProfit() {
        return raiseProfit;
    }

    public void setRaiseProfit(BigDecimal raiseProfit) {
        this.raiseProfit = raiseProfit;
    }

    public String getExpectedProfitString() {
        return expectedProfitString;
    }

    public void setExpectedProfitString(String expectedProfitString) {
        this.expectedProfitString = expectedProfitString;
    }

    public double getPresentRate() {
        return presentRate;
    }

    public void setPresentRate(double presentRate) {
        this.presentRate = presentRate;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    public int getNewPlan() {
        return newPlan;
    }

    public void setNewPlan(int newPlan) {
        this.newPlan = newPlan;
    }

    public int getPlanCanQuit() {
        return planCanQuit;
    }

    public void setPlanCanQuit(int planCanQuit) {
        this.planCanQuit = planCanQuit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    @Override
    public int compareTo(InvestInfoVO o) {

       /* if(this.status == 2){//申请退出时间
            if (this.applyTime>o.getApplyTime()) {
                return -1;
            }
            if (this.applyTime<o.getApplyTime()) {
                return 1;
            }
        } */
        if (this.purchaseTime>o.getPurchaseTime()) {
            return -1;
        }
        if (this.purchaseTime<o.getPurchaseTime()) {
            return 1;
        }

        return 0;
    }
}
