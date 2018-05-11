package com.fenlibao.p2p.model.entity.finacing;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by junda.feng on 2016/6/16.
 */
public class RepaymentInfoExt {

    private int bidId; //标id

    private BigDecimal principal;//本金

    private BigDecimal gains;//收益

    private Date expectedRepaymentDate;//本期应还款日期

    private Date actualRepaymentDate;//实际还款日期

    private String creditCode;//债权标题

    private String bidTitle;//标标题

    private int creditOrderId;//债权转让订单Id

    private String state;//wh:未还 yh:以还

    private int number;//期号

    private int periods;//期数

    private String repayment;//还款方式

    private int itemType;//区分类型(0:标  1:计划)

    private int planRecordId;//回款计划记录ID

    public String getRepayment() {
        return repayment;
    }

    public void setRepayment(String repayment) {
        this.repayment = repayment;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getPlanRecordId() {
        return planRecordId;
    }

    public void setPlanRecordId(int planRecordId) {
        this.planRecordId = planRecordId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getGains() {
        return gains;
    }

    public void setGains(BigDecimal gains) {
        this.gains = gains;
    }

    public Date getExpectedRepaymentDate() {
        return expectedRepaymentDate;
    }

    public void setExpectedRepaymentDate(Date expectedRepaymentDate) {
        this.expectedRepaymentDate = expectedRepaymentDate;
    }

    public Date getActualRepaymentDate() {
        return actualRepaymentDate;
    }

    public void setActualRepaymentDate(Date actualRepaymentDate) {
        this.actualRepaymentDate = actualRepaymentDate;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getBidTitle() {
        return bidTitle;
    }

    public void setBidTitle(String bidTitle) {
        this.bidTitle = bidTitle;
    }

    public int getCreditOrderId() {
        return creditOrderId;
    }

    public void setCreditOrderId(int creditOrderId) {
        this.creditOrderId = creditOrderId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }


}
