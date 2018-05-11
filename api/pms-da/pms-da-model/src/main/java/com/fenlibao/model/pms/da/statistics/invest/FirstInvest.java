package com.fenlibao.model.pms.da.statistics.invest;

import java.math.BigDecimal;

/**
 * Created by Bogle on 2016/3/11.
 */
public class FirstInvest {

    /*手机号*/
    private String phoneNum;
    /*注册时间*/
    private String regtime;
    /*真实姓名*/
    private String realName;
    /*投资时间*/
    private String createTime;
    /*投资金额*/
    private BigDecimal money;
    /*首投期限*/
    private String limitTime;

    /**
     * 投资人数
     */
    private Integer totalInvestor;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getRegtime() {
        return regtime;
    }

    public void setRegtime(String regtime) {
        this.regtime = regtime;
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

    public Integer getTotalInvestor() {
        return totalInvestor;
    }

    public void setTotalInvestor(Integer totalInvestor) {
        this.totalInvestor = totalInvestor;
    }
}
