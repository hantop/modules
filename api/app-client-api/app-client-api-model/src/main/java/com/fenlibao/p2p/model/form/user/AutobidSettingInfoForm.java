package com.fenlibao.p2p.model.form.user;

/**
 * @author Mingway.Xu
 * @date 2016/11/15 9:58
 */
public class AutobidSettingInfoForm {

    private String interestRate;

    private String timeMin;

    private String minMark;

    private String maxMark;

    private String timeMax;

    private String reserve;

    private String validityMod;

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

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getValidityMod() {
        return validityMod;
    }

    public void setValidityMod(String validityMod) {
        this.validityMod = validityMod;
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
