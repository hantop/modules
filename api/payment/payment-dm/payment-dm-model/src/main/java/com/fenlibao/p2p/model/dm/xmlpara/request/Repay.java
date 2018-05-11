package com.fenlibao.p2p.model.dm.xmlpara.request;

/**
 * 还款收益的投资人(OGW00074)
 * Created by zcai on 2016/9/6.
 */
public class Repay {

    private String SUBSEQNO;//子流水号(用于对账，必须唯一，建议在开关加商户号)
    private String ACNO;//投资人账号
    private String ACNAME;//投资人账号户名
    private String INCOMEDATE;//该收益所属截止日期(YYYYMMDD)
    private String AMOUNT;//还款总金额(总金额=本次还款本金+本次还款收益+本次还款费用数值类型（15，2），整数15位，小数点后2位。例：3.00)
    /**
     投资人收款金额=（还款总金额-本次还款费用）；当投资人收款金额>0时，投资人会有资资人会有资金入账；
     本次还款费用>0时，则该还款费用清算到p2p公司在华兴开立的费用结算户中（p2p需告之银行该唯一指定的企业费用账户，配置到参数中）。
     数值类型（15，2），整数15位，小数点后2位。例：3.00
     */
    private String PRINCIPALAMT;//本次还款本金
    private String INCOMEAMT;//本次还款收益
    private String FEEAMT;//本次还款费用

    public String getSUBSEQNO() {
        return SUBSEQNO;
    }

    public void setSUBSEQNO(String SUBSEQNO) {
        this.SUBSEQNO = SUBSEQNO;
    }

    public String getACNO() {
        return ACNO;
    }

    public void setACNO(String ACNO) {
        this.ACNO = ACNO;
    }

    public String getACNAME() {
        return ACNAME;
    }

    public void setACNAME(String ACNAME) {
        this.ACNAME = ACNAME;
    }

    public String getINCOMEDATE() {
        return INCOMEDATE;
    }

    public void setINCOMEDATE(String INCOMEDATE) {
        this.INCOMEDATE = INCOMEDATE;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getPRINCIPALAMT() {
        return PRINCIPALAMT;
    }

    public void setPRINCIPALAMT(String PRINCIPALAMT) {
        this.PRINCIPALAMT = PRINCIPALAMT;
    }

    public String getINCOMEAMT() {
        return INCOMEAMT;
    }

    public void setINCOMEAMT(String INCOMEAMT) {
        this.INCOMEAMT = INCOMEAMT;
    }

    public String getFEEAMT() {
        return FEEAMT;
    }

    public void setFEEAMT(String FEEAMT) {
        this.FEEAMT = FEEAMT;
    }
}
