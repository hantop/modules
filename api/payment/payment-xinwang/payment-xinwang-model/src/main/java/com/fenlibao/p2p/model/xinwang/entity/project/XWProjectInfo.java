package com.fenlibao.p2p.model.xinwang.entity.project;

import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.project.ProjectType;
import com.fenlibao.p2p.model.xinwang.enums.project.RepaymentWay;

import java.math.BigDecimal;
import java.util.Date;

/**
 * t6230
 */
public class XWProjectInfo {
    private String projectNo;
    /**
     * 借款人存管账号，t_xw_project.borrower_platform_user_no
     */
    private String borrowerPlatformUserNo;
    /**
     * 平台用户id，t6110.F01
     */
    private Integer borrowerUserId;
    /**
     * 标名称
     */
    private String projectName;
    /**
     * 标类型
     */
    private Integer bidType;
    /**
     * 借款金额
     */
    private BigDecimal projectAmount;
    /**
     * 存管字段，标类型 t_xw_project.project_type
     */
    private ProjectType projectType;
    /**
     * 年化利率
     */
    private BigDecimal annnualInterestRate;
    /**
     * 还款方式
     */
    private RepaymentWay repaymentWay;
    /**
     * 筹款天数
     */
    private Integer fundRaisingDays;
    /**
     * 筹款到期时间
     */
    private Date fundRaisingDeadline;
    /**
     * 标状态
     */
    private PTProjectState state;
    /**
     * 发布时间
     */
    private Date establishTime;
    /**
     * 显示时间
     */
    private Date displayTime;
    /**
     * 月标周期
     */
    private Integer monthProjectPeriod;
    /**
     * 日标周期
     */
    private Integer dayProjectPeriod;
    /**
     * 剩余可投金额
     */
    private BigDecimal surplusAmount;
    /**
     * 标编号，t6230.F25
     */
    private String projectCode;

    /**
     * 是否正在还款，t6230.F34
     */
    private Boolean inProgressOfRepay;

    /**
     * 标的来源，t6230.F36
     */
    private String projectSource;

    /**
     * 担保人存管账号， t_xw_project.guarantee_platform_user_no
     */
    private String guaranteePlatformUserNo;

    /**
     * 委托收款人用户编号， t_xw_project.entrust_payee_platform_user_no
     */
    private String entrustPayeePlatformUserNo;

    /**
     * 委托支付授权请求编号，t_xw_project.entrust_pay_authorize_request_no
     */
    private String entrustPayAuthorizeRequestNo;



    /**
     * 存管版优化二期需求：_2017.12.29
     * 服务费收取节点方式:1 前置收取,0后置收取
     */
    private int collectType;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getBorrowerPlatformUserNo() {
        return borrowerPlatformUserNo;
    }

    public void setBorrowerPlatformUserNo(String borrowerPlatformUserNo) {
        this.borrowerPlatformUserNo = borrowerPlatformUserNo;
    }

    public Integer getBidType() {
        return bidType;
    }

    public void setBidType(Integer bidType) {
        this.bidType = bidType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BigDecimal getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(BigDecimal projectAmount) {
        this.projectAmount = projectAmount;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public BigDecimal getAnnnualInterestRate() {
        return annnualInterestRate;
    }

    public void setAnnnualInterestRate(BigDecimal annnualInterestRate) {
        this.annnualInterestRate = annnualInterestRate;
    }

    public RepaymentWay getRepaymentWay() {
        return repaymentWay;
    }

    public void setRepaymentWay(RepaymentWay repaymentWay) {
        this.repaymentWay = repaymentWay;
    }

    public Integer getFundRaisingDays() {
        return fundRaisingDays;
    }

    public void setFundRaisingDays(Integer fundRaisingDays) {
        this.fundRaisingDays = fundRaisingDays;
    }

    public Date getFundRaisingDeadline() {
        return fundRaisingDeadline;
    }

    public void setFundRaisingDeadline(Date fundRaisingDeadline) {
        this.fundRaisingDeadline = fundRaisingDeadline;
    }

    public PTProjectState getState() {
        return state;
    }

    public void setState(PTProjectState state) {
        this.state = state;
    }

    public Date getEstablishTime() {
        return establishTime;
    }

    public void setEstablishTime(Date establishTime) {
        this.establishTime = establishTime;
    }

    public Date getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(Date displayTime) {
        this.displayTime = displayTime;
    }

    public Integer getMonthProjectPeriod() {
        return monthProjectPeriod;
    }

    public void setMonthProjectPeriod(Integer monthProjectPeriod) {
        this.monthProjectPeriod = monthProjectPeriod;
    }

    public Integer getDayProjectPeriod() {
        return dayProjectPeriod;
    }

    public void setDayProjectPeriod(Integer dayProjectPeriod) {
        this.dayProjectPeriod = dayProjectPeriod;
    }

    public BigDecimal getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(BigDecimal surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public Integer getBorrowerUserId() {
        return borrowerUserId;
    }

    public void setBorrowerUserId(Integer borrowerUserId) {
        this.borrowerUserId = borrowerUserId;
    }

    public Boolean getInProgressOfRepay() {
        return inProgressOfRepay;
    }

    public void setInProgressOfRepay(Boolean inProgressOfRepay) {
        this.inProgressOfRepay = inProgressOfRepay;
    }

    public String getGuaranteePlatformUserNo() {
        return guaranteePlatformUserNo;
    }

    public void setGuaranteePlatformUserNo(String guaranteePlatformUserNo) {
        this.guaranteePlatformUserNo = guaranteePlatformUserNo;
    }

    public String getEntrustPayeePlatformUserNo() {
        return entrustPayeePlatformUserNo;
    }

    public void setEntrustPayeePlatformUserNo(String entrustPayeePlatformUserNo) {
        this.entrustPayeePlatformUserNo = entrustPayeePlatformUserNo;
    }

    public String getEntrustPayAuthorizeRequestNo() {
        return entrustPayAuthorizeRequestNo;
    }

    public void setEntrustPayAuthorizeRequestNo(String entrustPayAuthorizeRequestNo) {
        this.entrustPayAuthorizeRequestNo = entrustPayAuthorizeRequestNo;
    }

    public String getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
    }



    public int getCollectType() {
        return collectType;
    }

    public void setCollectType(int collectType) {
        this.collectType = collectType;
    }

}
