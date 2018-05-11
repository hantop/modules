package com.fenlibao.p2p.model.lottery.vo;

public class LotteryPrizeInfoVO {

    //奖项id
    private int prizeId;

    //奖项名称
    private String prizeName;

    //奖项类型
    private int prizeType;

    //奖项logo
    private String prizeLogo;

    //显示顺序
    private int listOrder;

    //20170425 奖品中奖纪录id
    private int prizeRecordId;

    public int getPrizeRecordId() {
        return prizeRecordId;
    }

    public void setPrizeRecordId(int prizeRecordId) {
        this.prizeRecordId = prizeRecordId;
    }

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName==null?"":prizeName;
    }

    public int getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(int prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeLogo() {
        return prizeLogo;
    }

    public void setPrizeLogo(String prizeLogo) {
        this.prizeLogo = prizeLogo==null?"":prizeLogo;
    }

    public int getListOrder() {
        return listOrder;
    }

    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
    }
}