package com.fenlibao.model.pms.da.biz.viewobject;

/**
 * Created by Administrator on 2016/4/11.
 */
public class LoanApplicationVO {
    private String phonenum;// 手机号
    private String contacts;// 联系人
    private String amountRange;// 借款金额范围
    private String districtFullName;// 所在区域全称
    private String annualIncome;// 月收入（仅记录收入范围）
    private String hasRoom;// 是否有房（0=否，1=有）
    private String hasCar;// 是否有车（0=否，1=有）
    private String createTime;// 创建时间
    private String updateTime;// 修改时间
    private String processingTime;// 处理时间
    private int processingStatus;// 处理状态（0=未处理，1=通过，2=不通过）
    private String processingOpinion;// 处理意见
    private String nopassReasonTitle;// 不通过原因标题

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getAmountRange() {
        return amountRange;
    }

    public void setAmountRange(String amountRange) {
        this.amountRange = amountRange;
    }

    public String getDistrictFullName() {
        return districtFullName;
    }

    public void setDistrictFullName(String districtFullName) {
        this.districtFullName = districtFullName;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getHasRoom() {
        return hasRoom;
    }

    public void setHasRoom(String hasRoom) {
        this.hasRoom = hasRoom;
    }

    public String getHasCar() {
        return hasCar;
    }

    public void setHasCar(String hasCar) {
        this.hasCar = hasCar;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
    }

    public int getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(int processingStatus) {
        this.processingStatus = processingStatus;
    }

    public String getProcessingOpinion() {
        return processingOpinion;
    }

    public void setProcessingOpinion(String processingOpinion) {
        this.processingOpinion = processingOpinion;
    }

    public String getNopassReasonTitle() {
        return nopassReasonTitle;
    }

    public void setNopassReasonTitle(String nopassReasonTitle) {
        this.nopassReasonTitle = nopassReasonTitle;
    }
}