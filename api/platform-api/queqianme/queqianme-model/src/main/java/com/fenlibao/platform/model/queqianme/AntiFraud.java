package com.fenlibao.platform.model.queqianme;

import com.fenlibao.platform.common.config.Config;
import com.fenlibao.platform.common.json.Jackson;
import com.fenlibao.platform.model.Response;
import org.aeonbits.owner.ConfigFactory;

import java.util.Map;

/**
 * 风控模型（用户基本信息）
 */
public class AntiFraud {

    private static Config config = ConfigFactory.create(Config.class);

    private int id;
    private int authStatus;//身份证是否实名(0:未实名 1:已实名)
    private int bankBadness;//身份证查询银行不良(0:本人 1：一度人脉  2:二度人脉)
    private int bankOverdue;//身份证查询银行逾期(0:本人 1：一度人脉  2:二度人脉)
    private int bankCheat;//身份证查询银行欺诈(0:本人 1：一度人脉  2:二度人脉)
    private int bankLost;//身份证查询银行失联(0:本人 1：一度人脉  2:二度人脉)
    private int bankRefuse;//身份证查询银行拒绝(0:本人 1：一度人脉  2:二度人脉)
    private int creditBadness;//身份证查询资信不良(0:本人 1：一度人脉  2:二度人脉)
    private int p2pBadness;//身份证查询小额贷不良
    private int p2pOverdue;//身份证查询小额贷逾期
    private int p2pCheat;//身份证查询小额贷欺诈
    private int p2pLost;//身份证查询小额贷失联
    private int p2pRefuse;//身份证查询小额贷拒绝
    private int telecomeOwe;//身份证查询电信欠费
    private int insuranceCheat;//身份证查询保险骗保
    private int courtBreak;//身份证查询法院失信人
    private int courtEnforced;//身份证查询法院被执行人
    private int phoneBankBadness;//手机查询银行不良
    private int phoneBankOverdue;//手机查询银行逾期
    private int phoneBankCheat;//手机查询银行欺诈
    private int phoneBankLost;//手机查询银行失联
    private int phoneBankRefuse;//手机查询银行拒绝
    private int phoneP2pBadness;//手机查询小额贷不良
    private int phoneP2pOverdue;//手机查询小额贷逾期
    private int phoneP2pCheat;//手机查询小额贷欺诈
    private int phoneP2pLost;//手机查询小额贷失联
    private int phoneP2pRefuse;//手机查询小额贷拒绝
    private int phoneTelecomeOwe;//手机查询电信欠费
    private int phoneInsuranceCheat;//手机查询保险骗保
    private int signBankBadness;//标识查询银行不良
    private int signBankOverdue;//标识查询银行逾期
    private int signBankCheat;//标识查询银行欺诈
    private int signBankLost;//标识查询银行失联
    private int signBankRefuse;//标识查询银行拒绝
    private int signP2pBadness;//标识查询小额贷不良
    private int signP2pOverdue;//标识查询小额贷逾期
    private int signP2pCheat;//标识查询小额贷欺诈
    private int signP2pLost;//标识查询小额贷失联
    private int signP2pRefuse;//标识查询小额贷拒绝
    private int signTelecomeOwe;//标识查询电信欠费
    private int signInsuranceCheat;//标识查询保险欠保




    public AntiFraud() {

    }

