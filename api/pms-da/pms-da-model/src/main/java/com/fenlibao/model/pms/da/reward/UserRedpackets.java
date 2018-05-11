package com.fenlibao.model.pms.da.reward;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class UserRedpackets implements Comparable<UserRedpackets> {
    private int id;

    private Integer userId;

    private Integer redpacketId;

    private Byte status;

    private Integer bidId;

    private Date validTime;

    private Integer grantId;

    private Byte grantStatus;

    //额外字段
    private String grantStatusName;
    private String phone;
    private String activityCode;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Integer effectDay;

    private BigDecimal redMoney;

    private BigDecimal investMoney;

    private int reawrdType;// 奖励类型

    private String grantName;//导入报表的名字

    private String remarks;// 来源

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRedpacketId() {
        return redpacketId;
    }

    public void setRedpacketId(Integer redpacketId) {
        this.redpacketId = redpacketId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public Integer getGrantId() {
        return grantId;
    }

    public void setGrantId(Integer grantId) {
        this.grantId = grantId;
    }

    public Byte getGrantStatus() {
        return grantStatus;
    }

    public void setGrantStatus(Byte grantStatus) {
        this.grantStatus = grantStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getGrantStatusName() {
        return grantStatusName;
    }

    public void setGrantStatusName(String grantStatusName) {
        this.grantStatusName = grantStatusName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(Integer effectDay) {
        this.effectDay = effectDay;
    }

    public BigDecimal getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(BigDecimal redMoney) {
        this.redMoney = redMoney;
    }

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }


    public int getReawrdType() {
        return reawrdType;
    }

    public void setReawrdType(int reawrdType) {
        this.reawrdType = reawrdType;
    }

    @Override
    public int compareTo(UserRedpackets o) {
        boolean flag = this.getRedpacketId() == o.getRedpacketId() && this.getUserId() == o.getUserId();
        return flag ? 1 : -1;
    }

    public String getGrantName() {
        return grantName;
    }

    public void setGrantName(String grantName) {
        this.grantName = grantName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}