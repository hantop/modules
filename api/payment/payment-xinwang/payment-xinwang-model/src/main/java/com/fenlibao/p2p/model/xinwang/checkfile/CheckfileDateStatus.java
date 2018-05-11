package com.fenlibao.p2p.model.xinwang.checkfile;

import java.util.Date;

/**
 * Created by Administrator on 2017/8/30.
 */
public class CheckfileDateStatus {
    private Date checkfileDate;
    private Integer status;
    private Integer backRollRechargeStatus;
    private Integer commissionStatus;
    private Integer rechargeStatus;
    private Integer transactionStatus;
    private Integer userStatus;
    private Integer withDrawStatus;

    public Date getCheckfileDate() {
        return checkfileDate;
    }

    public void setCheckfileDate(Date checkfileDate) {
        this.checkfileDate = checkfileDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBackRollRechargeStatus() {
        return backRollRechargeStatus;
    }

    public void setBackRollRechargeStatus(Integer backRollRechargeStatus) {
        this.backRollRechargeStatus = backRollRechargeStatus;
    }

    public Integer getCommissionStatus() {
        return commissionStatus;
    }

    public void setCommissionStatus(Integer commissionStatus) {
        this.commissionStatus = commissionStatus;
    }

    public Integer getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(Integer rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public Integer getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Integer transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getWithDrawStatus() {
        return withDrawStatus;
    }

    public void setWithDrawStatus(Integer withDrawStatus) {
        this.withDrawStatus = withDrawStatus;
    }
}