    public AntiFraud(String antiFraud) {
        Map<String,Object> antiFraudMap  = Jackson.getMapFormString(antiFraud);
        if(antiFraud!=null){
            this.authStatus = (Integer) antiFraudMap.get("authStatus");
            this.bankBadness = (Integer) antiFraudMap.get("bankBadness");
            this.bankOverdue = (Integer) antiFraudMap.get("bankOverdue");
            this.bankCheat = (Integer) antiFraudMap.get("bankCheat");
            this.bankLost = (Integer) antiFraudMap.get("bankLost");
            this.bankRefuse = (Integer) antiFraudMap.get("bankRefuse");
            this.creditBadness = (Integer) antiFraudMap.get("creditBadness");
            this.p2pBadness = (Integer) antiFraudMap.get("p2pBadness");
            this.p2pOverdue = (Integer) antiFraudMap.get("p2pOverdue");
            this.p2pCheat = (Integer) antiFraudMap.get("p2pCheat");
            this.p2pLost = (Integer) antiFraudMap.get("p2pLost");
            this.p2pRefuse = (Integer) antiFraudMap.get("p2pRefuse");
            this.telecomeOwe = (Integer) antiFraudMap.get("telecomeOwe");
            this.insuranceCheat = (Integer) antiFraudMap.get("insuranceCheat");
            this.courtBreak = (Integer) antiFraudMap.get("courtBreak");
            this.courtEnforced = (Integer) antiFraudMap.get("courtEnforced");
            this.phoneBankBadness = (Integer) antiFraudMap.get("phoneBankBadness");
            this.phoneBankOverdue = (Integer) antiFraudMap.get("phoneBankOverdue");
            this.phoneBankCheat = (Integer) antiFraudMap.get("phoneBankCheat");
            this.phoneBankLost = (Integer) antiFraudMap.get("phoneBankLost");
            this.phoneBankRefuse = (Integer) antiFraudMap.get("phoneBankRefuse");
            this.phoneP2pBadness = (Integer) antiFraudMap.get("phoneP2pBadness");
            this.phoneP2pOverdue = (Integer) antiFraudMap.get("phoneP2pOverdue");
            this.phoneP2pCheat = (Integer) antiFraudMap.get("phoneP2pCheat");
            this.phoneP2pLost = (Integer) antiFraudMap.get("phoneP2pLost");
            this.phoneP2pRefuse = (Integer) antiFraudMap.get("phoneP2pRefuse");
            this.phoneTelecomeOwe = (Integer) antiFraudMap.get("phoneTelecomeOwe");
            this.phoneInsuranceCheat = (Integer) antiFraudMap.get("phoneInsuranceCheat");
            this.signBankBadness = (Integer) antiFraudMap.get("signBankBadness");
            this.signBankOverdue = (Integer) antiFraudMap.get("signBankOverdue");
            this.signBankCheat = (Integer) antiFraudMap.get("signBankCheat");
            this.signBankLost = (Integer) antiFraudMap.get("signBankLost");
            this.signBankRefuse = (Integer) antiFraudMap.get("signBankRefuse");
            this.signP2pBadness = (Integer) antiFraudMap.get("signP2pBadness");
            this.signP2pOverdue = (Integer) antiFraudMap.get("signP2pOverdue");
            this.signP2pCheat = (Integer) antiFraudMap.get("signP2pCheat");
            this.signP2pLost = (Integer) antiFraudMap.get("signP2pLost");
            this.signP2pRefuse = (Integer) antiFraudMap.get("signP2pRefuse");
            this.signTelecomeOwe = (Integer) antiFraudMap.get("signTelecomeOwe");
            this.signInsuranceCheat = (Integer) antiFraudMap.get("signInsuranceCheat");

        }


    }




