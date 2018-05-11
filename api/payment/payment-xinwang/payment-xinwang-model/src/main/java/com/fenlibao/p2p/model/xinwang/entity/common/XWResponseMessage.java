package com.fenlibao.p2p.model.xinwang.entity.common;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */
public class XWResponseMessage {
    private Integer id;
    private String batchNo;
    private String requestNo;
    private String requestParams;
    private String responseMsg;
    private Date updateTime;

    /**
     * batch多个requestNo时使用，因为根据batchNo进行upate会导致全表锁，故应使用主键或唯一索引update
     */
    private List<String> requestNos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getRequestNos() {
        return requestNos;
    }

    public void setRequestNos(List<String> requestNos) {
        this.requestNos = requestNos;
    }
}
