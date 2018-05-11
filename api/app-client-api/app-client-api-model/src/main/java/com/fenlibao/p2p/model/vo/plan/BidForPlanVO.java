package com.fenlibao.p2p.model.vo.plan;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zeronx on 2017/11/17 11:39.
 * @version 1.0
 */
public class BidForPlanVO implements Serializable {

    private Integer planLibId; // flb.t_plan_product_lib.id
    private Integer planId; // flb.t_invest_plan.id
    private Integer bidId; // s62.t6230.F01
    private Integer userId; // 借款用户Id s62.t6230.F02
    private Integer bind; // 0:不绑定、1：绑定
    private String bidName; // bid 的名称
    private BigDecimal loanAmount; // 借款金额
    private BigDecimal rate; // 借款年利率
    private BigDecimal voteAmount; // 可投金额
    private Integer month; // 借款月
    private Integer loanDays; // 借款日
    private String repaymentMode; // 还款方式
    private String isNoviceBid; // 是否新手bid：S:是、F:否

    public Integer getPlanLibId() {
        return planLibId;
    }

    public void setPlanLibId(Integer planLibId) {
        this.planLibId = planLibId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBind() {
        return bind;
    }

    public void setBind(Integer bind) {
        this.bind = bind;
    }

    public String getBidName() {
        return bidName;
    }

    public void setBidName(String bidName) {
        this.bidName = bidName;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getVoteAmount() {
        return voteAmount;
    }

    public void setVoteAmount(BigDecimal voteAmount) {
        this.voteAmount = voteAmount;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(Integer loanDays) {
        this.loanDays = loanDays;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    public String getIsNoviceBid() {
        return isNoviceBid;
    }

    public void setIsNoviceBid(String isNoviceBid) {
        this.isNoviceBid = isNoviceBid;
    }
}
