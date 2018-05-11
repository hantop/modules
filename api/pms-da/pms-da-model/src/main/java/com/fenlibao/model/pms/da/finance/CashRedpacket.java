package com.fenlibao.model.pms.da.finance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 现金红包
 * Created by Bogle on 2016/1/13.
 */
public class CashRedpacket {

    private String sendTime;
    private String phoneNum;
    private BigDecimal money;

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
