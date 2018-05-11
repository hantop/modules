package com.fenlibao.model.pms.da.cs;

import java.sql.Timestamp;

/**
 * Created by Louis Wang on 2015/12/23.
 */

public class ReplacePhoneInfo {

    private Integer userId;// 用户ID
    private String oldPhone;// 旧手机号
    private String newPhone;// 新手机号
    private String operator;// 操作人
    private Timestamp operatorTime;// 解绑开始时间

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOldPhone() {
        return oldPhone;
    }

    public void setOldPhone(String oldPhone) {
        this.oldPhone = oldPhone;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Timestamp getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Timestamp operatorTime) {
        this.operatorTime = operatorTime;
    }
}
