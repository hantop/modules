package com.fenlibao.model.pms.da.channel.vo;

import java.math.BigDecimal;

/**
 * 渠道统计
 *
 * Created by chenzhixuan on 2015/12/11.
 */
public class ChannelStatisticsVO implements Comparable<ChannelStatisticsVO> {
    private int id;// 渠道ID
    private String name;// 渠道名称
    private String code;// 渠道编号
    private int registerCount;// 注册人数
    private int authCount;// 实名认证人数
    private int rechargeCount;// 充值人数
    private BigDecimal rechargeSum;// 充值总额
    private int investCount;// 投资人数
    private BigDecimal investSum;// 投资总额
    private int activeRedpacketCount;// 激活红包人数
    private BigDecimal activeRedpacketSum;// 激活红包人数

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public BigDecimal getRechargeSum() {
        return rechargeSum;
    }

    public void setRechargeSum(BigDecimal rechargeSum) {
        this.rechargeSum = rechargeSum;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
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

    @Override
    public int compareTo(ChannelStatisticsVO o) {
        // return (x < y) ? -1 : ((x == y) ? 0 : 1);
        return (registerCount < o.getRegisterCount()) ? -1 : ((registerCount == o.getRegisterCount()) ? 0 : 1);
    }
}
