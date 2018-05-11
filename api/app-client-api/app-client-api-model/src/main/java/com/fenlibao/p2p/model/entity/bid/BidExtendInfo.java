package com.fenlibao.p2p.model.entity.bid;

import java.util.Date;

/**
 * Created by laubrence on 2016/3/29.
 */
public class BidExtendInfo extends BidBaseInfo {

    int totalRepaymentPeriod;//还款总期数
    int leftRepaymentPeriod;//	剩余期数
    Date nextRepaymentDate;//下次还款日期
    Date loanDte; //放款时间
    Date payoffDate; //结清时间
    Date interestDate; //起息日期
    Date expireDate; //到期日期
    String isYq;//标是否逾期
    String remark;// 借款描述
    Integer guaranteeUserId; //担保用户id
    Integer thirdRegisterState; //第三方注册状态


    public String getIsYq() {
        return isYq;
    }

    public void setIsYq(String isYq) {
        this.isYq = isYq;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Date getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(Date interestDate) {
        this.interestDate = interestDate;
    }

    public int getLeftRepaymentPeriod() {
        return leftRepaymentPeriod;
    }

    public void setLeftRepaymentPeriod(int leftRepaymentPeriod) {
        this.leftRepaymentPeriod = leftRepaymentPeriod;
    }

    public Date getLoanDte() {
        return loanDte;
    }

    public void setLoanDte(Date loanDte) {
        this.loanDte = loanDte;
    }

    public Date getNextRepaymentDate() {
        return nextRepaymentDate;
    }

    public void setNextRepaymentDate(Date nextRepaymentDate) {
        this.nextRepaymentDate = nextRepaymentDate;
    }

    public Date getPayoffDate() {
        return payoffDate;
    }

    public void setPayoffDate(Date payoffDate) {
        this.payoffDate = payoffDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getTotalRepaymentPeriod() {
        return totalRepaymentPeriod;
    }

    public void setTotalRepaymentPeriod(int totalRepaymentPeriod) {
        this.totalRepaymentPeriod = totalRepaymentPeriod;
    }

    public Integer getGuaranteeUserId() {
        return guaranteeUserId;
    }

    public void setGuaranteeUserId(Integer guaranteeUserId) {
        this.guaranteeUserId = guaranteeUserId;
    }

    public Integer getThirdRegisterState() {
        return thirdRegisterState;
    }

    public void setThirdRegisterState(Integer thirdRegisterState) {
        this.thirdRegisterState = thirdRegisterState;
    }
}
