package com.fenlibao.p2p.model.dm.message.body;

/**
 * 请求报文body封装
 * Created by zcai on 2016/8/29.
 */
public class RequestBody {

    private String TRANSCODE; //交易码
    private String XMLPARA; //数据加密域

    public RequestBody() {}

    /**
     * @param TRANSCODE
     * @param XMLPARA
     */
    public RequestBody(String TRANSCODE, String XMLPARA) {
        this.TRANSCODE = TRANSCODE;
        this.XMLPARA = XMLPARA;
    }

    public String getXMLPARA() {
        return XMLPARA;
    }

    public void setXMLPARA(String XMLPARA) {
        this.XMLPARA = XMLPARA;
    }

    public String getTRANSCODE() {
        return TRANSCODE;
    }

    public void setTRANSCODE(String TRANSCODE) {
        this.TRANSCODE = TRANSCODE;
    }
}
