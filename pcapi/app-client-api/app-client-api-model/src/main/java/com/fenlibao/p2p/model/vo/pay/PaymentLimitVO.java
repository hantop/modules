package com.fenlibao.p2p.model.vo.pay;

import java.math.BigDecimal;

/**
 * 支付限额
 * Created by zcai on 2016/6/15.
 */
public class PaymentLimitVO {

    private String bankCode;
    private String bankName;
    private BigDecimal singleLimit; //单笔限额
    private BigDecimal dailyLimit; //单日限额
    private BigDecimal monthlyLimit; //单月限额
    private BigDecimal minimum; //最低充值限额
//    private String remark; //备注
    private Integer channelCode;//支付通道编码

    private String xwBankCode;


    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(BigDecimal singleLimit) {
        this.singleLimit = singleLimit;
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public BigDecimal getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public Integer getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(Integer channelCode) {
        this.channelCode = channelCode;
    }

    public String getXwBankCode() {
        return xwBankCode;
    }

    public void setXwBankCode(String xwBankCode) {
        this.xwBankCode = xwBankCode;
    }
}
