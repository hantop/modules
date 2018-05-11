package com.fenlibao.p2p.model.dm.xmlpara.request;

import com.fenlibao.p2p.model.dm.xmlpara.request.Borrower;
import com.fenlibao.p2p.model.dm.xmlpara.request.Preferential;
import com.fenlibao.p2p.model.dm.xmlpara.request.Repay;

import java.util.ArrayList;
import java.util.List;

/**
 * 华兴请求报文业务参数
 * <p>许多重复，所有接口业务参数字段，使用不同接口set相应的字段</p>
 * Created by zcai on 2016/9/2.
 */
public class ReqBusinessParams {

    private String MERCHANTID;//商户唯一标识
    private String MERCHANTNAME;//商户名称
    private String APPID;//应用标识,个人电脑:PC,手机：APP,微信：WX
    private String TTRANS;//交易类型

    private String ACNAME;//姓名
    private String IDTYPE;//证件类型(1010：居民身份证)
    private String IDNO;//证件号码(只支持身份证)
    private String MOBILE;//手机号码(11位手机号)
    private String EMAIL;//用户邮箱
    private String RETURNURL;//返回商户URL
    private String CUSTMNGRNO;//客户经理编号

    private String ACNO;//银行账号
    private String AMOUNT;//交易金额
    private String REMARK;//备注

    private String LOANNO;//借款编号

    private String DFFLAG;//还款类型(1=正常还款2=垫付后，借款人还款)
    private String OLDREQSEQNO;//垫付还款时必需/原交易流水号
    private String BWACNAME;//还款账号户名
    private String BWACNO;//还款账号

    private String TRANSDT;//原提现交易日期

    private String INVESTID;//标的编号
    private String INVESTOBJNAME;//标的名称
    private String INVESTOBJINFO;//标的简介
    private String MININVESTAMT;//最低投标金额
    private String MAXINVESTAMT;//最高投标金额
    private String INVESTOBJAMT;//总标的金额(各个借款人列表中的BWAMT总和)
    private String INVESTBEGINDATE;//招标开始日期(YYYYMMDD)
    private String INVESTENDDATE;//招标到期日期(YYYYMMDD)
    private String REPAYDATE;//还款日期(YYYYMMDD)
    private String YEARRATE;//年利率(最大值为：999.999999)
    private String INVESTRANGE;//期限(整型，天数，单位为天)
    private String RATESTYPE;//计息方式
    private String REPAYSTYPE;//还款方式
    private String INVESTOBJSTATE;//标的状态(0 正常 1 撤销)
    private String BWTOTALNUM;//借款人总数
    private String ZRFLAG;//是否为债券转让标的(0 否，1 是)
    private String REFLOANNO;//债券转让原标的(当ZRFLAG=1时必填)
    private String OLDREQSEQ;//原投标第三方交易流水号(当ZRFLAG=1时必填)
    private Borrower BWLIST;//借款人列表(目前只支持一个)

    private String OTPSEQNO;//动态密码唯一标识
    private String OTPNO;//动态密码

    private String CANCELREASON;//撤标原因

    private String OLDTTJNL;//原流标交易流水号

    private String ACMNGAMT;//账户管理费(数值类型（15，2），整数15位，小数点后2位。例：3.00)
    private String GUARANTAMT;//风险保证金(数值类型（15，2），整数15位，小数点后2位。例：3.00)

    private String TOTALNUM;//总笔数
    private List<Repay> REPAYLIST = new ArrayList<>();//还款收益人明细列表

    private String SUBSEQNO;//子流水号(OGW00075)

    private String OPERFLAG;//对账类型0 金额类对账(OGW00077)
    private String CHECKDATE;//对账日期(OGW00077)

    private String OLDREQNUMBER;//原标的编号(OGW00061)
    private String OLDREQNAME;//原标的名称(OGW00061)
    private String ACCNO;//转让人银行账号(OGW00061)
    private String CUSTNAME;//转让人名称(OGW00061)
    private String PREINCOME;//预计剩余收益(OGW00061)

    private String TRSTYPE;//操作类型(1：自动投标撤销 2：自动还款授权撤销 0：默认)(OGW00041)
    private String MOBILE_NO;//手机号(OGW00041)
    
    private String FEEAMT; //扣借款人的平台手续费

    private List<Preferential> FEEDBACKLIST;//投标优惠返回（OGW00054）

    public List<Preferential> getFEEDBACKLIST() {
        return FEEDBACKLIST;
    }

    public void setFEEDBACKLIST(List<Preferential> FEEDBACKLIST) {
        this.FEEDBACKLIST = FEEDBACKLIST;
    }

    public String getTRSTYPE() {
        return TRSTYPE;
    }

