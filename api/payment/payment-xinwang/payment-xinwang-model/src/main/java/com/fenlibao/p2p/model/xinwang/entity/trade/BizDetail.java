package com.fenlibao.p2p.model.xinwang.entity.trade;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/6/1.
 */
public class BizDetail {
    String  bizType;
    String freezeRequestNo;
    String sourcePlatformUserNo;
    String targetPlatformUserNo;
    BigDecimal amount;
    BigDecimal income;
    BigDecimal share;
    String customDefine;
    String remark;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getFreezeRequestNo() {
        return freezeRequestNo;
    }

    public void setFreezeRequestNo(String freezeRequestNo) {
        this.freezeRequestNo = freezeRequestNo;
    }

    public String getSourcePlatformUserNo() {
        return sourcePlatformUserNo;
    }

    public void setSourcePlatformUserNo(String sourcePlatformUserNo) {
        this.sourcePlatformUserNo = sourcePlatformUserNo;
    }

    public String getTargetPlatformUserNo() {
        return targetPlatformUserNo;
    }

    public void setTargetPlatformUserNo(String targetPlatformUserNo) {
        this.targetPlatformUserNo = targetPlatformUserNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getShare() {
        return share;
    }

    public void setShare(BigDecimal share) {
        this.share = share;
    }

    public String getCustomDefine() {
        return customDefine;
    }

    public void setCustomDefine(String customDefine) {
        this.customDefine = customDefine;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean validate() {
        return bizType != null && amount != null;
    }
}
