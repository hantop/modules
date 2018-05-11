package com.fenlibao.p2p.model.vo.redpacket;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zcai on 2016/8/5.
 */
public class _RedPacketVO {

    private Integer id;
    private String activityName;//活动名称
    private Date activityStartTime;//活动结束时间
    private Date activityEndTime;//活动开始时间
    private BigDecimal amount;//红包金额
    private Integer type;//红包类型
    private Integer effectDays;//有效天数
    private String activityCode;//活动编码
    private BigDecimal investAmount;//起头金额
    private Integer investDeadline;//投资期限

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(Date activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public Date getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(Date activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getEffectDays() {
        return effectDays;
    }

    public void setEffectDays(Integer effectDays) {
        this.effectDays = effectDays;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public Integer getInvestDeadline() {
        return investDeadline;
    }

    public void setInvestDeadline(Integer investDeadline) {
        this.investDeadline = investDeadline;
    }
}
