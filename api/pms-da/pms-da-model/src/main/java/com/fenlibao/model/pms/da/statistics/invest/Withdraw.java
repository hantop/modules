package com.fenlibao.model.pms.da.statistics.invest;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Bogle on 2016/3/11.
 */
public class Withdraw implements Serializable {
    private static final long serialVersionUID = 1175497061365405839L;
    /*手机号*/
    private String phoneNum;
    /*真实姓名*/
    private String realName;
    /*提现时间*/
    private String createTime;
    /*提现金额*/
    private BigDecimal withdrawMoney;
    /*提现总金额*/
    private BigDecimal withdrawTotalMoney;
    /*累计投资金额*/
    private BigDecimal investTotalMoney;
    /*投资次数*/
    private Integer investCount;
    /*再投金额*/
    private BigDecimal investing;
    /*账户余额*/
    private BigDecimal surplusMoney;
    /**
     * 提现人数
     */
    private Integer totalWithdrawr;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(BigDecimal withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    public BigDecimal getInvestTotalMoney() {
        return investTotalMoney;
    }

    public void setInvestTotalMoney(BigDecimal investTotalMoney) {
        this.investTotalMoney = investTotalMoney;
    }

    public Integer getInvestCount() {
        return investCount;
    }

    public void setInvestCount(Integer investCount) {
        this.investCount = investCount;
    }

    public BigDecimal getInvesting() {
        return investing;
    }

    public void setInvesting(BigDecimal investing) {
        this.investing = investing;
    }

    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }

    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }

    public Integer getTotalWithdrawr() {
        return totalWithdrawr;
    }

    public void setTotalWithdrawr(Integer totalWithdrawr) {
        this.totalWithdrawr = totalWithdrawr;
    }

    public BigDecimal getWithdrawTotalMoney() {
        return withdrawTotalMoney;
    }

    public void setWithdrawTotalMoney(BigDecimal withdrawTotalMoney) {
        this.withdrawTotalMoney = withdrawTotalMoney;
    }
}
