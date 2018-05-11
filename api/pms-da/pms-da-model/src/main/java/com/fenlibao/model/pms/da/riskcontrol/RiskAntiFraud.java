package com.fenlibao.model.pms.da.riskcontrol;

import java.util.Date;

/**
 * Created by Administrator on 2017/12/27.
 */
public class RiskAntiFraud {

    /**
     *借款标ID,参考flb.t_consume_bidinfo.id
     */
    private String bidInfoId;
    /**
     *创建时间
     */
    private Date createTime;

    /**
     * 是否通过身份信息实名验证
     **/
    private String authStatus;
    /**
     * 通过身份证查询银行不良
     **/
    private String idQueryBankBadness;
    /**
     * 通过身份证查询银行短时逾期
     **/
    private String idQueryBankOverdue;
    /**
     * 通过身份证查询银行欺诈
     **/
    private String idQueryBankCheat;
    /**
     * 通过身份证查询银行失联
     **/
    private String idQueryBankLost;
    /**
     * 通过身份证查询银行拒绝
     **/
    private String idQueryBankRefuse;
    /**
     * 通过身份证查询资信不良
     **/
    private String idQueryCreditBadness;
    /**
     * 通过身份证查询小贷或P2P不良
     **/
    private String idQueryP2PBadness;
    /**
     * 通过身份证查询小贷或P2P短时逾期
     **/
    private String idQueryP2POverdue;
    /**
     * 通过身份证查询小贷或P2P欺诈
     **/
    private String idQueryP2PCheat;
    /**
     * 通过身份证查询小贷或P2P失联
     **/
    private String idQueryP2PLost;
    /**
     * 通过身份证查询小贷或P2P拒绝
     **/
    private String idQueryP2PRefuse;
    /**
     * 通过身份证查询电信欠费
     **/
    private String idQueryTelecomeOwe;
    /**
     * 通过身份证查询保险骗保
     **/
    private String idQueryInsuranceCheat;
    /**
     * 通过身份证查询法院失信人
     **/
    private String idQueryCourtBreak;
    /**
     * 通过身份证查询法院被执行人
     **/
    private String idQueryCourtEnforced;
    /**
     * 通过手机号查询银行不良
     **/
    private String phoneQueryBankBadness;
    /**
     * 通过手机号查询银行短时逾期
     **/
    private String phoneQueryBankOverdue;
    /**
     * 通过手机号查询银行欺诈
     **/
    private String phoneQueryBankCheat;
    /**
     * 通过手机号查询银行失联
     **/
    private String phoneQueryBankLost;
    /**
     * 通过手机号查询银行拒绝
     **/
    private String phoneQueryBankRefuse;
    /**
     * 通过手机号查询小贷或P2P不良
     **/
    private String phoneQueryP2PBadness;
    /**
     * 通过手机号查询小贷或P2P短时逾期
     **/
    private String phoneQueryP2POverdue;
    /**
     * 通过手机号查询小贷或P2P欺诈
     **/
    private String phoneQueryP2PCheat;
    /**
     * 通过手机号查询小贷或P2P失联
     **/
    private String phoneQueryP2PLost;
    /**
     * 通过手机号查询小贷或P2P拒绝
     **/
    private String phoneQueryP2PRefuse;
    /**
     * 通过手机号查询电信欠费
     **/
    private String phoneQueryTelecomeOwe;
    /**
     * 通过手机号查询保险骗保
     **/
    private String phoneQueryP2POwe;
    /**
     * 通过标识查询银行不良
     **/
    private String signQueryBankBadness;
    /**
     * 通过标识查询银行短时逾期
     **/
    private String signQueryBankOverdue;
    /**
     * 通过标识查询银行欺诈
     **/
    private String signQueryBankCheat;
    /**
     * 通过标识查询银行失联
     **/
    private String signQueryBankLost;
    /**
     * 通过标识查询银行拒绝
     **/
    private String signQueryBankRefuse;
    /**
     * 通过标识查询小贷或P2P不良
     **/
    private String signQueryP2PBadness;
    /**
     * 通过标识查询小贷或P2P短时逾期
     **/
    private String signQueryP2POverdue;
    /**
     * 通过标识查询小贷或P2P欺诈
     **/
    private String signQueryP2PCheat;
    /**
     * 通过标识查询小贷或P2P失联
     **/
    private String signQueryP2PLost;
    /**
     * 通过标识查询小贷或P2P拒绝
     **/
    private String signQueryP2PRefuse;
    /**
     * 通过标识查询电信欠费
     **/
    private String signQueryTelecomeOwe;
    /**
     * 通过标识查询保险骗保
     **/
    private String sighQueryInsuranceCheat;





    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getIdQueryBankBadness() {
        return idQueryBankBadness;
    }

