package com.fenlibao.model.pms.da.reward.form;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateCouponEditForm {
    private int id;
    private Date timeStart;
    private Date timeEnd;
    private String couponCode;
    private Integer effectDay;
    private Integer maxInvestMoney;
    private Integer minInvestMoney;
    private Integer minInvestDay;
    private Integer maxInvestDay;
    private BigDecimal scope;
    //==================增加标的类型限制,投资期限,以及来源(实际是备注)add Lee==============
    private List<Integer> bidTypeIds = new ArrayList<Integer>(); //对象映射,来自对应的标的限制,一对多(不存入这个实体类中对应的数据库表)

    private boolean investDeadLineType;//默认为false,此时表示投资期限不限.反之,按天来计算

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public Integer getMaxInvestMoney() {
        return maxInvestMoney;
    }

    public void setMaxInvestMoney(Integer maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public Integer getMinInvestMoney() {
        return minInvestMoney;
    }

    public void setMinInvestMoney(Integer minInvestMoney) {
        this.minInvestMoney = minInvestMoney;
    }

    public Integer getMinInvestDay() {
        return minInvestDay;
    }

    public void setMinInvestDay(Integer minInvestDay) {
        this.minInvestDay = minInvestDay;
    }

    public Integer getMaxInvestDay() {
        return maxInvestDay;
    }

    public void setMaxInvestDay(Integer maxInvestDay) {
        this.maxInvestDay = maxInvestDay;
    }

    public BigDecimal getScope() {
        return scope;
    }

    public void setScope(BigDecimal scope) {
        this.scope = scope;
    }

    public List<Integer> getBidTypeIds() {
        return bidTypeIds;
    }

    public void setBidTypeIds(List<Integer> bidTypeIds) {
        this.bidTypeIds = bidTypeIds;
    }

    public boolean isInvestDeadLineType() {
        return investDeadLineType;
    }

    public void setInvestDeadLineType(boolean investDeadLineType) {
        this.investDeadLineType = investDeadLineType;
    }
}