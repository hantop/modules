package com.fenlibao.p2p.model.lottery.vo;

public class UserLotteryRecordVO {

    //用户手机尾号
    private String cellTailNumber;

    //奖项名称
    private String prizeName;

    //记录id
    private int recordId;

    public String getCellTailNumber() {
        return cellTailNumber;
    }

    public void setCellTailNumber(String cellTailNumber) {
        this.cellTailNumber = cellTailNumber;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
}