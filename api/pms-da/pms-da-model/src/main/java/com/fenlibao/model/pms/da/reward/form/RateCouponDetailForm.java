package com.fenlibao.model.pms.da.reward.form;

public class RateCouponDetailForm {
    private byte rewardType;//奖励类型(1:体验金，2：现金红包，3：返现券 4：加息券)
    private Integer grantId;
    private String activityCode;
    private Byte grantStatus;

    public byte getRewardType() {
        return rewardType;
    }

    public void setRewardType(byte rewardType) {
        this.rewardType = rewardType;
    }

    public Integer getGrantId() {
        return grantId;
    }

    public void setGrantId(Integer grantId) {
        this.grantId = grantId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public Byte getGrantStatus() {
        return grantStatus;
    }

    public void setGrantStatus(Byte grantStatus) {
        this.grantStatus = grantStatus;
    }
}