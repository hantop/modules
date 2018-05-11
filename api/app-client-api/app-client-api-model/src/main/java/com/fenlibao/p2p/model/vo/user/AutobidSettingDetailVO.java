package com.fenlibao.p2p.model.vo.user;

import java.util.Date;

/**
 * @author Mingway.Xu
 * @date 2016/11/15 16:05
 */
public class AutobidSettingDetailVO {
    private int settingId;
    private String interestRate;
    private String timeMin;
    private String minMark;
    private String timeMax;
    private String maxMark;
    private String validityMod;
    private Date startTime;
    private Date endTime;
    private boolean active;
    private String bidType;
    private String repaymentMode;
    private String reserve;

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(String timeMin) {
        this.timeMin = timeMin;
    }

    public String getTimeMax() {
        return timeMax;
    }

    public void setTimeMax(String timeMax) {
        this.timeMax = timeMax;
    }

    public String getValidityMod() {
        return validityMod;
    }

    public void setValidityMod(String validityMod) {
        this.validityMod = validityMod;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getRepaymentMode() {
        return repaymentMode;
    }

    public void setRepaymentMode(String repaymentMode) {
        this.repaymentMode = repaymentMode;
    }

    public String getMinMark() {
        return minMark;
    }

    public void setMinMark(String minMark) {
        this.minMark = minMark;
    }

    public String getMaxMark() {
        return maxMark;
    }

    public void setMaxMark(String maxMark) {
        this.maxMark = maxMark;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }
}
