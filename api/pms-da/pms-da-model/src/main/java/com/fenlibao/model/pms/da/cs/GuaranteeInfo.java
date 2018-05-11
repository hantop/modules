package com.fenlibao.model.pms.da.cs;

/**
 * Created by zeronx on 2017/6/26.
 * 担保账户 列表信息
 */

public class GuaranteeInfo extends XWAccountInfo{

    private String balance;// 账号余额
    private Integer isBindBank; // 是否已绑定银行卡： 1:是、0:否
    private Integer allowUnbind;// 是否允许解绑银行卡 0：否、1：是

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Integer getIsBindBank() {
        return isBindBank;
    }

    public void setIsBindBank(Integer isBindBank) {
        this.isBindBank = isBindBank;
    }

    public Integer getAllowUnbind() {
        return allowUnbind;
    }

    public void setAllowUnbind(Integer allowUnbind) {
        this.allowUnbind = allowUnbind;
    }
}