    public Response verifyAntiFraud() throws Exception {
       if (!verifyInfo(authStatus)
               ||!verifyInfo(bankBadness)
               ||!verifyInfo(bankOverdue)
               ||!verifyInfo(bankCheat)
               ||!verifyInfo(bankLost)
               ||!verifyInfo(bankRefuse)
               ||!verifyInfo(creditBadness)
               ||!verifyInfo(p2pBadness)
               ||!verifyInfo(p2pOverdue)
               ||!verifyInfo(p2pCheat)
               ||!verifyInfo(p2pLost)
               ||!verifyInfo(p2pRefuse)
               ||!verifyInfo(telecomeOwe)
               ||!verifyInfo(insuranceCheat)
               ||!verifyInfo(courtBreak)
               ||!verifyInfo(courtEnforced)
               ||!verifyInfo(phoneBankBadness)
               ||!verifyInfo(phoneBankOverdue)
               ||!verifyInfo(phoneBankCheat)
               ||!verifyInfo(phoneBankLost)
               ||!verifyInfo(phoneBankRefuse)
               ||!verifyInfo(phoneP2pBadness)
               ||!verifyInfo(phoneP2pOverdue)
               ||!verifyInfo(phoneP2pCheat)
               ||!verifyInfo(phoneP2pLost)
               ||!verifyInfo(phoneP2pRefuse)
               ||!verifyInfo(phoneTelecomeOwe)
               ||!verifyInfo(phoneInsuranceCheat)
               ||!verifyInfo(signBankBadness)
               ||!verifyInfo(signBankOverdue)
               ||!verifyInfo(signBankCheat)
               ||!verifyInfo(signBankLost)
               ||!verifyInfo(signBankRefuse)
               ||!verifyInfo(signP2pBadness)
               ||!verifyInfo(signP2pOverdue)
               ||!verifyInfo(signP2pCheat)
               ||!verifyInfo(signP2pLost)
               ||!verifyInfo(signP2pRefuse)
               ||!verifyInfo(signTelecomeOwe)
               ||!verifyInfo(signInsuranceCheat)
               ) {
            return Response.WRONGFUL_ANTI_FRAUD;
        }

       /* if(subjectNature>3){
            return Response.SYSTEM_ERROR_PARAMETERS;
        }*/


        return Response.RESPONSE_SUCCESS;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public int getBankBadness() {
        return bankBadness;
    }

    public void setBankBadness(int bankBadness) {
        this.bankBadness = bankBadness;
    }

    public int getBankCheat() {
        return bankCheat;
    }

    public void setBankCheat(int bankCheat) {
        this.bankCheat = bankCheat;
    }

    public int getBankLost() {
        return bankLost;
    }

    public void setBankLost(int bankLost) {
        this.bankLost = bankLost;
    }

    public int getBankOverdue() {
        return bankOverdue;
    }

    public void setBankOverdue(int bankOverdue) {
        this.bankOverdue = bankOverdue;
    }

    public int getBankRefuse() {
        return bankRefuse;
    }

    public void setBankRefuse(int bankRefuse) {
        this.bankRefuse = bankRefuse;
    }

    public int getCourtBreak() {
        return courtBreak;
    }

    public void setCourtBreak(int courtBreak) {
        this.courtBreak = courtBreak;
    }

    public int getCourtEnforced() {
        return courtEnforced;
    }

    public void setCourtEnforced(int courtEnforced) {
        this.courtEnforced = courtEnforced;
    }

    public int getCreditBadness() {
        return creditBadness;
    }

    public void setCreditBadness(int creditBadness) {
        this.creditBadness = creditBadness;
    }

    public int getInsuranceCheat() {
        return insuranceCheat;
    }

    public void setInsuranceCheat(int insuranceCheat) {
        this.insuranceCheat = insuranceCheat;
    }

    public int getP2pBadness() {
        return p2pBadness;
    }

    public void setP2pBadness(int p2pBadness) {
        this.p2pBadness = p2pBadness;
    }

    public int getP2pCheat() {
        return p2pCheat;
    }

    public void setP2pCheat(int p2pCheat) {
        this.p2pCheat = p2pCheat;
    }

    public int getP2pLost() {
        return p2pLost;
    }

    public void setP2pLost(int p2pLost) {
        this.p2pLost = p2pLost;
    }

    public int getP2pOverdue() {
        return p2pOverdue;
    }

    public void setP2pOverdue(int p2pOverdue) {
        this.p2pOverdue = p2pOverdue;
    }

    public int getP2pRefuse() {
        return p2pRefuse;
    }

    public void setP2pRefuse(int p2pRefuse) {
        this.p2pRefuse = p2pRefuse;
    }

    public int getPhoneBankBadness() {
        return phoneBankBadness;
    }

    public void setPhoneBankBadness(int phoneBankBadness) {
        this.phoneBankBadness = phoneBankBadness;
    }

    public int getPhoneBankCheat() {
        return phoneBankCheat;
    }

    public void setPhoneBankCheat(int phoneBankCheat) {
        this.phoneBankCheat = phoneBankCheat;
    }

    public int getPhoneBankLost() {
        return phoneBankLost;
    }

    public void setPhoneBankLost(int phoneBankLost) {
        this.phoneBankLost = phoneBankLost;
    }

    public int getPhoneBankOverdue() {
        return phoneBankOverdue;
    }

    public void setPhoneBankOverdue(int phoneBankOverdue) {
        this.phoneBankOverdue = phoneBankOverdue;
    }

    public int getPhoneBankRefuse() {
        return phoneBankRefuse;
    }

    public void setPhoneBankRefuse(int phoneBankRefuse) {
        this.phoneBankRefuse = phoneBankRefuse;
    }

    public int getPhoneInsuranceCheat() {
        return phoneInsuranceCheat;
    }

    public void setPhoneInsuranceCheat(int phoneInsuranceCheat) {
        this.phoneInsuranceCheat = phoneInsuranceCheat;
    }

    public int getPhoneP2pBadness() {
        return phoneP2pBadness;
    }

    public void setPhoneP2pBadness(int phoneP2pBadness) {
        this.phoneP2pBadness = phoneP2pBadness;
    }

    public int getPhoneP2pCheat() {
        return phoneP2pCheat;
    }

    public void setPhoneP2pCheat(int phoneP2pCheat) {
        this.phoneP2pCheat = phoneP2pCheat;
    }

    public int getPhoneP2pLost() {
        return phoneP2pLost;
    }

    public void setPhoneP2pLost(int phoneP2pLost) {
        this.phoneP2pLost = phoneP2pLost;
    }

    public int getPhoneP2pOverdue() {
        return phoneP2pOverdue;
    }

    public void setPhoneP2pOverdue(int phoneP2pOverdue) {
        this.phoneP2pOverdue = phoneP2pOverdue;
    }

    public int getPhoneP2pRefuse() {
        return phoneP2pRefuse;
    }

    public void setPhoneP2pRefuse(int phoneP2pRefuse) {
        this.phoneP2pRefuse = phoneP2pRefuse;
    }

    public int getPhoneTelecomeOwe() {
        return phoneTelecomeOwe;
    }

    public void setPhoneTelecomeOwe(int phoneTelecomeOwe) {
        this.phoneTelecomeOwe = phoneTelecomeOwe;
    }


    public int getSignBankBadness() {
        return signBankBadness;
    }

    public void setSignBankBadness(int signBankBadness) {
        this.signBankBadness = signBankBadness;
    }

    public int getSignBankCheat() {
        return signBankCheat;
    }

    public void setSignBankCheat(int signBankCheat) {
        this.signBankCheat = signBankCheat;
    }

    public int getSignBankLost() {
        return signBankLost;
    }

    public void setSignBankLost(int signBankLost) {
        this.signBankLost = signBankLost;
    }

    public int getSignBankOverdue() {
        return signBankOverdue;
    }

    public void setSignBankOverdue(int signBankOverdue) {
        this.signBankOverdue = signBankOverdue;
    }

    public int getSignBankRefuse() {
        return signBankRefuse;
    }

    public void setSignBankRefuse(int signBankRefuse) {
        this.signBankRefuse = signBankRefuse;
    }

    public int getSignInsuranceCheat() {
        return signInsuranceCheat;
    }

    public void setSignInsuranceCheat(int signInsuranceCheat) {
        this.signInsuranceCheat = signInsuranceCheat;
    }

    public int getSignP2pBadness() {
        return signP2pBadness;
    }

    public void setSignP2pBadness(int signP2pBadness) {
        this.signP2pBadness = signP2pBadness;
    }

    public int getSignP2pCheat() {
        return signP2pCheat;
    }

    public void setSignP2pCheat(int signP2pCheat) {
        this.signP2pCheat = signP2pCheat;
    }

    public int getSignP2pLost() {
        return signP2pLost;
    }

    public void setSignP2pLost(int signP2pLost) {
        this.signP2pLost = signP2pLost;
    }

    public int getSignP2pOverdue() {
        return signP2pOverdue;
    }

    public void setSignP2pOverdue(int signP2pOverdue) {
        this.signP2pOverdue = signP2pOverdue;
    }

    public int getSignP2pRefuse() {
        return signP2pRefuse;
    }

    public void setSignP2pRefuse(int signP2pRefuse) {
        this.signP2pRefuse = signP2pRefuse;
    }

    public int getSignTelecomeOwe() {
        return signTelecomeOwe;
    }

    public void setSignTelecomeOwe(int signTelecomeOwe) {
        this.signTelecomeOwe = signTelecomeOwe;
    }

    public int getTelecomeOwe() {
        return telecomeOwe;
    }

    public void setTelecomeOwe(int telecomeOwe) {
        this.telecomeOwe = telecomeOwe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private boolean verifyInfo(int info){
        if(info!=0&&info!=1&&info!=2){
            return false;
        }else {
            return true;
        }
    }

}
