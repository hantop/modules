package com.fenlibao.p2p.model.dm.vo;

/**
 * Created by zcai on 2016/10/20.
 */
public class HXAccountInfo {

    private Integer userId;
    private String acNo;//华兴账号(加密)
    private Integer isBindBankCard;//是否已绑卡(0=未绑定，1=已绑定)
    private Integer autoTenderFlag;//自动投标授权标识(0=未授权，1=已授权)
    private Integer autoRepaymentFlag;//自动还款授权标识(0=未授权，1=已授权)

    private String acName;//大部分接口都用到acNo和acName,所以一起获取

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAcNo() {
        return acNo;
    }

    public void setAcNo(String acNo) {
        this.acNo = acNo;
    }

    public Integer getIsBindBankCard() {
        return isBindBankCard;
    }

    public void setIsBindBankCard(Integer isBindBankCard) {
        this.isBindBankCard = isBindBankCard;
    }

    public Integer getAutoTenderFlag() {
        return autoTenderFlag;
    }

    public void setAutoTenderFlag(Integer autoTenderFlag) {
        this.autoTenderFlag = autoTenderFlag;
    }

    public Integer getAutoRepaymentFlag() {
        return autoRepaymentFlag;
    }

    public void setAutoRepaymentFlag(Integer autoRepaymentFlag) {
        this.autoRepaymentFlag = autoRepaymentFlag;
    }
}
