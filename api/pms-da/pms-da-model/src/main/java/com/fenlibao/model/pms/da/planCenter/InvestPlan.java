package com.fenlibao.model.pms.da.planCenter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/20.
 */
public class InvestPlan {
    private int id;
    private int type;//类型(1:月月升计划 2:省心计划)
    private String name;// 计划名称
    private String number;// 计划名称
    private boolean novice;//是否新手(1:是 0:否)
    private BigDecimal amount;// 计划金额
    private int cycle;// 借款周期
    private String cycleType;// 借款周期类型（按天'd'/按月'm'）
    private BigDecimal investRate;// 发标利率
    private String investRateStr;// 发标利率
    private BigDecimal moIncreaseRate;// 月增幅加息
    private String moIncreaseRateStr;// 月增幅加息
    private BigDecimal raiseRate;// 计划加息利率
    private String raiseRateStr;// 计划加息利率
    private Date releaseTime;// 发布时间
    private Date displayTime;// 发布时间
    private Date auditTime;// 审核时间
    private Date fullTime;// 筹满时间
    private Date settleTime;// 结清时间
    private Date bearrateDate;// 起息时间
    private Date expireTime;// 到期时间
    private BigDecimal overdueRate;// 逾期费率
    private String status;// 状态
    private BigDecimal minYearlyRate;// 最低年化利率
    private String minYearlyRateStr;// 最低年化利率
    private BigDecimal maxYearlyRate;// 最高年化利率
    private String maxYearlyRateStr;// 最高年化利率
    private String repayMode;//还款方式
    private int fundRaisingPeriod;// 筹款期
    private int canQuit;// 随时退出(1:是 0:否)
    private Date sticktopTime;// 置顶时间
    private Date recommendTime;// 推荐置顶时间
    private Date createTime;// 创建时间
    private Date updateTime;// 修改时间
    private String description;//计划描述
    private int isCG;//系统类型(1:普通计划 2:存管计划)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isNovice() {
        return novice;
    }

    public void setNovice(boolean novice) {
        this.novice = novice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getInvestRateStr() {
        return investRateStr;
    }

    public void setInvestRateStr(String investRateStr) {
        this.investRateStr = investRateStr;
    }

    public BigDecimal getMoIncreaseRate() {
        return moIncreaseRate;
    }

    public void setMoIncreaseRate(BigDecimal moIncreaseRate) {
        this.moIncreaseRate = moIncreaseRate;
    }

    public String getMoIncreaseRateStr() {
        return moIncreaseRateStr;
    }

    public void setMoIncreaseRateStr(String moIncreaseRateStr) {
        this.moIncreaseRateStr = moIncreaseRateStr;
    }

    public BigDecimal getRaiseRate() {
        return raiseRate;
    }

    public void setRaiseRate(BigDecimal raiseRate) {
        this.raiseRate = raiseRate;
    }

    public String getRaiseRateStr() {
        return raiseRateStr;
    }

    public void setRaiseRateStr(String raiseRateStr) {
        this.raiseRateStr = raiseRateStr;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Date getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(Date displayTime) {
        this.displayTime = displayTime;
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

    public Date getBearrateDate() {
        return bearrateDate;
    }

    public void setBearrateDate(Date bearrateDate) {
        this.bearrateDate = bearrateDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMinYearlyRate() {
        return minYearlyRate;
    }

    public void setMinYearlyRate(BigDecimal minYearlyRate) {
        this.minYearlyRate = minYearlyRate;
    }

    public String getMinYearlyRateStr() {
        return minYearlyRateStr;
    }

    public void setMinYearlyRateStr(String minYearlyRateStr) {
        this.minYearlyRateStr = minYearlyRateStr;
    }

    public BigDecimal getMaxYearlyRate() {
        return maxYearlyRate;
    }

    public void setMaxYearlyRate(BigDecimal maxYearlyRate) {
        this.maxYearlyRate = maxYearlyRate;
    }

    public String getMaxYearlyRateStr() {
        return maxYearlyRateStr;
    }

    public void setMaxYearlyRateStr(String maxYearlyRateStr) {
        this.maxYearlyRateStr = maxYearlyRateStr;
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

    public Date getSticktopTime() {
        return sticktopTime;
    }

    public void setSticktopTime(Date sticktopTime) {
        this.sticktopTime = sticktopTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsCG() {
        return isCG;
    }

    public void setIsCG(int isCG) {
        this.isCG = isCG;
    }

    public Date getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(Date recommendTime) {
        this.recommendTime = recommendTime;
    }
}
