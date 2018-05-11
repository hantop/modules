package com.fenlibao.p2p.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 用户体验金
 * Created by Louis Wang on 2015/10/17.
 */

public class ExperienceGoldInfo implements Serializable {

    private Integer id; //自增id
    private Integer expId;   //体验金Id
    private Integer userId; //用户Id
    private String experienceType;    //体验金活动类型
    private BigDecimal  yearYield = BigDecimal.ZERO;   //体验金年华收益 是百分比的
    private BigDecimal experienceGold = BigDecimal.ZERO;  //体验金金额
    private Integer effectDay;  // 红包有效天数
    private String status;  //红包状态
    private Timestamp startTime;  //开始时间
    private Timestamp endTime;    //结束时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExpId() {
        return expId;
    }

    public void setExpId(Integer expId) {
        this.expId = expId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getExperienceType() {
        return experienceType;
    }

    public void setExperienceType(String experienceType) {
        this.experienceType = experienceType;
    }

    public BigDecimal getYearYield() {
        return yearYield;
    }

    public void setYearYield(BigDecimal yearYield) {
        this.yearYield = yearYield;
    }

    public BigDecimal getExperienceGold() {
        return experienceGold;
    }

    public void setExperienceGold(BigDecimal experienceGold) {
        this.experienceGold = experienceGold;
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
