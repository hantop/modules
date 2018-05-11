package com.fenlibao.p2p.model.xinwang.entity.project;

import com.fenlibao.p2p.model.xinwang.enums.project.ProjectExtraInfo_Overdue;

import java.util.Date;

/**
 * t6231
 */
public class XWProjectExtraInfo {
    private Integer id;

    /**
     * 还款总期数
     */
    private Integer totalTerms;

    /**
     * 剩余期数
     */
    private Integer remainTerms;

    /**
     * 下次还款日期
     */
    private Date nextRepayDate;

    /**
     * 资金用途
     */
    private String useFor;

    /**
     * 放款时间
     */
    private Date bidConfirmTime;

    /**
     * 流标时间
     */
    private Date cancelTenderTime;

    /**
     * 结清时间
     */
    private Date settleTime;
    /**
     * 起息时间
     */
    private Date bearInterestDate;
    /**
     * 结束时间
     */
    private Date endDate;

    /*
     * 合同编号
     */
    private String agreementNo;

    /**
     * 是否逾期
     */
    private ProjectExtraInfo_Overdue overdue;

    /**
     * 发布后是否加入计划债权库
     */
    private Boolean joinPlan;

    /**
     * 担保用户id
     */
    private Integer guaranteeUserId;

    /**
     * 委托收款人用户id
     */
    private Integer entrustPayeeUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalTerms() {
        return totalTerms;
    }

    public void setTotalTerms(Integer totalTerms) {
        this.totalTerms = totalTerms;
    }

    public Integer getRemainTerms() {
        return remainTerms;
    }

    public void setRemainTerms(Integer remainTerms) {
        this.remainTerms = remainTerms;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public String getUseFor() {
        return useFor;
    }

    public void setUseFor(String useFor) {
        this.useFor = useFor;
    }

    public Date getBidConfirmTime() {
        return bidConfirmTime;
    }

    public void setBidConfirmTime(Date bidConfirmTime) {
        this.bidConfirmTime = bidConfirmTime;
    }

    public Date getBearInterestDate() {
        return bearInterestDate;
    }

    public void setBearInterestDate(Date bearInterestDate) {
        this.bearInterestDate = bearInterestDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ProjectExtraInfo_Overdue getOverdue() {
        return overdue;
    }

    public void setOverdue(ProjectExtraInfo_Overdue overdue) {
        this.overdue = overdue;
    }

    public Date getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }

    public Date getCancelTenderTime() {
        return cancelTenderTime;
    }

    public void setCancelTenderTime(Date cancelTenderTime) {
        this.cancelTenderTime = cancelTenderTime;
    }

    public Integer getGuaranteeUserId() {
        return guaranteeUserId;
    }

    public void setGuaranteeUserId(Integer guaranteeUserId) {
        this.guaranteeUserId = guaranteeUserId;
    }

    public Integer getEntrustPayeeUserId() {
        return entrustPayeeUserId;
    }

    public void setEntrustPayeeUserId(Integer entrustPayeeUserId) {
        this.entrustPayeeUserId = entrustPayeeUserId;
    }

    public Boolean getJoinPlan() {
        return joinPlan;
    }

    public void setJoinPlan(Boolean joinPlan) {
        this.joinPlan = joinPlan;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo;
    }
}
