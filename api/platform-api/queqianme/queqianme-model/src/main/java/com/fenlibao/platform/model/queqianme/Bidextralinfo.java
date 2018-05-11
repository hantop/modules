package com.fenlibao.platform.model.queqianme;

import com.fenlibao.platform.model.Response;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiao on 2016/10/24.
 */
public class Bidextralinfo {

    private String loanSerialNum;
    private String serialNum;
    private String type;
    private String filePath;

    public Bidextralinfo() {
    }

    public Bidextralinfo(String loanSerialNum, String serialNum, String type) {
        this.loanSerialNum = loanSerialNum;
        this.serialNum = serialNum;
        this.type = type;
    }

    public Bidextralinfo(String loanSerialNum, String serialNum, String type, String filePath) {
        this.loanSerialNum = loanSerialNum;
        this.serialNum = serialNum;
        this.type = type;
        this.filePath = filePath;
    }

    /**
     * 校验数据
     */
    public Response verify() {
        if (StringUtils.isBlank(loanSerialNum) || StringUtils.isBlank(serialNum) || StringUtils.isBlank(type)) {
            return Response.SYSTEM_EMPTY_PARAMETERS;
        }
        return Response.RESPONSE_SUCCESS;
    }

    public String getLoanSerialNum() {
        return loanSerialNum;
    }

    public void setLoanSerialNum(String loanSerialNum) {
        this.loanSerialNum = loanSerialNum;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
