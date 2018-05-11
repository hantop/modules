package com.fenlibao.p2p.model.xinwang.entity.order;

import java.math.BigDecimal;

/**
 * 提现订单 t6503
 */
public class SysWithdrawOrder {
    /**
     * 订单ID,参考T6501.F01
     */
    private Integer id;
    /**
     * 用户ID,参考T6110.F01
     */
    private Integer userId;
    /**
     * 提现金额
     */
    private BigDecimal amount= BigDecimal.ZERO;
    /**
     * 应收手续费
     */
    private BigDecimal commissionReceivable= BigDecimal.ZERO;
    /**
     * 实收手续费
     */
    private BigDecimal paidInCommission= BigDecimal.ZERO;
    /**
     * 银行卡号
     */
    private String bankcard;
    /**
     * 支付公司代码
     */
    private Integer thirdParty;
    /**
     * 流水单号,提现成功时填入
     */
    private String thirdPartyFlow;
    /**
     * 提现申请记录ID,参考T6130.F01
     */
    private Integer withdrawApplyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCommissionReceivable() {
        return commissionReceivable;
    }

    public void setCommissionReceivable(BigDecimal commissionReceivable) {
        this.commissionReceivable = commissionReceivable;
    }

    public BigDecimal getPaidInCommission() {
        return paidInCommission;
    }

    public void setPaidInCommission(BigDecimal paidInCommission) {
        this.paidInCommission = paidInCommission;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public Integer getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(Integer thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getThirdPartyFlow() {
        return thirdPartyFlow;
    }

    public void setThirdPartyFlow(String thirdPartyFlow) {
        this.thirdPartyFlow = thirdPartyFlow;
    }

    public Integer getWithdrawApplyId() {
        return withdrawApplyId;
    }

    public void setWithdrawApplyId(Integer withdrawApplyId) {
        this.withdrawApplyId = withdrawApplyId;
    }
}
