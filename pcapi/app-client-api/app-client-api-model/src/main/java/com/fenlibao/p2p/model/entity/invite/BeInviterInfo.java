package com.fenlibao.p2p.model.entity.invite;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 被邀请人信息
 * Created by chenzhixuan on 2016/7/26.
 */
public class BeInviterInfo {
    private String realname;// 姓名
    private String phonenum;// 手机号码
    private Date registerDate;// 注册日期
    private BigDecimal userInvestSum;// 用户累计投资金额

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public BigDecimal getUserInvestSum() {
        return userInvestSum;
    }

    public void setUserInvestSum(BigDecimal userInvestSum) {
        this.userInvestSum = userInvestSum;
    }
}