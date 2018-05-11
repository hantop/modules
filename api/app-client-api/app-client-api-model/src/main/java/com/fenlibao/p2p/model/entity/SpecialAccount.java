package com.fenlibao.p2p.model.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 超级账户信息
 * Created by Administrator on 2018/1/10.
 */
public class SpecialAccount {
    private Integer userId;//用户id

    private String phoneNum;//手机号码

    private BigDecimal balance;//新网往来账户余额

    private Date checkTime;//检查时间

    private Integer smsCount;//当天短信发送次数

    private String name;//姓名

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(Integer smsCount) {
        this.smsCount = smsCount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
