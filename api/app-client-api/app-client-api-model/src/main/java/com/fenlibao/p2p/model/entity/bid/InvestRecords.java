package com.fenlibao.p2p.model.entity.bid;

/**
 * Created by kris on 2016/10/28.
 * 消费信贷标投资记录
 */
public class InvestRecords {
    private int recordId;
    private int userId;
    private String phone;
    private String userName;
    private String signid;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSignid() {
        return signid;
    }

    public void setSignid(String signid) {
        this.signid = signid;
    }
}
