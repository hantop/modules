package com.fenlibao.model.pms.da.finance.vo;

import java.math.BigDecimal;
import java.util.Date;

public class ReturnExperienceGoldVO {
    private int id;

    private String activityName;

    private Date timeStart;

    private Date timeEnd;

    private BigDecimal yearYield;

    private BigDecimal experienceGold;

    private Byte experienceType;

    private Integer effectMonth;

    private String remarks;

    private Integer effectDay;

    private String activityCode;

    private BigDecimal investMoney;

    private Integer grantStatus;//发放状态

    private  String usersCount; //人数

    private  String userspay; //金额

    private String earningsDate;  //收益日期

    private String telphone;    //手机号码

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
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

    public Byte getExperienceType() {
        return experienceType;
    }

    public void setExperienceType(Byte experienceType) {
        this.experienceType = experienceType;
    }

    public Integer getEffectMonth() {
        return effectMonth;
    }

    public void setEffectMonth(Integer effectMonth) {
        this.effectMonth = effectMonth;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode == null ? null : activityCode.trim();
    }

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }

    public Integer getGrantStatus() {
        return grantStatus;
    }

    public void setGrantStatus(Integer grantStatus) {
        this.grantStatus = grantStatus;
    }

    public String getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(String usersCount) {
        this.usersCount = usersCount;
    }

    public String getUserspay() {
        return userspay;
    }

    public void setUserspay(String userspay) {
        this.userspay = userspay;
    }

    public String getEarningsDate() {
        return earningsDate;
    }

    public void setEarningsDate(String earningsDate) {
        this.earningsDate = earningsDate;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
}