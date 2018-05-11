package com.fenlibao.model.pms.da.statistics.invest.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Bogle on 2016/3/11.
 */
public class WithdrawForm {

    private Boolean def;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date withdrawStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date withdrawEndTime;

    private BigDecimal minWithdrawMoney;

    private BigDecimal maxWithdrawMoney;

    public Date getWithdrawStartTime() {
        return withdrawStartTime;
    }

    public void setWithdrawStartTime(Date withdrawStartTime) {
        this.withdrawStartTime = withdrawStartTime;
    }

    public Date getWithdrawEndTime() {
        return withdrawEndTime;
    }

    public void setWithdrawEndTime(Date withdrawEndTime) {
        this.withdrawEndTime = withdrawEndTime;
    }

    public BigDecimal getMinWithdrawMoney() {
        return minWithdrawMoney;
    }

    public void setMinWithdrawMoney(BigDecimal minWithdrawMoney) {
        this.minWithdrawMoney = minWithdrawMoney;
    }

    public BigDecimal getMaxWithdrawMoney() {
        return maxWithdrawMoney;
    }

    public void setMaxWithdrawMoney(BigDecimal maxWithdrawMoney) {
        this.maxWithdrawMoney = maxWithdrawMoney;
    }

    public Boolean getDef() {
        return def;
    }

    public void setDef(Boolean def) {
        this.def = def;
    }
}