    public void setIdQueryBankBadness(String idQueryBankBadness) {
        this.idQueryBankBadness = idQueryBankBadness;
    }

    public String getIdQueryBankOverdue() {
        return idQueryBankOverdue;
    }

    public void setIdQueryBankOverdue(String idQueryBankOverdue) {
        this.idQueryBankOverdue = idQueryBankOverdue;
    }

    public String getIdQueryBankCheat() {
        return idQueryBankCheat;
    }

    public void setIdQueryBankCheat(String idQueryBankCheat) {
        this.idQueryBankCheat = idQueryBankCheat;
    }

    public String getIdQueryBankLost() {
        return idQueryBankLost;
    }

    public void setIdQueryBankLost(String idQueryBankLost) {
        this.idQueryBankLost = idQueryBankLost;
    }

    public String getIdQueryBankRefuse() {
        return idQueryBankRefuse;
    }

    public void setIdQueryBankRefuse(String idQueryBankRefuse) {
        this.idQueryBankRefuse = idQueryBankRefuse;
    }

    public String getIdQueryCreditBadness() {
        return idQueryCreditBadness;
    }

    public void setIdQueryCreditBadness(String idQueryCreditBadness) {
        this.idQueryCreditBadness = idQueryCreditBadness;
    }

    public String getIdQueryP2PBadness() {
        return idQueryP2PBadness;
    }

    public void setIdQueryP2PBadness(String idQueryP2PBadness) {
        this.idQueryP2PBadness = idQueryP2PBadness;
    }

    public String getIdQueryP2POverdue() {
        return idQueryP2POverdue;
    }

    public void setIdQueryP2POverdue(String idQueryP2POverdue) {
        this.idQueryP2POverdue = idQueryP2POverdue;
    }

    public String getIdQueryP2PCheat() {
        return idQueryP2PCheat;
    }

    public void setIdQueryP2PCheat(String idQueryP2PCheat) {
        this.idQueryP2PCheat = idQueryP2PCheat;
    }

    public String getIdQueryP2PLost() {
        return idQueryP2PLost;
    }

    public void setIdQueryP2PLost(String idQueryP2PLost) {
        this.idQueryP2PLost = idQueryP2PLost;
    }

    public String getIdQueryP2PRefuse() {
        return idQueryP2PRefuse;
    }

    public void setIdQueryP2PRefuse(String idQueryP2PRefuse) {
        this.idQueryP2PRefuse = idQueryP2PRefuse;
    }

    public String getIdQueryTelecomeOwe() {
        return idQueryTelecomeOwe;
    }

    public void setIdQueryTelecomeOwe(String idQueryTelecomeOwe) {
        this.idQueryTelecomeOwe = idQueryTelecomeOwe;
    }

    public String getIdQueryInsuranceCheat() {
        return idQueryInsuranceCheat;
    }

    public void setIdQueryInsuranceCheat(String idQueryInsuranceCheat) {
        this.idQueryInsuranceCheat = idQueryInsuranceCheat;
    }

    public String getIdQueryCourtBreak() {
        return idQueryCourtBreak;
    }

    public void setIdQueryCourtBreak(String idQueryCourtBreak) {
        this.idQueryCourtBreak = idQueryCourtBreak;
    }

    public String getIdQueryCourtEnforced() {
        return idQueryCourtEnforced;
    }

    public void setIdQueryCourtEnforced(String idQueryCourtEnforced) {
        this.idQueryCourtEnforced = idQueryCourtEnforced;
    }

    public String getPhoneQueryBankBadness() {
        return phoneQueryBankBadness;
    }

    public void setPhoneQueryBankBadness(String phoneQueryBankBadness) {
        this.phoneQueryBankBadness = phoneQueryBankBadness;
    }

    public String getPhoneQueryBankOverdue() {
        return phoneQueryBankOverdue;
    }

    public void setPhoneQueryBankOverdue(String phoneQueryBankOverdue) {
        this.phoneQueryBankOverdue = phoneQueryBankOverdue;
    }

    public String getPhoneQueryBankCheat() {
        return phoneQueryBankCheat;
    }

    public void setPhoneQueryBankCheat(String phoneQueryBankCheat) {
        this.phoneQueryBankCheat = phoneQueryBankCheat;
    }

    public String getPhoneQueryBankLost() {
        return phoneQueryBankLost;
    }

    public void setPhoneQueryBankLost(String phoneQueryBankLost) {
        this.phoneQueryBankLost = phoneQueryBankLost;
    }

    public String getPhoneQueryBankRefuse() {
        return phoneQueryBankRefuse;
    }

    public void setPhoneQueryBankRefuse(String phoneQueryBankRefuse) {
        this.phoneQueryBankRefuse = phoneQueryBankRefuse;
    }

    public String getPhoneQueryP2PBadness() {
        return phoneQueryP2PBadness;
    }

