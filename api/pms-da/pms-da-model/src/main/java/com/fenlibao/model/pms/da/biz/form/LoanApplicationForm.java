package com.fenlibao.model.pms.da.biz.form;

/**
 * Created by Administrator on 2016/4/11.
 */
public class LoanApplicationForm {
    private String startDate;
    private String endDate;
    private String phonenum;// 手机号
    private String processingStatus;// 处理状态（0=未处理，1=已处理）

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(String processingStatus) {
        this.processingStatus = processingStatus;
    }
}
