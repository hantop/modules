package com.fenlibao.p2p.model.entity.redenvelope;

/**
 * 投资金额对应的奖励类型  flb.t_tender_share_setting_reward
 * @author Mingway.Xu
 * @date 2017/1/23 18:28
 */
public class ShareRewardEntity {
    private int id;
    private int settingId;
    private int rewardType;
    private int rewardId;
    private int getTimes;
    private int isNovice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public int getRewardType() {
        return rewardType;
    }

    public void setRewardType(int rewardType) {
        this.rewardType = rewardType;
    }

    public int getRewardId() {
        return rewardId;
    }

    public void setRewardId(int rewardId) {
        this.rewardId = rewardId;
    }

    public int getGetTimes() {
        return getTimes;
    }

    public void setGetTimes(int getTimes) {
        this.getTimes = getTimes;
    }

    public int getIsNovice() {
        return isNovice;
    }

    public void setIsNovice(int isNovice) {
        this.isNovice = isNovice;
    }
}
