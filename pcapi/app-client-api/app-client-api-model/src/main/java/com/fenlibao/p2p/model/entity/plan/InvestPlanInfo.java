package com.fenlibao.p2p.model.entity.plan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投资计划 flb.t_invest_plan
 * by:kris 20170325
 */

public class InvestPlanInfo {
    public static BigDecimal FEED_RATE =BigDecimal.valueOf(0.01) ;// 省心计划提前退出费率

    protected Integer planId;// 投资计划id

    protected String planName;//投资计划名称

    protected Integer planType;// 投资类型(1:月月升计划 2:省心计划)

    protected Integer planStatus;// 投资计划状态

    protected Date bearrateTime;//起息时间

    protected BigDecimal totalRate;//总年化率

    protected String cycleType;//enum('d','m') 借款周期类型（按天/按月）

    protected Integer cycle;//借款周期

    protected Date expireTime;//到期时间
    
    protected BigDecimal overdueRate; //逾期费率
    
    protected BigDecimal investRate;//计划利率
    
    protected String repayMode; //还款方式
    
    protected BigDecimal raiseRate;//计划加息利率
    
    protected BigDecimal minYearlyRate;
    
	protected BigDecimal maxYearlyRate;
    
    public BigDecimal getMinYearlyRate() {
		return minYearlyRate;
	}

	public void setMinYearlyRate(BigDecimal minYearlyRate) {
		this.minYearlyRate = minYearlyRate;
	}

	public BigDecimal getMaxYearlyRate() {
		return maxYearlyRate;
	}

	public void setMaxYearlyRate(BigDecimal maxYearlyRate) {
		this.maxYearlyRate = maxYearlyRate;
	}

    
    public BigDecimal getRaiseRate() {
		return raiseRate;
	}

	public void setRaiseRate(BigDecimal raiseRate) {
		this.raiseRate = raiseRate;
	}

	public String getRepayMode() {
		return repayMode;
	}

	public void setRepayMode(String repayMode) {
		this.repayMode = repayMode;
	}

	public BigDecimal getInvestRate() {
		return investRate;
	}

	public void setInvestRate(BigDecimal investRate) {
		this.investRate = investRate;
	}

    public BigDecimal getOverdueRate() {
		return overdueRate;
	}

	public void setOverdueRate(BigDecimal overdueRate) {
		this.overdueRate = overdueRate;
	}

	public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getPlanType() {
        return planType;
    }

    public void setPlanType(Integer planType) {
        this.planType = planType;
    }

    public Integer getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Date getBearrateTime() {
        return bearrateTime;
    }

    public void setBearrateTime(Date bearrateTime) {
        this.bearrateTime = bearrateTime;
    }

    public BigDecimal getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(BigDecimal totalRate) {
        this.totalRate = totalRate;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
