package com.fenlibao.model.pms.da.cs.account;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 奖励记录
 * Created by chenzhixuan on 2016/1/4.
 */
public class ReawrdRecord {
    private int reawrdType;// 奖励类型
    private BigDecimal reawrdMoney;// 红包金额
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date reawrdTime;// 奖励时间

    private Integer effectDay;
    private BigDecimal experienceGold;
    private BigDecimal yearYield;

    public int getReawrdType() {
        return reawrdType;
    }

    public void setReawrdType(int reawrdType) {
        this.reawrdType = reawrdType;
    }

    public BigDecimal getReawrdMoney() {
        return reawrdMoney;
    }

    public void setReawrdMoney(BigDecimal reawrdMoney) {
        this.reawrdMoney = reawrdMoney;
    }

    public Date getReawrdTime() {
        return reawrdTime;
    }

    public void setReawrdTime(Date reawrdTime) {
        this.reawrdTime = reawrdTime;
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public BigDecimal getExperienceGold() {
        return experienceGold;
    }

    public void setExperienceGold(BigDecimal experienceGold) {
        this.experienceGold = experienceGold;
    }

    public BigDecimal getYearYield() {
        return yearYield;
    }

    public void setYearYield(BigDecimal yearYield) {
        this.yearYield = yearYield;
    }
}
