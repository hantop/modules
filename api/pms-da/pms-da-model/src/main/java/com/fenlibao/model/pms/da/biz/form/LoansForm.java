package com.fenlibao.model.pms.da.biz.form;

import java.util.Set;

/**
 * 计划
 * <p>
 * Created by chenzhixuan on 2017/2/7.
 */
public class LoansForm {
    private Integer planId;// 计划ID
    private String planName;// 计划名称
    private String loanName;// 标的名称
    private String releaseStartDate;// 发布开始时间
    private String releaseEndDate;// 发布结束时间
    private String tenderfullStartDate;// 投满开始时间
    private String tenderfullEndDate;// 投满结束时间
    private String status;// 状态
    private Set<String> statuses;// 状态

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getReleaseStartDate() {
        return releaseStartDate;
    }

    public void setReleaseStartDate(String releaseStartDate) {
        this.releaseStartDate = releaseStartDate;
    }

    public String getReleaseEndDate() {
        return releaseEndDate;
    }

    public void setReleaseEndDate(String releaseEndDate) {
        this.releaseEndDate = releaseEndDate;
    }

    public String getTenderfullStartDate() {
        return tenderfullStartDate;
    }

    public void setTenderfullStartDate(String tenderfullStartDate) {
        this.tenderfullStartDate = tenderfullStartDate;
    }

    public String getTenderfullEndDate() {
        return tenderfullEndDate;
    }

    public void setTenderfullEndDate(String tenderfullEndDate) {
        this.tenderfullEndDate = tenderfullEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<String> statuses) {
        this.statuses = statuses;
    }
}