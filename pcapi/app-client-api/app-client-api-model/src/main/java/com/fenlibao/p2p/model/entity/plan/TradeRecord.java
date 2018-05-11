package com.fenlibao.p2p.model.entity.plan;

import java.math.BigDecimal;

/**
 * @author zeronx on 2017/11/23 14:35.
 * @version 1.0
 */
public class TradeRecord {

    private Integer id; // 流水Id
    private Integer payAccountId; // 资金账号ID,参考T6101.F01
    private Integer payTypeId; // 交易类型ID,参考T5122.F01
    private Integer incomeAccountId; // 对方账户ID,参考T6101.F01
    private BigDecimal incomeAmount; // 收入金额
    private BigDecimal payAmount; // 支出金额
    private BigDecimal balance; // 余额
    private String description; // 描述
    private String payLevel; // 流水级别 XT:系统级别;YH:用户级别;
    private Integer orderId; // 订单Id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPayAccountId() {
        return payAccountId;
    }

    public void setPayAccountId(Integer payAccountId) {
        this.payAccountId = payAccountId;
    }

    public Integer getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(Integer payTypeId) {
        this.payTypeId = payTypeId;
    }

    public Integer getIncomeAccountId() {
        return incomeAccountId;
    }

    public void setIncomeAccountId(Integer incomeAccountId) {
        this.incomeAccountId = incomeAccountId;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayLevel() {
        return payLevel;
    }

    public void setPayLevel(String payLevel) {
        this.payLevel = payLevel;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
