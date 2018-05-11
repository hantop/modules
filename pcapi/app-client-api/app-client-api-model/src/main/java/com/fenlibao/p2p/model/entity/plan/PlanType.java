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
    public Integer id;
    /**
     * 计划标题
     */
    public String title;
    /**
     * 发标利率
     */
    public BigDecimal rate;
    /**
     * 加息利率
     */
    public BigDecimal raisedRate;
    /**
     * 计划借款总金额
     */
    public BigDecimal amount;
    /**
     * 借款周期
     */
    public Integer cycle;
    /**
     * 借款周期类型（按天/按月）
     */
    public String cycleType;
    /**
     * 还款方式（DEBX:等额本息;MYFX:每月付息到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;）
     */
    public String repaymentType;
    /**
     * 分期期数
     */
    public Integer stageNum;
    /**
     * 计划描述
     */
    public String description;
    /**
     * 是否新手标
     */
    public String isNoviceBid;
    /**
     * 优先级（在同时筹满的相应标的情况下，优先级高的先发）
     */
    public Integer priority;
    /**
     * 计划模板创建时间
     */
    public Timestamp createTime;
    /**
     * 计划模板更新时间
     */
    public Timestamp updateTime;
    /**
     * 状态（该模板是否启用，启用后自动发标处会自动筹标）
     */
    public String status;
    /**
     * 如果超过改时间（分钟）未能筹满，并且超过最小金额，则按筹集的金额发出计划
     */
    public Integer waitTime;
    /**
     * 最小发布计划金额
     */
    public BigDecimal minAmount;
    /**
     * 是否存管计划 1:普通，2：存管
     */
    public Integer cgMode;

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
}
