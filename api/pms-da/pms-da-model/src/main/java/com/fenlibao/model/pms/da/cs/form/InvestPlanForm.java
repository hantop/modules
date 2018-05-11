package com.fenlibao.model.pms.da.cs.form;

public class InvestPlanForm extends TransactionForm{
    private Integer userId;
    private Integer planId;
    private Integer recordId;// 投资计划记录Id
    private String title;//计划的名称
    private Integer planType;// 区分新旧计划

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPlanType() {
        return planType;
    }

    public void setPlanType(Integer planType) {
        this.planType = planType;
    }
}
