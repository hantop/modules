package com.fenlibao.model.pms.da.channel;

import java.math.BigDecimal;

/**
 * 渠道对应的用户统计
 * Created by chenzhixuan on 2015/11/18.
 */
public class ChannelUserStatistics {
    private String phoneNum;// 手机号
    private String nickname;// 昵称
    private String name;// 姓名
    private String idCardEncrypt;// 加密的身份证号码
    private String idCard;// 身份证号码
    private boolean isAuth;// 是否实名认证
    private boolean isBindBankcard;// 是否绑定银行卡
    private BigDecimal rechargeSum;// 充值金额
    private BigDecimal investSum;// 投资金额
    private int investCount;// 投资次数
    private BigDecimal activeRedpacketSum;// 激活红包金额

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCardEncrypt() {
        return idCardEncrypt;
    }

    public void setIdCardEncrypt(String idCardEncrypt) {
        this.idCardEncrypt = idCardEncrypt;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public boolean getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    public boolean getIsBindBankcard() {
        return isBindBankcard;
    }

    public void setIsBindBankcard(boolean isBindBankcard) {
        this.isBindBankcard = isBindBankcard;
    }

    public BigDecimal getRechargeSum() {
        return rechargeSum;
    }

    public void setRechargeSum(BigDecimal rechargeSum) {
        this.rechargeSum = rechargeSum;
    }

    public BigDecimal getInvestSum() {
        return investSum;
    }

    public void setInvestSum(BigDecimal investSum) {
        this.investSum = investSum;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public BigDecimal getActiveRedpacketSum() {
        return activeRedpacketSum;
    }

    public void setActiveRedpacketSum(BigDecimal activeRedpacketSum) {
        this.activeRedpacketSum = activeRedpacketSum;
    }
}
