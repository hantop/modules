package com.fenlibao.p2p.model.dm.xmlpara.request;

/**
 * 投标优惠返回(OGW00054)
 * Created by zcai on 2016/9/7.
 */
public class Preferential {

    private String SUBSEQNO;//子流水号
    private String OLDREQSEQNO;//原投标流水号
    private String ACNO;//投资人账号
    private String ACNAME;//投资人账号户名
    private String AMOUNT;//优惠金额
    private String REMARK;//备注

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

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }
}
