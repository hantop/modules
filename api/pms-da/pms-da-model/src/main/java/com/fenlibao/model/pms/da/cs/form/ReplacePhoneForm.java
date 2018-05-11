package com.fenlibao.model.pms.da.cs.form;

/**
 * Created by Louis Wang on 2015/12/23.
 */

public class ReplacePhoneForm {
    private Integer userId;// 用户ID
    private String phoneNum;// 手机号
    private String operator;// 操作人
    private String unbindStartTime;// 解绑开始时间
    private String unbindEndTime;// 解绑结束时间

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUnbindStartTime() {
        return unbindStartTime;
    }

    public void setUnbindStartTime(String unbindStartTime) {
        this.unbindStartTime = unbindStartTime;
    }

    public String getUnbindEndTime() {
        return unbindEndTime;
    }

    public void setUnbindEndTime(String unbindEndTime) {
        this.unbindEndTime = unbindEndTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
