package com.fenlibao.model.pms.da.channel;

import java.math.BigDecimal;

/**
 * 渠道统计
 * Created by chenzhixuan on 2015/11/10.
 */
public class ChannelStatistics {

    private int channelId;// 渠道ID
    private String channelName;// 渠道名称
    private int registerCount;// 注册人数
    private int authCount;// 实名认证人数
    private int rechargeCount;// 充值人数
    private int investCount;// 投资人数
    private BigDecimal rechargeSum;// 充值金额
    private BigDecimal investSum;// 投资金额
    private int activeRedpacketCount;// 激活红包人数
    private BigDecimal activeRedpacketSum;// 激活红包金额

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(int registerCount) {
        this.registerCount = registerCount;
    }

    public int getAuthCount() {
        return authCount;
    }

    public void setAuthCount(int authCount) {
        this.authCount = authCount;
    }

    public int getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(int rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public BigDecimal getRechargeSum() {
        return rechargeSum;
    }

    public void setRechargeSum(BigDecimal rechargeSum) {
        this.rechargeSum = rechargeSum;
    }

    public BigDecimal getInvestSum() {
        return investSum;
    }

    public void setInvestSum(BigDecimal investSum) {
        this.investSum = investSum;
    }

    public int getActiveRedpacketCount() {
        return activeRedpacketCount;
    }

    public void setActiveRedpacketCount(int activeRedpacketCount) {
        this.activeRedpacketCount = activeRedpacketCount;
    }

    public BigDecimal getActiveRedpacketSum() {
        return activeRedpacketSum;
    }

    public void setActiveRedpacketSum(BigDecimal activeRedpacketSum) {
        this.activeRedpacketSum = activeRedpacketSum;
    }
}
