package com.fenlibao.model.pms.da.statistics.invest.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Bogle on 2016/3/11.
 */
public class FirstInvestForm {

    private Boolean def;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date investStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date investEndTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date regStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date regEndTime;

    private BigDecimal minInvestMoney;

    private BigDecimal maxInvestMoney;

    public Date getInvestStartTime() {
        return investStartTime;
    }

    public void setInvestStartTime(Date investStartTime) {
        this.investStartTime = investStartTime;
    }

    public Date getInvestEndTime() {
        return investEndTime;
    }

    public void setInvestEndTime(Date investEndTime) {
        this.investEndTime = investEndTime;
    }

    public Date getRegStartTime() {
        return regStartTime;
    }

    public void setRegStartTime(Date regStartTime) {
        this.regStartTime = regStartTime;
    }

    public Date getRegEndTime() {
        return regEndTime;
    }

    public void setRegEndTime(Date regEndTime) {
        this.regEndTime = regEndTime;
    }

    public BigDecimal getMinInvestMoney() {
        return minInvestMoney;
    }

    public void setMinInvestMoney(BigDecimal minInvestMoney) {
        this.minInvestMoney = minInvestMoney;
    }

    public BigDecimal getMaxInvestMoney() {
        return maxInvestMoney;
    }

    public void setMaxInvestMoney(BigDecimal maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public Boolean getDef() {
        return def;
    }

    public void setDef(Boolean def) {
        this.def = def;
    }
}
