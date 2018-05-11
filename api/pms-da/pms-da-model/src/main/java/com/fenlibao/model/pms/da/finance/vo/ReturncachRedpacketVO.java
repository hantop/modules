package com.fenlibao.model.pms.da.finance.vo;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/1/12.
 */
public class ReturncachRedpacketVO {
    private Integer redpacketId;// 返现券ID
    private String activityCode;// 返现券代码
    private String remarks;// 返现券来源(备注)
    private BigDecimal investMoney;// 返现券门槛
    private BigDecimal redMoney;// 返现券金额
    private Integer effectDay;// 返现券有效期
    private Integer redNumber;// 发送数量
    private Integer activeCount;// 激活数量
    private BigDecimal redMoneySum;// 产生成本

    public Integer getRedpacketId() {
        return redpacketId;
    }

    public void setRedpacketId(Integer redpacketId) {
        this.redpacketId = redpacketId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }

    public BigDecimal getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(BigDecimal redMoney) {
        this.redMoney = redMoney;
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getRedNumber() {
		return redNumber;
	}

	public void setRedNumber(Integer redNumber) {
		this.redNumber = redNumber;
	}

	public BigDecimal getRedMoneySum() {
        return redMoneySum;
    }

    public void setRedMoneySum(BigDecimal redMoneySum) {
        this.redMoneySum = redMoneySum;
    }
}