    public void setTRSTYPE(String TRSTYPE) {
        this.TRSTYPE = TRSTYPE;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getACCNO() {
        return ACCNO;
    }

    public void setACCNO(String ACCNO) {
        this.ACCNO = ACCNO;
    }

    public String getOLDREQNUMBER() {
        return OLDREQNUMBER;
    }

    public void setOLDREQNUMBER(String OLDREQNUMBER) {
        this.OLDREQNUMBER = OLDREQNUMBER;
    }

    public String getOLDREQNAME() {
        return OLDREQNAME;
    }

    public void setOLDREQNAME(String OLDREQNAME) {
        this.OLDREQNAME = OLDREQNAME;
    }

    public String getCUSTNAME() {
        return CUSTNAME;
    }

    public void setCUSTNAME(String CUSTNAME) {
        this.CUSTNAME = CUSTNAME;
    }

    public String getPREINCOME() {
        return PREINCOME;
    }

    public void setPREINCOME(String PREINCOME) {
        this.PREINCOME = PREINCOME;
    }

    public String getOPERFLAG() {
        return OPERFLAG;
    }

    public void setOPERFLAG(String OPERFLAG) {
        this.OPERFLAG = OPERFLAG;
    }

    public String getCHECKDATE() {
        return CHECKDATE;
    }

    public void setCHECKDATE(String CHECKDATE) {
        this.CHECKDATE = CHECKDATE;
    }

    public String getSUBSEQNO() {
        return SUBSEQNO;
    }

    public void setSUBSEQNO(String SUBSEQNO) {
        this.SUBSEQNO = SUBSEQNO;
    }

    public String getTOTALNUM() {
        return TOTALNUM;
    }

    public void setTOTALNUM(String TOTALNUM) {
        this.TOTALNUM = TOTALNUM;
    }

    public List<Repay> getREPAYLIST() {
        return REPAYLIST;
    }

    public void setREPAYLIST(List<Repay> REPAYLIST) {
        this.REPAYLIST = REPAYLIST;
    }

    public String getOLDTTJNL() {
        return OLDTTJNL;
    }

    public void setOLDTTJNL(String OLDTTJNL) {
        this.OLDTTJNL = OLDTTJNL;
    }

    public String getACMNGAMT() {
        return ACMNGAMT;
    }

    public void setACMNGAMT(String ACMNGAMT) {
        this.ACMNGAMT = ACMNGAMT;
    }

    public String getGUARANTAMT() {
        return GUARANTAMT;
    }

    public void setGUARANTAMT(String GUARANTAMT) {
        this.GUARANTAMT = GUARANTAMT;
    }

    public String getCANCELREASON() {
        return CANCELREASON;
    }

    public void setCANCELREASON(String CANCELREASON) {
        this.CANCELREASON = CANCELREASON;
    }

    public String getOTPSEQNO() {
        return OTPSEQNO;
    }

    public void setOTPSEQNO(String OTPSEQNO) {
        this.OTPSEQNO = OTPSEQNO;
    }

    public String getOTPNO() {
        return OTPNO;
    }

    public void setOTPNO(String OTPNO) {
        this.OTPNO = OTPNO;
    }

    public Borrower getBWLIST() {
        return BWLIST;
    }

    public void setBWLIST(Borrower BWLIST) {
        this.BWLIST = BWLIST;
    }

    public String getINVESTID() {
        return INVESTID;
    }

    public void setINVESTID(String INVESTID) {
        this.INVESTID = INVESTID;
    }

    public String getINVESTOBJNAME() {
        return INVESTOBJNAME;
    }

    public void setINVESTOBJNAME(String INVESTOBJNAME) {
        this.INVESTOBJNAME = INVESTOBJNAME;
    }

    public String getINVESTOBJINFO() {
        return INVESTOBJINFO;
    }

    public void setINVESTOBJINFO(String INVESTOBJINFO) {
        this.INVESTOBJINFO = INVESTOBJINFO;
    }

    public String getMININVESTAMT() {
        return MININVESTAMT;
    }

    public void setMININVESTAMT(String MININVESTAMT) {
        this.MININVESTAMT = MININVESTAMT;
    }

    public String getMAXINVESTAMT() {
        return MAXINVESTAMT;
    }

    public void setMAXINVESTAMT(String MAXINVESTAMT) {
        this.MAXINVESTAMT = MAXINVESTAMT;
    }

    public String getINVESTOBJAMT() {
        return INVESTOBJAMT;
    }

    public void setINVESTOBJAMT(String INVESTOBJAMT) {
        this.INVESTOBJAMT = INVESTOBJAMT;
    }

    public String getINVESTBEGINDATE() {
        return INVESTBEGINDATE;
    }

    public void setINVESTBEGINDATE(String INVESTBEGINDATE) {
        this.INVESTBEGINDATE = INVESTBEGINDATE;
    }

    public String getINVESTENDDATE() {
        return INVESTENDDATE;
    }

    public void setINVESTENDDATE(String INVESTENDDATE) {
        this.INVESTENDDATE = INVESTENDDATE;
    }

    public String getREPAYDATE() {
        return REPAYDATE;
    }

    public void setREPAYDATE(String REPAYDATE) {
        this.REPAYDATE = REPAYDATE;
    }

    public String getYEARRATE() {
        return YEARRATE;
    }

    public void setYEARRATE(String YEARRATE) {
        this.YEARRATE = YEARRATE;
    }

    public String getINVESTRANGE() {
        return INVESTRANGE;
    }

    public void setINVESTRANGE(String INVESTRANGE) {
        this.INVESTRANGE = INVESTRANGE;
    }

    public String getRATESTYPE() {
        return RATESTYPE;
    }

    public void setRATESTYPE(String RATESTYPE) {
        this.RATESTYPE = RATESTYPE;
    }

    public String getREPAYSTYPE() {
        return REPAYSTYPE;
    }

    public void setREPAYSTYPE(String REPAYSTYPE) {
        this.REPAYSTYPE = REPAYSTYPE;
    }

    public String getINVESTOBJSTATE() {
        return INVESTOBJSTATE;
    }

    public void setINVESTOBJSTATE(String INVESTOBJSTATE) {
        this.INVESTOBJSTATE = INVESTOBJSTATE;
    }

    public String getBWTOTALNUM() {
        return BWTOTALNUM;
    }

    public void setBWTOTALNUM(String BWTOTALNUM) {
        this.BWTOTALNUM = BWTOTALNUM;
    }

    public String getZRFLAG() {
        return ZRFLAG;
    }

    public void setZRFLAG(String ZRFLAG) {
        this.ZRFLAG = ZRFLAG;
    }

    public String getREFLOANNO() {
        return REFLOANNO;
    }

    public void setREFLOANNO(String REFLOANNO) {
        this.REFLOANNO = REFLOANNO;
    }

    public String getOLDREQSEQ() {
        return OLDREQSEQ;
    }

    public void setOLDREQSEQ(String OLDREQSEQ) {
        this.OLDREQSEQ = OLDREQSEQ;
    }

    public String getTRANSDT() {
        return TRANSDT;
    }

    public void setTRANSDT(String TRANSDT) {
        this.TRANSDT = TRANSDT;
    }

    public String getOLDREQSEQNO() {
        return OLDREQSEQNO;
    }

    public void setOLDREQSEQNO(String OLDREQSEQNO) {
        this.OLDREQSEQNO = OLDREQSEQNO;
    }

    public String getBWACNAME() {
        return BWACNAME;
    }

    public void setBWACNAME(String BWACNAME) {
        this.BWACNAME = BWACNAME;
    }

    public String getBWACNO() {
        return BWACNO;
    }

    public void setBWACNO(String BWACNO) {
        this.BWACNO = BWACNO;
    }

    public String getDFFLAG() {
        return DFFLAG;
    }

    public void setDFFLAG(String DFFLAG) {
        this.DFFLAG = DFFLAG;
    }

    public String getLOANNO() {
        return LOANNO;
    }

    public void setLOANNO(String LOANNO) {
        this.LOANNO = LOANNO;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getMERCHANTID() {
        return MERCHANTID;
    }

    public void setMERCHANTID(String MERCHANTID) {
        this.MERCHANTID = MERCHANTID;
    }

    public String getAPPID() {
        return APPID;
    }

    public void setAPPID(String APPID) {
        this.APPID = APPID;
    }

    public String getTTRANS() {
        return TTRANS;
    }

    public void setTTRANS(String TTRANS) {
        this.TTRANS = TTRANS;
    }

    public String getMERCHANTNAME() {
        return MERCHANTNAME;
    }

    public void setMERCHANTNAME(String MERCHANTNAME) {
        this.MERCHANTNAME = MERCHANTNAME;
    }

    public String getACNAME() {
        return ACNAME;
    }

    public void setACNAME(String ACNAME) {
        this.ACNAME = ACNAME;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getIDTYPE() {
        return IDTYPE;
    }

    public void setIDTYPE(String IDTYPE) {
        this.IDTYPE = IDTYPE;
    }

    public String getIDNO() {
        return IDNO;
    }

    public void setIDNO(String IDNO) {
        this.IDNO = IDNO;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getRETURNURL() {
        return RETURNURL;
    }

    public void setRETURNURL(String RETURNURL) {
        this.RETURNURL = RETURNURL;
    }

    public String getCUSTMNGRNO() {
        return CUSTMNGRNO;
    }

    public void setCUSTMNGRNO(String CUSTMNGRNO) {
        this.CUSTMNGRNO = CUSTMNGRNO;
    }

    public String getACNO() {
        return ACNO;
    }

    public void setACNO(String ACNO) {
        this.ACNO = ACNO;
    }

	public String getFEEAMT() {
		return FEEAMT;
	}

	public void setFEEAMT(String fEEAMT) {
		FEEAMT = fEEAMT;
	}
}
