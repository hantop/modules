package com.fenlibao.p2p.model.form.user;

/**
 * @author Mingway.Xu
 * @date 2016/11/16 16:30
 */
public class AutobidSettingForm {
    private String interestRate;

    private String timeMin;

    private String minMark;

    private String maxMark;

    private String timeMax;

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
}
