package com.fenlibao.p2p.model.dm.xmlpara.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * 华兴响应报文业务参数
 * Created by zcai on 2016/9/9.
 */
public class RespBusinessParams {

    private String OTPSEQNO;//短信唯一标识(与客户收到的短信动态密码建立一一对应关系)
    private String OTPINDEX;//短信序号(只用于页面展示)

    private String OLDREQSEQNO;//原交易流水号
    private String ACNAME;//客户姓名
    private String IDTYPE;//证件类型(身份证：1010)
    private String IDNO;//证件号码(值格式化处理：4498**********2134)
    private String MOBILE;//手机号码(值格式化处理：159****1870)
    private String ACNO;//银行账号

    /*
    S 成功
    F 失败
    R 处理中（客户仍停留在页面操作，25分钟后仍是此状态可置交易为失败）
    N 未知（已提交后台，需再次发查询接口。）
    //OGW00046
    P 预授权成功（目前未到账，下一工作日到账，当天无需再进行查询，下一工作日上午6点再进行查询，状态会变成S，如状态不变则也无需再查询，可在下一工作日在对账文件中确认交易状态。）
    D 后台支付系统处理中（如果 ERRORMSG值为“ORDER_CREATED”，并超过25分钟未变，则可置交易是失败。其他情况商户需再次发查询接口。但2小时后状态仍未变的则可置为异常，无需再发起查询，后续在对账文件中确认交易状态或线下人工处理）
     */
    private String RETURN_STATUS;//交易状态
    private String ERRORMSG;//失败原因(失败才返回)
    private String RESJNLNO;//银行交易流水号(成功才返回)
    private String TRANSDT;//交易日期
    private String TRANSTM;//交易时间

    /*
    ORDER_COMPLETED：订单完成
    ORDER_PRE_AUTHING：订单预授权中（非实时到账，下一工作日到账） //（OGW00047非实时到账，约1小时到账）
     */
    private String ORDERSTATUS;//订单处理状态

    private BigDecimal ACCTBAL;//账户余额
    private BigDecimal AVAILABLEBAL;//可用余额
    private BigDecimal FROZBL;//冻结金额

    private String MERCHANTNAME;//商户名称
    /* OGW00050
    000000 余额充足
    540026  余额为零
    540009  余额不足
    540008  账户没有关联
    OGW001  账户与户名不匹配
     */
    private String RESFLAG;//校验结果

    //此两值不再传值，但标签仍在。
    private String HOSTDT;//银行支付日期
    private String HOSTJNLNO;//银行支付流水号

    private String LOANNO;//借款编号
    private String BWACNAME;//还款账号户名
    private String BWACNO;//还款账号
    private BigDecimal AMOUNT;//优惠总金额
    private Integer TOTALNUM;//优惠总笔数
    private List<com.fenlibao.p2p.model.dm.xmlpara.response.RSLIST> RSLIST;

    private BigDecimal ACMNGAMT;//账户管理费
    private BigDecimal GUARANTAMT;//账户管理费

    private String DFFLAG;//还款类型(1=正常还款2=垫付后，借款人还款

    //对账
    private String OPERFLAG;//0金额类对账
    private String CHECKDATE;//对账日期
    private String FILENAME;//文件名称(RETURN_STATUS =S 时返回。)
    private String FILECONTEXT;//文件内容(生成明细文件，然后压缩成ZIP文件，读取ZIP文件流进行BASE64后放到该字段值。)

    //主动撤标/流标
    private String CANCELREASON;//撤标原因

    public String getOTPSEQNO() {
        return OTPSEQNO;
    }

    public void setOTPSEQNO(String OTPSEQNO) {
        this.OTPSEQNO = OTPSEQNO;
    }

    public String getOTPINDEX() {
        return OTPINDEX;
    }

    public void setOTPINDEX(String OTPINDEX) {
        this.OTPINDEX = OTPINDEX;
    }

    public String getOLDREQSEQNO() {
        return OLDREQSEQNO;
    }

    public void setOLDREQSEQNO(String OLDREQSEQNO) {
        this.OLDREQSEQNO = OLDREQSEQNO;
    }

    public String getACNAME() {
        return ACNAME;
    }

