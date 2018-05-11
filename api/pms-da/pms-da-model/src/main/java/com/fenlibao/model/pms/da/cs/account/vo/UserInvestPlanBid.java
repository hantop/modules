package com.fenlibao.model.pms.da.cs.account.vo;

import java.math.BigDecimal;
import java.util.Date;

public class UserInvestPlanBid {

    private String bidType;// 标的类型

    private String bidName;// 标的名称

    private String matchMoney;// 匹配金额

    private BigDecimal rate;// 投资利率

    private int day;// 标的期限天

    private int month;// 标的期限月

    private String repayment;// 还款方式

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date matchTime;// 匹配时间

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date quitTime;// 退出时间

    private String status;// 状态

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getBidName() {
        return bidName;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public String getMatchMoney() {
        return matchMoney;
    }

    public void setMatchMoney(String matchMoney) {
        this.matchMoney = matchMoney;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getRepayment() {
        return repayment;
    }

    public void setRepayment(String repayment) {
        this.repayment = repayment;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public Date getQuitTime() {
        return quitTime;
    }

    public void setQuitTime(Date quitTime) {
        this.quitTime = quitTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
