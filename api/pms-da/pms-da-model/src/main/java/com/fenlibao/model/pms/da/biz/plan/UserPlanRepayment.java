package com.fenlibao.model.pms.da.biz.plan;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户投资计划对应还款计划 flb.t_user_plan_repayment
 * by：kris 20170327
 */

public class UserPlanRepayment extends InvestPlanInfo{
    private int userPlanRepaymentId;// 用户投资计划对应还款计划id

    private Integer userPlanId;// 用户投资计划id

    private Integer userPlanProductId;//用户投资计划产品id(t_user_plan_product.id

    private Integer payeeId;//收款人id(投资用户)

    private BigDecimal amount;//还款金额

    private Date repaymentDate;//应还日期

    private Integer tradeType;//交易类型,1.本息，2.逾期罚息
    
    public int getUserPlanRepaymentId() {
        return userPlanRepaymentId;
    }

    public void setUserPlanRepaymentId(int userPlanRepaymentId) {
        this.userPlanRepaymentId = userPlanRepaymentId;
    }

    public Integer getUserPlanId() {
        return userPlanId;
    }

    public void setUserPlanId(Integer userPlanId) {
        this.userPlanId = userPlanId;
    }

    public Integer getUserPlanProductId() {
        return userPlanProductId;
    }

    public void setUserPlanProductId(Integer userPlanProductId) {
        this.userPlanProductId = userPlanProductId;
    }

    public Integer getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Integer payeeId) {
        this.payeeId = payeeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(Date repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }
}
