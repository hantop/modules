package com.fenlibao.p2p.model.dm.xmlpara.response;

import java.math.BigDecimal;

/**
 * 投标优惠查询返回结果（OGW00055）
 * <p>放款结果查询(OGW00066)</p>
 * Created by zcai on 2016/9/9.
 */
public class RSLIST {

    private String SUBSEQNO;//子流水号
    private String OLDREQSEQNO;//原投标流水号
    private String ACNO;//投资人账号
    private String ACNAME;//投资人账号户名
    private BigDecimal AMOUNT;//金额
    private String REMARK;//备注
    private String STATUS;//状态(L 待处理 R 正在处理 N 未明 F失败 S成功)
    private String ERRORMSG;//错误原因

    private String REQSEQNO;//投标交易流水号
    private String LOANNO;//借款编号
    private String HOSTDT;//银行处理日期
    private String HOSTJNLNO;//银行支付流水号

    private String INCOMEDATE;//该收益所属截止日期
    private String PRINCIPALAMT;//本次还款本金
    private String INCOMEAMT;//本次还款收益
    private String FEEAMT;//本次还款费用
    private String ERRMSG;//错误原因

    public String getSUBSEQNO() {
        return SUBSEQNO;
    }

    public void setSUBSEQNO(String SUBSEQNO) {
        this.SUBSEQNO = SUBSEQNO;
    }

    public String getOLDREQSEQNO() {
        return OLDREQSEQNO;
    }

    public void setOLDREQSEQNO(String OLDREQSEQNO) {
        this.OLDREQSEQNO = OLDREQSEQNO;
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

    public BigDecimal getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(BigDecimal AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getERRORMSG() {
        return ERRORMSG;
    }

    public void setERRORMSG(String ERRORMSG) {
        this.ERRORMSG = ERRORMSG;
    }

    public String getREQSEQNO() {
        return REQSEQNO;
    }

    public void setREQSEQNO(String REQSEQNO) {
        this.REQSEQNO = REQSEQNO;
    }

    public String getLOANNO() {
        return LOANNO;
    }

    public void setLOANNO(String LOANNO) {
        this.LOANNO = LOANNO;
    }

    public String getHOSTDT() {
        return HOSTDT;
    }

    public void setHOSTDT(String HOSTDT) {
        this.HOSTDT = HOSTDT;
    }

    public String getHOSTJNLNO() {
        return HOSTJNLNO;
    }

    public void setHOSTJNLNO(String HOSTJNLNO) {
        this.HOSTJNLNO = HOSTJNLNO;
    }

    public String getINCOMEDATE() {
        return INCOMEDATE;
    }

    public void setINCOMEDATE(String INCOMEDATE) {
        this.INCOMEDATE = INCOMEDATE;
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

    public String getERRMSG() {
        return ERRMSG;
    }

    public void setERRMSG(String ERRMSG) {
        this.ERRMSG = ERRMSG;
    }
}
