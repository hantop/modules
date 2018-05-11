package com.fenlibao.model.pms.da.biz;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 计划
 * Created by chenzhixuan on 2017/2/7.
 */
public class Plan {
    private int id;
    private String title;// 计划名称
    private BigDecimal amount;// 计划借款总金额
    private int cycle;// 借款期限
    private String cycleType;// 借款周期类型（按天/按月）
    private BigDecimal rate;// 发标利率
    private String rateStr;// 发标利率
    private BigDecimal raisedRate;// 加息利率
    private String raisedRateStr;// 加息利率
    private int investPeopleNum;// 投资人数
    private int loanNum;// 标的个数
    private Date createTime;// 创建时间
    private Date tenderfullTime;// 投满时间
    private String status;// 状态

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getRateStr() {
        return rateStr;
    }

    public void setRateStr(String rateStr) {
        this.rateStr = rateStr;
    }

    public BigDecimal getRaisedRate() {
        return raisedRate;
    }

    public void setRaisedRate(BigDecimal raisedRate) {
        this.raisedRate = raisedRate;
    }

    public String getRaisedRateStr() {
        return raisedRateStr;
    }

    public void setRaisedRateStr(String raisedRateStr) {
        this.raisedRateStr = raisedRateStr;
    }

    public int getInvestPeopleNum() {
        return investPeopleNum;
    }

    public void setInvestPeopleNum(int investPeopleNum) {
        this.investPeopleNum = investPeopleNum;
    }

    public int getLoanNum() {
        return loanNum;
    }

    public void setLoanNum(int loanNum) {
        this.loanNum = loanNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTenderfullTime() {
        return tenderfullTime;
    }

    public void setTenderfullTime(Date tenderfullTime) {
        this.tenderfullTime = tenderfullTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}