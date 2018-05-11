package com.fenlibao.p2p.model.entity.bid;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 投资计划
 */

public class InvestPlan {
    private int id;// 投资计划id
    private String title;//计划名称

    private int type;//类型
    private int isNovice;//是否新手
    private String number;//编号
    private BigDecimal amount;//金额
    private BigDecimal surplusAmount;//剩余可投金额
    private int cycle;
    private String cycleType;
    private BigDecimal investRate;
    private BigDecimal moIncreaseRate;
    private BigDecimal minYearlyRate;
    private BigDecimal maxYearlyRate;
    private BigDecimal raiseRate;
    private Timestamp displayTime;
    private Timestamp releaseTime;
    private Timestamp auditTime;
    private Timestamp fullTime;
    private Timestamp settleTime;
    private Timestamp bearrateTime;
    private Timestamp expireTime;
    private BigDecimal overdueRate;
    private int status;
    private String repayMode;
    private int fundRaisingPeriod;
    private int canQuit;
    private Timestamp createTime;
    private Timestamp updateTime;

    //20170525新增存管标志
    private int isCG;//1：普通标，2：存管标

    public InvestPlan() {
    }

    public InvestPlan(int id, String title, int type, int isNovice, String number, BigDecimal amount, BigDecimal surplusAmount, int cycle, String cycleType, BigDecimal investRate, BigDecimal moIncreaseRate, BigDecimal minYearlyRate, BigDecimal maxYearlyRate, BigDecimal raiseRate, Timestamp displayTime, Timestamp releaseTime, Timestamp auditTime, Timestamp fullTime, Timestamp settleTime, Timestamp bearrateTime, Timestamp expireTime, BigDecimal overdueRate, int status, String repayMode, int fundRaisingPeriod, int canQuit, Timestamp createTime, Timestamp updateTime, int isCG) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.isNovice = isNovice;
        this.number = number;
        this.amount = amount;
        this.surplusAmount = surplusAmount;
        this.cycle = cycle;
        this.cycleType = cycleType;
        this.investRate = investRate;
        this.moIncreaseRate = moIncreaseRate;
        this.minYearlyRate = minYearlyRate;
        this.maxYearlyRate = maxYearlyRate;
        this.raiseRate = raiseRate;
        this.displayTime = displayTime;
        this.releaseTime = releaseTime;
        this.auditTime = auditTime;
        this.fullTime = fullTime;
        this.settleTime = settleTime;
        this.bearrateTime = bearrateTime;
        this.expireTime = expireTime;
        this.overdueRate = overdueRate;
        this.status = status;
        this.repayMode = repayMode;
        this.fundRaisingPeriod = fundRaisingPeriod;
        this.canQuit = canQuit;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.isCG = isCG;
    }

    public int getIsCG() {
        return isCG;
    }

    public void setIsCG(int isCG) {
        this.isCG = isCG;
    }

    public BigDecimal getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsNovice() {
        return isNovice;
    }

    public void setIsNovice(int isNovice) {
        this.isNovice = isNovice;
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

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
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

    public BigDecimal getMoIncreaseRate() {
        return moIncreaseRate;
    }

    public void setMoIncreaseRate(BigDecimal moIncreaseRate) {
        this.moIncreaseRate = moIncreaseRate;
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

    public Timestamp getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(Timestamp displayTime) {
        this.displayTime = displayTime;
    }

    public Timestamp getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Timestamp getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Timestamp auditTime) {
        this.auditTime = auditTime;
    }

    public Timestamp getFullTime() {
        return fullTime;
    }

    public void setFullTime(Timestamp fullTime) {
        this.fullTime = fullTime;
    }

    public Timestamp getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(Timestamp settleTime) {
        this.settleTime = settleTime;
    }

    public Timestamp getBearrateTime() {
        return bearrateTime;
    }

    public void setBearrateTime(Timestamp bearrateTime) {
        this.bearrateTime = bearrateTime;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRepayMode() {
        return repayMode;
    }

    public void setRepayMode(String repayMode) {
        this.repayMode = repayMode;
    }

    public int getFundRaisingPeriod() {
        return fundRaisingPeriod;
    }

    public void setFundRaisingPeriod(int fundRaisingPeriod) {
        this.fundRaisingPeriod = fundRaisingPeriod;
    }

    public int getCanQuit() {
        return canQuit;
    }

    public void setCanQuit(int canQuit) {
        this.canQuit = canQuit;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
