package com.fenlibao.p2p.model.dm.xmlpara.response;

/**
 * 接收到华兴异步回调时，处理完后返回响应华兴
 * Created by zcai on 2016/9/9.
 */
public class ResponseToHXParams {

    public static final String RETURNCODE_SUCCESS = "000000";
    public static final String RETURNMSG_SUCCESS = "交易成功";

    private String RETURNCODE;//响应码(000000标识成功)
    private String RETURNMSG;//响应信息(交易成功)
    private String OLDREQSEQNO;//原开户交易流水号

    private String LOANNO; //借款编码F25

    private String REQSEQNO;//交易流水号(OGW00069)
    private String RESJNLNO;//银行交易流水号(OGW00069)

    public ResponseToHXParams success(String OLDREQSEQNO) {
        this.RETURNCODE = RETURNCODE_SUCCESS;
        this.RETURNMSG = RETURNMSG_SUCCESS;
        this.OLDREQSEQNO = OLDREQSEQNO;
        return this;
    }
    
    public ResponseToHXParams() {
    }
    
    public ResponseToHXParams(String LOANNO) {
    	this.RETURNCODE = RETURNCODE_SUCCESS;
    	this.RETURNMSG = RETURNMSG_SUCCESS;
    	this.LOANNO = LOANNO;
    }

    public String getRETURNCODE() {
        return RETURNCODE;
    }

    public void setRETURNCODE(String RETURNCODE) {
        this.RETURNCODE = RETURNCODE;
    }

    public String getRETURNMSG() {
        return RETURNMSG;
    }

    public void setRETURNMSG(String RETURNMSG) {
        this.RETURNMSG = RETURNMSG;
    }

    public String getOLDREQSEQNO() {
        return OLDREQSEQNO;
    }

    public void setOLDREQSEQNO(String OLDREQSEQNO) {
        this.OLDREQSEQNO = OLDREQSEQNO;
    }

    public String getREQSEQNO() {
        return REQSEQNO;
    }

    public void setREQSEQNO(String REQSEQNO) {
        this.REQSEQNO = REQSEQNO;
    }

    public String getRESJNLNO() {
        return RESJNLNO;
    }

    public void setRESJNLNO(String RESJNLNO) {
        this.RESJNLNO = RESJNLNO;
    }

	public String getLOANNO() {
		return LOANNO;
	}

	public void setLOANNO(String lOANNO) {
		LOANNO = lOANNO;
	}
}
