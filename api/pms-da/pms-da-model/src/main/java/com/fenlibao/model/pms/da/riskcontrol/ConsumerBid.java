package com.fenlibao.model.pms.da.riskcontrol;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费信贷标
 * Created by Administrator on 2017/11/17.
 */
public class ConsumerBid {

    private int id;

    /**
     * 账户
     */
    private String accountNo;

    /**
     * 手机
     */
    private String phone;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 借款金额
     */
    private BigDecimal loanAmount;

    /**
     *借款周期
     */
    private int cycle;

    /**
     *借款周期类型（按天/按月）
     */
    private String cycleType;

    /**
     * 还款方式
     */
    private String paybackWay;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     *审核状态
     */
    private String auditStatus;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 信用分数
     */
    private Integer creditScore;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 银行卡号
     */
    private String bankNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }


    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }


    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public String getPaybackWay() {
        return paybackWay;
    }

    public void setPaybackWay(String paybackWay) {
        this.paybackWay = paybackWay;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }


    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }
}
