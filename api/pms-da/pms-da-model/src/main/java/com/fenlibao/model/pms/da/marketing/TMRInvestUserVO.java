package com.fenlibao.model.pms.da.marketing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 用户投资VO
 * Created by Louis Wang on 2016/3/11.
 */
public class TMRInvestUserVO implements Serializable {

    private Integer id;     //主键id

    private Integer tmrId;  // 导入信息id

    private String investUser;  //投资用户

    private Timestamp investTime;   //投资日期

    private String investPhone; //手机号

    private String investBid;   //投资的借款标题

    private BigDecimal investMoney = new BigDecimal(0); //投资金额

    private BigDecimal activateRedBag = new BigDecimal(0);  //激活红包金额

    private String investDate;  //借款期限

    private String investTimeStr;   //字符类型投资日期

    private Integer bid;     //标id

    private Integer userId;     //用户id

    private String investDays;  //借款天数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTmrId() {
        return tmrId;
    }

    public void setTmrId(Integer tmrId) {
        this.tmrId = tmrId;
    }

    public String getInvestUser() {
        return investUser;
    }

    public void setInvestUser(String investUser) {
        this.investUser = investUser;
    }

    public Timestamp getInvestTime() {
        return investTime;
    }

    public void setInvestTime(Timestamp investTime) {
        this.investTime = investTime;
    }

    public String getInvestPhone() {
        return investPhone;
    }

    public void setInvestPhone(String investPhone) {
        this.investPhone = investPhone;
    }

    public String getInvestBid() {
        return investBid;
    }

    public void setInvestBid(String investBid) {
        this.investBid = investBid;
    }

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }

    public BigDecimal getActivateRedBag() {
        return activateRedBag;
    }

    public void setActivateRedBag(BigDecimal activateRedBag) {
        this.activateRedBag = activateRedBag;
    }

    public String getInvestDate() {
        return investDate;
    }

    public void setInvestDate(String investDate) {
        this.investDate = investDate;
    }

    public String getInvestTimeStr() {
        return investTimeStr;
    }

    public void setInvestTimeStr(String investTimeStr) {
        this.investTimeStr = investTimeStr;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getInvestDays() {
        return investDays;
    }

    public void setInvestDays(String investDays) {
        this.investDays = investDays;
    }
}
