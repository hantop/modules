package com.fenlibao.p2p.model.xinwang.entity.project;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 提前还款，利息管理费实体类
 * Created by Administrator on 2018/1/26.
 */
public class InterestFeeEntity {

    /**
     * 债权id
     */
    private Integer creditId ;

    /**
     * 利息的6252id
     */
    private Integer interestId;

    /**
     * 利息管理费的6252id
     */
    private Integer manageFeeId;

    /**
     * 剩余本金
     */
    private BigDecimal remainPrincipalOfCredit=BigDecimal.ZERO;

    /**
     * 日利息  (剩余本金*还款利率/365)
     */
    private BigDecimal interestOfDay=BigDecimal.ZERO;

    /**
     * 当前借款天数
     */
    private int  currentTermLoanDays;


    /**
     * 原始利息
     */
    private BigDecimal orginalInterst=BigDecimal.ZERO;
    /**
     * 投资加息利息
     */
    private BigDecimal investAddInterest = BigDecimal.ZERO;

    /**
     * 标加息利息
     */
    private BigDecimal bidAddInterest = BigDecimal.ZERO;

    /**
     * 提前还款计算出来的利息，此时未扣减利息管理费
     */
    private BigDecimal newInterst=BigDecimal.ZERO;

    /**
     * 计算利息服务费的总费用
     */
    private BigDecimal totalAmount=BigDecimal.ZERO;

    /**
     * 利息管理费费率
     */
    private BigDecimal interestManageRate=BigDecimal.ZERO;

    /**
     * 利息管理费
     */
    private BigDecimal interestManageFee=BigDecimal.ZERO;



    /**
     * 扣减了利息管理费的利息(最后入库的利息)
     */
    private BigDecimal lastInterest=BigDecimal.ZERO;

    /**
     * 原本的利息还款计划
     */
    private XWRepaymentPlan interestPlan;

    /**
     * 原本的利息管理费还款计划
     */
    private XWRepaymentPlan manageFeePaln;


    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public BigDecimal getOrginalInterst() {
        return orginalInterst;
    }

    public void setOrginalInterst(BigDecimal orginalInterst) {
        this.orginalInterst = orginalInterst;
    }

    public BigDecimal getNewInterst() {
        this.newInterst=interestOfDay.multiply(new BigDecimal(currentTermLoanDays)).setScale(2, RoundingMode.HALF_UP);
        return newInterst;
    }

    public void setNewInterst(BigDecimal newInterst) {
        this.newInterst = interestOfDay.multiply(new BigDecimal(currentTermLoanDays)).setScale(2, RoundingMode.HALF_UP);

    }

    /**
     * 总数 新利息+标计息+投资加息
     * @return
     */
    public BigDecimal getTotalAmount() {
        this.totalAmount = this.getNewInterst().add(this.bidAddInterest).add(this.investAddInterest);
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * 获得最后的利息管理费
     * @return
     */
    public BigDecimal getLastInterest() {
        this.lastInterest=this.getNewInterst().subtract(this.getInterestManageFee());
        return lastInterest;
    }

    public void setLastInterest(BigDecimal lastInterest) {
        this.lastInterest = lastInterest;
    }

    /**
     * 计算利息管理费
     * @return
     */
    public BigDecimal getInterestManageFee() {
        this.interestManageFee=this.getTotalAmount().multiply(this.interestManageRate).setScale(3, BigDecimal.ROUND_DOWN);
        this.interestManageFee=interestManageFee.setScale(2,BigDecimal.ROUND_UP);//千分位进位管理费
        return interestManageFee;
    }

    public void setInterestManageFee(BigDecimal interestManageFee) {
        this.interestManageFee = interestManageFee;
    }

    public BigDecimal getInvestAddInterest() {
        return investAddInterest;
    }

    public void setInvestAddInterest(BigDecimal investAddInterest) {
        this.investAddInterest = investAddInterest;
    }

    public BigDecimal getBidAddInterest() {
        return bidAddInterest;
    }

    public void setBidAddInterest(BigDecimal bidAddInterest) {
        this.bidAddInterest = bidAddInterest;
    }

    public BigDecimal getInterestManageRate() {
        return interestManageRate;
    }

    public void setInterestManageRate(BigDecimal interestManageRate) {
        this.interestManageRate = interestManageRate;
    }

    public BigDecimal getRemainPrincipalOfCredit() {
        return remainPrincipalOfCredit;
    }

    public void setRemainPrincipalOfCredit(BigDecimal remainPrincipalOfCredit) {
        this.remainPrincipalOfCredit = remainPrincipalOfCredit;
    }

    public BigDecimal getInterestOfDay() {
        return interestOfDay;
    }

    public void setInterestOfDay(BigDecimal interestOfDay) {
        this.interestOfDay = interestOfDay;
    }

    public int getCurrentTermLoanDays() {
        return currentTermLoanDays;
    }

    public void setCurrentTermLoanDays(int currentTermLoanDays) {
        this.currentTermLoanDays = currentTermLoanDays;
    }

    public Integer getInterestId() {
        return interestId;
    }

    public void setInterestId(Integer interestId) {
        this.interestId = interestId;
    }

    public Integer getManageFeeId() {
        return manageFeeId;
    }

    public void setManageFeeId(Integer manageFeeId) {
        this.manageFeeId = manageFeeId;
    }

    public InterestFeeEntity() {
        this.newInterst = this.getNewInterst();
        this.interestManageFee = this.getInterestManageFee();
        this.lastInterest = this.getLastInterest();
    }

    public XWRepaymentPlan getInterestPlan() {
        return interestPlan;
    }

    public void setInterestPlan(XWRepaymentPlan interestPlan) {
        this.interestPlan = interestPlan;
    }

    public XWRepaymentPlan getManageFeePaln() {
        return manageFeePaln;
    }

    public void setManageFeePaln(XWRepaymentPlan manageFeePaln) {
        this.manageFeePaln = manageFeePaln;
    }

    @Override
    public String toString() {
        return "InterestFeeEntity{" +
                "creditId=" + creditId +
                ", interestId=" + interestId +
                ", manageFeeId=" + manageFeeId +
                ", remainPrincipalOfCredit=" + remainPrincipalOfCredit +
                ", interestOfDay=" + interestOfDay +
                ", currentTermLoanDays=" + currentTermLoanDays +
                ", orginalInterst=" + orginalInterst +
                ", investAddInterest=" + investAddInterest +
                ", bidAddInterest=" + bidAddInterest +
                ", newInterst=" + newInterst +
                ", totalAmount=" + totalAmount +
                ", interestManageRate=" + interestManageRate +
                ", interestManageFee=" + interestManageFee +
                ", lastInterest=" + lastInterest +
                '}';
    }
}
