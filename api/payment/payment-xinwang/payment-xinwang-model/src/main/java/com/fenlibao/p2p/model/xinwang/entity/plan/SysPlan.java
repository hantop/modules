package com.fenlibao.p2p.model.xinwang.entity.plan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * flb.t_invest_plan
 */
public class SysPlan {
    private Integer id;
    /**
     * 类型(1:月月升计划 2:省心计划)
     */
    private Integer type;
    /**
     * 是否新手专享(1:是 0:否)
     */
    private Integer novice;
    /**
     * 名称
     */
    private String name;
    /**
     * 计划描述
     */
    private String description;
    /**
     * 编号
     */
    private String number;
    /**
     * 计划金额
     */
    private BigDecimal amount;
    /**
     * 剩余可投金额
     */
    private BigDecimal surplusAmount;
    /**
     * 借款周期
     */
    private Integer cycle;
    /**
     * 借款周期类型（按天/按月）
     */
    private String cycleType;
    /**
     * 投资利率
     */
    private BigDecimal investRate;
    /**
     * 月增幅加息
     */
    private BigDecimal monthIncreaseRate;
    /**
     * 最低年化利率
     */
    private BigDecimal minYearlyRate;
    /**
     * 最高年化利率
     */
    private BigDecimal maxYearlyRate;
    /**
     * 计划加息利率
     */
    private BigDecimal raiseRate;
    /**
     * 显示时间
     */
    private Date displayTime;
    /**
     * 发布时间
     */
    private Date releaseTime;
    /**
     * 审核时间
     */
    private Date auditTime;
    /**
     * 筹满时间
     */
    private Date fullTime;
    /**
     * 结清时间
     */
    private Date settleTime;
    /**
     * 起息日期
     */
    private Date bearrateTime;
    /**
     * 到期时间
     */
    private Date expireTime;
    /**
     * 逾期费率
     */
    private BigDecimal overdueRate;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 还款方式（DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;）
     */
    private String repayMode;
    /**
     * 筹款期
     */
    private Integer fundRaisingPeriod;
    /**
     * 随时退出(1:是 0:否)
     */
    private Integer canQuit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNovice() {
        return novice;
    }

    public void setNovice(Integer novice) {
        this.novice = novice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(BigDecimal surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public BigDecimal getInvestRate() {
        return investRate;
    }

    public void setInvestRate(BigDecimal investRate) {
        this.investRate = investRate;
    }

    public BigDecimal getMonthIncreaseRate() {
        return monthIncreaseRate;
    }

    public void setMonthIncreaseRate(BigDecimal monthIncreaseRate) {
        this.monthIncreaseRate = monthIncreaseRate;
    }

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

    public Date getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(Date displayTime) {
        this.displayTime = displayTime;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Date getFullTime() {
        return fullTime;
    }

    public void setFullTime(Date fullTime) {
        this.fullTime = fullTime;
    }

    public Date getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }

    public Date getBearrateTime() {
        return bearrateTime;
    }

    public void setBearrateTime(Date bearrateTime) {
        this.bearrateTime = bearrateTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public BigDecimal getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRepayMode() {
        return repayMode;
    }

    public void setRepayMode(String repayMode) {
        this.repayMode = repayMode;
    }

    public Integer getFundRaisingPeriod() {
        return fundRaisingPeriod;
    }

    public void setFundRaisingPeriod(Integer fundRaisingPeriod) {
        this.fundRaisingPeriod = fundRaisingPeriod;
    }

    public Integer getCanQuit() {
        return canQuit;
    }

    public void setCanQuit(Integer canQuit) {
        this.canQuit = canQuit;
    }
}
