package com.fenlibao.model.pms.da.statistics.invest;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据管理表单内容
 * Created by Louis Wang on 2015/11/13.
 */

public class InvestForm {
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date investStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date investEndTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date regStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    private Date regEndTime;

    private BigDecimal minInvestMoney;

    private BigDecimal maxInvestMoney;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 标的类型
     */
    private String bidType;

    /**
     * 标的状态
     */
    private String status;

    /**
     * 是否首投
     */
    private String firstInvestStr;

    /**
     * 是否首投
     */
    private boolean isFirstInvest;

    /**
     * 渠道名称
     */
    private String channelName;

    public Date getInvestStartTime() {
        return investStartTime;
    }

    public void setInvestStartTime(Date investStartTime) {
        this.investStartTime = investStartTime;
    }

    public Date getInvestEndTime() {
        return investEndTime;
    }

    public void setInvestEndTime(Date investEndTime) {
        this.investEndTime = investEndTime;
    }

    public Date getRegStartTime() {
        return regStartTime;
    }

    public void setRegStartTime(Date regStartTime) {
        this.regStartTime = regStartTime;
    }

    public Date getRegEndTime() {
        return regEndTime;
    }

    public void setRegEndTime(Date regEndTime) {
        this.regEndTime = regEndTime;
    }

    public BigDecimal getMinInvestMoney() {
        return minInvestMoney;
    }

    public void setMinInvestMoney(BigDecimal minInvestMoney) {
        this.minInvestMoney = minInvestMoney;
    }

    public BigDecimal getMaxInvestMoney() {
        return maxInvestMoney;
    }

    public void setMaxInvestMoney(BigDecimal maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstInvestStr() {
        return firstInvestStr;
    }

    public void setFirstInvestStr(String firstInvestStr) {
        this.firstInvestStr = firstInvestStr;
    }

    public boolean isFirstInvest() {
        return isFirstInvest;
    }

    public void setFirstInvest(boolean firstInvest) {
        isFirstInvest = firstInvest;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
