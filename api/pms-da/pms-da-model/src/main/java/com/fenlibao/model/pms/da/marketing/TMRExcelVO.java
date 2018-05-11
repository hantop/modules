package com.fenlibao.model.pms.da.marketing;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Louis Wang on 2016/3/9.
 */

public class TMRExcelVO implements Serializable{

    private Integer id;

    private Integer trmId;

    private String phoneNumber;

    private String callTime;

    private Timestamp endTime;// 呼入结束时间

    private String msg; //原因

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrmId() {
        return trmId;
    }

    public void setTrmId(Integer trmId) {
        this.trmId = trmId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