    public void setACNAME(String ACNAME) {
        this.ACNAME = ACNAME;
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

    public String getACNO() {
        return ACNO;
    }

    public void setACNO(String ACNO) {
        this.ACNO = ACNO;
    }

    public String getRETURN_STATUS() {
        return RETURN_STATUS;
    }

    public void setRETURN_STATUS(String RETURN_STATUS) {
        this.RETURN_STATUS = RETURN_STATUS;
    }

    public String getERRORMSG() {
        return ERRORMSG;
    }

    public void setERRORMSG(String ERRORMSG) {
        this.ERRORMSG = ERRORMSG;
    }

    public String getRESJNLNO() {
        return RESJNLNO;
    }

    public void setRESJNLNO(String RESJNLNO) {
        this.RESJNLNO = RESJNLNO;
    }

    public String getTRANSDT() {
        return TRANSDT;
    }

    public void setTRANSDT(String TRANSDT) {
        this.TRANSDT = TRANSDT;
    }

    public String getTRANSTM() {
        return TRANSTM;
    }

    public void setTRANSTM(String TRANSTM) {
        this.TRANSTM = TRANSTM;
    }

    public String getORDERSTATUS() {
        return ORDERSTATUS;
    }

    public void setORDERSTATUS(String ORDERSTATUS) {
        this.ORDERSTATUS = ORDERSTATUS;
    }

    public BigDecimal getACCTBAL() {
        return ACCTBAL;
    }

    public void setACCTBAL(BigDecimal ACCTBAL) {
        this.ACCTBAL = ACCTBAL;
    }

    public BigDecimal getAVAILABLEBAL() {
        return AVAILABLEBAL;
    }

    public void setAVAILABLEBAL(BigDecimal AVAILABLEBAL) {
        this.AVAILABLEBAL = AVAILABLEBAL;
    }

    public BigDecimal getFROZBL() {
        return FROZBL;
    }

    public void setFROZBL(BigDecimal FROZBL) {
        this.FROZBL = FROZBL;
    }

    public String getMERCHANTNAME() {
        return MERCHANTNAME;
    }

    public void setMERCHANTNAME(String MERCHANTNAME) {
        this.MERCHANTNAME = MERCHANTNAME;
    }

    public String getRESFLAG() {
        return RESFLAG;
    }

    public void setRESFLAG(String RESFLAG) {
        this.RESFLAG = RESFLAG;
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

    public String getLOANNO() {
        return LOANNO;
    }

    public void setLOANNO(String LOANNO) {
        this.LOANNO = LOANNO;
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

    public BigDecimal getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(BigDecimal AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public Integer getTOTALNUM() {
        return TOTALNUM;
    }

    public void setTOTALNUM(Integer TOTALNUM) {
        this.TOTALNUM = TOTALNUM;
    }

    public List<com.fenlibao.p2p.model.dm.xmlpara.response.RSLIST> getRSLIST() {
        return RSLIST;
    }

    public void setRSLIST(List<com.fenlibao.p2p.model.dm.xmlpara.response.RSLIST> RSLIST) {
        this.RSLIST = RSLIST;
    }

    public BigDecimal getACMNGAMT() {
        return ACMNGAMT;
    }

    public void setACMNGAMT(BigDecimal ACMNGAMT) {
        this.ACMNGAMT = ACMNGAMT;
    }

    public BigDecimal getGUARANTAMT() {
        return GUARANTAMT;
    }

    public void setGUARANTAMT(BigDecimal GUARANTAMT) {
        this.GUARANTAMT = GUARANTAMT;
    }

    public String getDFFLAG() {
        return DFFLAG;
    }

    public void setDFFLAG(String DFFLAG) {
        this.DFFLAG = DFFLAG;
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

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }

    public String getFILECONTEXT() {
        return FILECONTEXT;
    }

    public void setFILECONTEXT(String FILECONTEXT) {
        this.FILECONTEXT = FILECONTEXT;
    }

    public String getCANCELREASON() {
        return CANCELREASON;
    }

    public void setCANCELREASON(String CANCELREASON) {
        this.CANCELREASON = CANCELREASON;
    }

    @Override
    public String toString() {
        return "RespBusinessParams{" +
                "OTPSEQNO='" + OTPSEQNO + '\'' +
                ", OTPINDEX='" + OTPINDEX + '\'' +
                ", OLDREQSEQNO='" + OLDREQSEQNO + '\'' +
                ", ACNAME='" + ACNAME + '\'' +
                ", IDTYPE='" + IDTYPE + '\'' +
                ", IDNO='" + IDNO + '\'' +
                ", MOBILE='" + MOBILE + '\'' +
                ", ACNO='" + ACNO + '\'' +
                ", RETURN_STATUS='" + RETURN_STATUS + '\'' +
                ", ERRORMSG='" + ERRORMSG + '\'' +
                ", RESJNLNO='" + RESJNLNO + '\'' +
                ", TRANSDT='" + TRANSDT + '\'' +
                ", TRANSTM='" + TRANSTM + '\'' +
                ", ORDERSTATUS='" + ORDERSTATUS + '\'' +
                ", ACCTBAL=" + ACCTBAL +
                ", AVAILABLEBAL=" + AVAILABLEBAL +
                ", FROZBL=" + FROZBL +
                ", MERCHANTNAME='" + MERCHANTNAME + '\'' +
                ", RESFLAG='" + RESFLAG + '\'' +
                ", HOSTDT='" + HOSTDT + '\'' +
                ", HOSTJNLNO='" + HOSTJNLNO + '\'' +
                ", LOANNO='" + LOANNO + '\'' +
                ", BWACNAME='" + BWACNAME + '\'' +
                ", BWACNO='" + BWACNO + '\'' +
                ", AMOUNT=" + AMOUNT +
                ", TOTALNUM=" + TOTALNUM +
                ", RSLIST=" + RSLIST +
                ", ACMNGAMT=" + ACMNGAMT +
                ", GUARANTAMT=" + GUARANTAMT +
                ", DFFLAG='" + DFFLAG + '\'' +
                ", OPERFLAG='" + OPERFLAG + '\'' +
                ", CHECKDATE='" + CHECKDATE + '\'' +
                ", FILENAME='" + FILENAME + '\'' +
                ", FILECONTEXT='" + FILECONTEXT + '\'' +
                ", CANCELREASON='" + CANCELREASON + '\'' +
                '}';
    }
}
