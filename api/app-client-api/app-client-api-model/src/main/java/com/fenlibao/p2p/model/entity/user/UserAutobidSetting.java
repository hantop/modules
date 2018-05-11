package com.fenlibao.p2p.model.entity.user;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 用户自动投标设置
 * @author Mingway.Xu
 * @date 2016/11/13 18:13
 */
public class UserAutobidSetting {

    private int id;

    private int userId;

    private BigDecimal interestRate;

    private int timeMin;

    private String minMark;

    private int timeMax;

    private String maxMark;

    private String bidType;

    private String repaymentMode;

    private BigDecimal reserve;

    private String validityMod;

    private Date startTime;//设置生效开始时间(DIY)

    private Date endTime;//设置生效结束时间(DIY)

    private int active;//是否在使用状态 1：是 0：否

    private int deleteFlag;//是否已经删除-0:已删除；1：未删除

    private Date createTime;//创建时间

    private Date updateTime;//更新时间

    private Date settingBegintime;//自动投标设置开启时间

    private Date lastBidtime;//自动投标成功时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(int timeMin) {
        this.timeMin = timeMin;
    }

    public int getTimeMax() {
        return timeMax;
    }

    public void setTimeMax(int timeMax) {
        this.timeMax = timeMax;
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

    public BigDecimal getReserve() {
        return reserve;
    }

    public void setReserve(BigDecimal reserve) {
        this.reserve = reserve;
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getSettingBegintime() {
        return settingBegintime;
    }

    public void setSettingBegintime(Date settingBegintime) {
        this.settingBegintime = settingBegintime;
    }

    public Date getLastBidtime() {
        return lastBidtime;
    }

    public void setLastBidtime(Date lastBidtime) {
        this.lastBidtime = lastBidtime;
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
}
