package com.fenlibao.p2p.model.dm.xmlpara.request;

/**
 * 借款人
 * Created by zcai on 2016/9/6.
 */
public class Borrower {

    private String BWACNAME;//借款人姓名
    private String BWIDTYPE;//借款人证件类型(身份证：1010)
    private String BWIDNO;//借款人证件号码(18位身份证)
    private String BWACNO;//借款人账号
    private String BWACBANKID;//借款人账号所属行号(12位联行号，12位数字)
    private String BWACBANKNAME;//借款人账号所属行名
    private String BWAMT;//借款人金额
    private String MORTGAGEID;//借款人抵押品编号
    private String MORTGAGEINFO;//借款人抵押品简单描述
    private String CHECKDATE;//借款人审批通过日期
    private String REMARK;//备注

    public String getBWACNAME() {
        return BWACNAME;
    }

    public void setBWACNAME(String BWACNAME) {
        this.BWACNAME = BWACNAME;
    }

    public String getBWIDTYPE() {
        return BWIDTYPE;
    }

    public void setBWIDTYPE(String BWIDTYPE) {
        this.BWIDTYPE = BWIDTYPE;
    }

    public String getBWIDNO() {
        return BWIDNO;
    }

    public void setBWIDNO(String BWIDNO) {
        this.BWIDNO = BWIDNO;
    }

    public String getBWACNO() {
        return BWACNO;
    }

    public void setBWACNO(String BWACNO) {
        this.BWACNO = BWACNO;
    }

    public String getBWACBANKID() {
        return BWACBANKID;
    }

    public void setBWACBANKID(String BWACBANKID) {
        this.BWACBANKID = BWACBANKID;
    }

    public String getBWACBANKNAME() {
        return BWACBANKNAME;
    }

    public void setBWACBANKNAME(String BWACBANKNAME) {
        this.BWACBANKNAME = BWACBANKNAME;
    }

    public String getBWAMT() {
        return BWAMT;
    }

    public void setBWAMT(String BWAMT) {
        this.BWAMT = BWAMT;
    }

    public String getMORTGAGEID() {
        return MORTGAGEID;
    }

    public void setMORTGAGEID(String MORTGAGEID) {
        this.MORTGAGEID = MORTGAGEID;
    }

    public String getMORTGAGEINFO() {
        return MORTGAGEINFO;
    }

    public void setMORTGAGEINFO(String MORTGAGEINFO) {
        this.MORTGAGEINFO = MORTGAGEINFO;
    }

    public String getCHECKDATE() {
        return CHECKDATE;
    }

    public void setCHECKDATE(String CHECKDATE) {
        this.CHECKDATE = CHECKDATE;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }
}
