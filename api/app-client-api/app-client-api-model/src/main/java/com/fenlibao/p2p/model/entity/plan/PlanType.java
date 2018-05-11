package com.fenlibao.p2p.model.entity.plan;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author zeronx on 2017/11/16 17:45.
 * @version 1.0
 */
public class PlanType implements Serializable {
    /**
     * 类型id
     */
    private Integer id;
    /**
     * 计划标题
     */
    private String title;
    /**
     * 发标利率
     */
    private BigDecimal rate;
    /**
     * 加息利率
     */
    private BigDecimal raisedRate;
    /**
     * 计划借款总金额
     */
    private BigDecimal amount;
    /**
     * 借款周期
     */
    private Integer cycle;
    /**
     * 借款周期类型（按天/按月）
     */
    private String cycleType;
    /**
     * 还款方式（DEBX:等额本息;MYFX:每月付息到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;）
     */
    private String repaymentType;
    /**
     * 分期期数
     */
    private Integer stageNum;
    /**
     * 计划描述
     */
    private String description;
    /**
     * 是否新手标
     */
    private String isNoviceBid;
    /**
     * 优先级（在同时筹满的相应标的情况下，优先级高的先发）
     */
    private Integer priority;
    /**
     * 计划模板创建时间
     */
    private Timestamp createTime;
    /**
     * 计划模板更新时间
     */
    private Timestamp updateTime;
    /**
     * 状态（该模板是否启用，启用后自动发标处会自动筹标）
     */
    private String status;
    /**
     * 如果超过改时间（分钟）未能筹满，并且超过最小金额，则按筹集的金额发出计划
     */
    private Integer waitTime;
    /**
     * 最小发布计划金额
     */
    private BigDecimal minAmount;
    /**
     * 是否存管计划 1:普通，2：存管
     */
    private Integer cgMode;

    private BigDecimal totalUserAssets; // 用户资产总额
    private BigDecimal userInvestingAmount; // 用户在投金额
    private BigDecimal userAccumulatedIncome; // 用户累计收益
    private Integer targetUser; // 指定用户(1:指定 0:不指定)
    private String label; // 标签
    private String comment; // 条件说明

    private BigDecimal rateManageRatio; // 利息管理费比例

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRaisedRate() {
        return raisedRate;
    }

    public void setRaisedRate(BigDecimal raisedRate) {
        this.raisedRate = raisedRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public Integer getStageNum() {
        return stageNum;
    }

    public void setStageNum(Integer stageNum) {
        this.stageNum = stageNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsNoviceBid() {
        return isNoviceBid;
    }

    public void setIsNoviceBid(String isNoviceBid) {
        this.isNoviceBid = isNoviceBid;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getCgMode() {
        return cgMode;
    }

    public void setCgMode(Integer cgMode) {
        this.cgMode = cgMode;
    }

    public BigDecimal getTotalUserAssets() {
        return totalUserAssets;
    }

    public void setTotalUserAssets(BigDecimal totalUserAssets) {
        this.totalUserAssets = totalUserAssets;
    }

    public BigDecimal getUserInvestingAmount() {
        return userInvestingAmount;
    }

    public void setUserInvestingAmount(BigDecimal userInvestingAmount) {
        this.userInvestingAmount = userInvestingAmount;
    }

    public BigDecimal getUserAccumulatedIncome() {
        return userAccumulatedIncome;
    }

    public void setUserAccumulatedIncome(BigDecimal userAccumulatedIncome) {
        this.userAccumulatedIncome = userAccumulatedIncome;
    }

    public Integer getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(Integer targetUser) {
        this.targetUser = targetUser;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getRateManageRatio() {
        return rateManageRatio;
    }

    public void setRateManageRatio(BigDecimal rateManageRatio) {
        this.rateManageRatio = rateManageRatio;
    }
}