    public void setPhoneQueryP2PBadness(String phoneQueryP2PBadness) {
        this.phoneQueryP2PBadness = phoneQueryP2PBadness;
    }

    public String getPhoneQueryP2POverdue() {
        return phoneQueryP2POverdue;
    }

    public void setPhoneQueryP2POverdue(String phoneQueryP2POverdue) {
        this.phoneQueryP2POverdue = phoneQueryP2POverdue;
    }

    public String getPhoneQueryP2PCheat() {
        return phoneQueryP2PCheat;
    }

    public void setPhoneQueryP2PCheat(String phoneQueryP2PCheat) {
        this.phoneQueryP2PCheat = phoneQueryP2PCheat;
    }

    public String getPhoneQueryP2PLost() {
        return phoneQueryP2PLost;
    }

    public void setPhoneQueryP2PLost(String phoneQueryP2PLost) {
        this.phoneQueryP2PLost = phoneQueryP2PLost;
    }

    public String getPhoneQueryP2PRefuse() {
        return phoneQueryP2PRefuse;
    }

    public void setPhoneQueryP2PRefuse(String phoneQueryP2PRefuse) {
        this.phoneQueryP2PRefuse = phoneQueryP2PRefuse;
    }

    public String getPhoneQueryTelecomeOwe() {
        return phoneQueryTelecomeOwe;
    }

    public void setPhoneQueryTelecomeOwe(String phoneQueryTelecomeOwe) {
        this.phoneQueryTelecomeOwe = phoneQueryTelecomeOwe;
    }

    public String getPhoneQueryP2POwe() {
        return phoneQueryP2POwe;
    }

    public void setPhoneQueryP2POwe(String phoneQueryP2POwe) {
        this.phoneQueryP2POwe = phoneQueryP2POwe;
    }

    public String getSignQueryBankBadness() {
        return signQueryBankBadness;
    }

    public void setSignQueryBankBadness(String signQueryBankBadness) {
        this.signQueryBankBadness = signQueryBankBadness;
    }

    public String getSignQueryBankOverdue() {
        return signQueryBankOverdue;
    }

    public void setSignQueryBankOverdue(String signQueryBankOverdue) {
        this.signQueryBankOverdue = signQueryBankOverdue;
    }

    public String getSignQueryBankCheat() {
        return signQueryBankCheat;
    }

    public void setSignQueryBankCheat(String signQueryBankCheat) {
        this.signQueryBankCheat = signQueryBankCheat;
    }

    public String getSignQueryBankLost() {
        return signQueryBankLost;
    }

    public void setSignQueryBankLost(String signQueryBankLost) {
        this.signQueryBankLost = signQueryBankLost;
    }

    public String getSignQueryBankRefuse() {
        return signQueryBankRefuse;
    }

    public void setSignQueryBankRefuse(String signQueryBankRefuse) {
        this.signQueryBankRefuse = signQueryBankRefuse;
    }

    public String getSignQueryP2PBadness() {
        return signQueryP2PBadness;
    }

    public void setSignQueryP2PBadness(String signQueryP2PBadness) {
        this.signQueryP2PBadness = signQueryP2PBadness;
    }

    public String getSignQueryP2POverdue() {
        return signQueryP2POverdue;
    }

    public void setSignQueryP2POverdue(String signQueryP2POverdue) {
        this.signQueryP2POverdue = signQueryP2POverdue;
    }

    public String getSignQueryP2PCheat() {
        return signQueryP2PCheat;
    }

    public void setSignQueryP2PCheat(String signQueryP2PCheat) {
        this.signQueryP2PCheat = signQueryP2PCheat;
    }

    public String getSignQueryP2PLost() {
        return signQueryP2PLost;
    }

    public void setSignQueryP2PLost(String signQueryP2PLost) {
        this.signQueryP2PLost = signQueryP2PLost;
    }

    public String getSignQueryP2PRefuse() {
        return signQueryP2PRefuse;
    }

    public void setSignQueryP2PRefuse(String signQueryP2PRefuse) {
        this.signQueryP2PRefuse = signQueryP2PRefuse;
    }

    public String getSignQueryTelecomeOwe() {
        return signQueryTelecomeOwe;
    }

    public void setSignQueryTelecomeOwe(String signQueryTelecomeOwe) {
        this.signQueryTelecomeOwe = signQueryTelecomeOwe;
    }

    public String getSighQueryInsuranceCheat() {
        return sighQueryInsuranceCheat;
    }

    public void setSighQueryInsuranceCheat(String sighQueryInsuranceCheat) {
        this.sighQueryInsuranceCheat = sighQueryInsuranceCheat;
    }

    public String getBidInfoId() {
        return bidInfoId;
    }

    public void setBidInfoId(String bidInfoId) {
        this.bidInfoId = bidInfoId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
