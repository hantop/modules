package com.fenlibao.p2p.model.dm.message.body;

/**
 * 响应报文body封装
 * Created by zcai on 2016/8/29.
 */
public class ResponseBody {

    private String TRANSCODE; //交易码
    private String MERCHANTID; //商户唯一标识
    private String BANKID; //银行标识,固定值：GHB
    private String XMLPARA; //加密数据

    public String getTRANSCODE() {
        return TRANSCODE;
    }

    public void setTRANSCODE(String TRANSCODE) {
        this.TRANSCODE = TRANSCODE;
    }

    public String getMERCHANTID() {
        return MERCHANTID;
    }

    public void setMERCHANTID(String MERCHANTID) {
        this.MERCHANTID = MERCHANTID;
    }

    public String getBANKID() {
        return BANKID;
    }

    public void setBANKID(String BANKID) {
        this.BANKID = BANKID;
    }

    public String getXMLPARA() {
        return XMLPARA;
    }

    public void setXMLPARA(String XMLPARA) {
        this.XMLPARA = XMLPARA;
    }
}
