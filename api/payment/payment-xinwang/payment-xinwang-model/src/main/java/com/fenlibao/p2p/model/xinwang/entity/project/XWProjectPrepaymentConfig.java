package com.fenlibao.p2p.model.xinwang.entity.project;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 标的提前还款时设置的违约金配置
 * Created by Administrator on 2018/1/5.
 */
public class XWProjectPrepaymentConfig {

    /**
     * 标id
     */
    private int bidId;
    /**
     * 是否收取违约金，1：是，0：否
     */
    private int penaltyFlag;
    /**
     * '违约金金额的收取方式，0：默认方式，1：自行设置，2：此时penalty_flag应为0，不收取违约金'
     */
    private int penaltyType;
    /**
     * 自行设置的违约金金额
     */
    private BigDecimal penaltyAmount;
    /**
     * 违约金分成1%，则存储0.01
     */
    private BigDecimal penaltyDivideRate;
    /**
     * 原本应收的违约金金额
     */
    private BigDecimal originalPenalty;
    /**
     * 创建时间
     */
    private Date createTime;

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getPenaltyFlag() {
        return penaltyFlag;
    }

    public void setPenaltyFlag(int penaltyFlag) {
        this.penaltyFlag = penaltyFlag;
    }

    public int getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(int penaltyType) {
        this.penaltyType = penaltyType;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getPenaltyDivideRate() {
        return penaltyDivideRate;
    }

    public void setPenaltyDivideRate(BigDecimal penaltyDivideRate) {
        this.penaltyDivideRate = penaltyDivideRate;
    }

    public BigDecimal getOriginalPenalty() {
        return originalPenalty;
    }

    public void setOriginalPenalty(BigDecimal originalPenalty) {
        this.originalPenalty = originalPenalty;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
