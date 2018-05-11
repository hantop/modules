package com.fenlibao.p2p.model.xinwang.param.errorLog;

/**
 * Created by Administrator on 2017/8/16.
 */
public class ErrorLogParam {
    private String method;
    private String errorLog;

    public ErrorLogParam() {
    }

    public ErrorLogParam(String method, String errorLog) {
        this.method = method;
        this.errorLog = errorLog;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }
}
