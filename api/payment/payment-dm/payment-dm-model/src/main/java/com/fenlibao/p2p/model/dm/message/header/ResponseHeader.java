package com.fenlibao.p2p.model.dm.message.header;

/**
 * 基础响应报文
 * Created by zcai on 2016/8/26.
 */
public class ResponseHeader extends HXHeader {

    private String transCode; //交易码,具体值以各自交易定义的为准
    private String serverFlow; //服务流水号
    private String serverDate; //服务日期
    private String serverTime; //服务时间
    private String status; //业务状态(0：受理成功;1：受理失败;2：受理中;3：受理超时，不确定;如查询交易返回状态为1时交易可置为失败。)
    private String errorCode; //错误代码
    private String errorMsg; //错误信息

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getServerFlow() {
        return serverFlow;
    }

    public void setServerFlow(String serverFlow) {
        this.serverFlow = serverFlow;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
