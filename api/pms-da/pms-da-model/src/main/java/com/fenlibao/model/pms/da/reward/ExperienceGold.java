package com.fenlibao.model.pms.da.reward;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class ExperienceGold {
    private int id;

    private String activityName;

    private Date timeStart;

    private Date timeEnd;

    @NotNull(message = "年化收益不能为空")
    @Min(value = 0, message = "年化收益不能为负数")
    @Max(value = 6, message = "年化收益不能超过6%")
    private BigDecimal yearYield;

    @NotNull(message = "体验金额不能为空")
    @Min(value = 0, message = "体验金额不能为负数")
    @Max(value = 500000, message = "体验金额不能超过500000元")
    private BigDecimal experienceGold;

    private Byte experienceType;

    private Integer effectMonth;

    private String remarks;

    @NotNull(message = "体验金有效期不能为空")
    @Min(value = 1, message = "体验金有效期天数不能小于1天")
    @Max(value = 15, message = "体验金有效期天数不能超过15天")
    private Integer effectDay;

    @NotEmpty(message = "体验金代码不能为空")
    @NotNull(message = "体验金代码不能为空")
    @Length(min = 0, max = 50, message = "体验金代码字符长度在 {min} 到 {max} 之间")
    private String activityCode;

    private BigDecimal investMoney;

    private Integer grantStatus;//发放状态

    @Length(max = 25, message = "来源{max}个中文字内")
    private String expGoldComment ; //来源

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

    public String getExpGoldComment() {
        return expGoldComment;
    }

    public void setExpGoldComment(String expGoldComment) {
        this.expGoldComment = expGoldComment;
    }
}