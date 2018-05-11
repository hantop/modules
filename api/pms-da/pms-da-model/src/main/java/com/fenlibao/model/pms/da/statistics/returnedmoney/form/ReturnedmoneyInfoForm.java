package com.fenlibao.model.pms.da.statistics.returnedmoney.form;

/**
 * 回款信息
 *
 * Created by chenzhixuan on 2016/3/22.
 */
public class ReturnedmoneyInfoForm {
    private String startDate;
    private String endDate;
    private Integer status;// 回款状态
    private String firstReturnedmoney;// 是否首次回款

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFirstReturnedmoney() {
        return firstReturnedmoney;
    }

    public void setFirstReturnedmoney(String firstReturnedmoney) {
        this.firstReturnedmoney = firstReturnedmoney;
    }
}