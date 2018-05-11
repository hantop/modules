package com.fenlibao.p2p.model.vo.user;

/**
 * @author Mingway.Xu
 * @date 2016/11/15 14:42
 */
public class AutobidSettingVO {
    /**
     settingId	Int	设置id
     interestRate	String	年化率
     timeMin	String	投资期限最小天数
     timeMax	String	投资期限最大天数
     validityMod	String	有效期(CQYX:长期有效DIY:自定义)
     startTime	long	设置生效开始时间(DIY)
     endTime	long	设置生效结束时间(DIY)
     active	boolean	是否启用设置 (0:否；1：是',)

     */
    private int settingId;
    private String interestRate;
    private String timeMin;
    private String minMark;
    private String timeMax;
    private String maxMark;
    private String validityMod;
    private long startTime;
    private long endTime;
    private boolean active;


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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
