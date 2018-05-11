package com.fenlibao.p2p.model.xinwang.entity.common;

import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */
public class XWRequest {
    private Integer id;
    private String interfaceName;
    private String batchNo;
    private String requestNo;
    private Integer orderId;
    /**
     * 存管用户编号
     */
    private String platformUserNo;
    /**
     * 平台用户id t6110.F01
     */
    private Integer userId;
    private Date requestTime;
    private XWRequestState state;
    private Date updateTime;
    /**
     * batch多个requestNo时使用，因为根据batchNo进行upate会导致全表锁，故应使用主键或唯一索引update
     */
    private List<String> requestNos;

    public XWRequest() {
    }

    public XWRequest(String requestNo) {
        this.requestNo = requestNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getPlatformUserNo() {
        return platformUserNo;
    }

    public void setPlatformUserNo(String platformUserNo) {
        this.platformUserNo = platformUserNo;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public XWRequestState getState() {
        return state;
    }

    public void setState(XWRequestState state) {
        this.state = state;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<String> getRequestNos() {
        return requestNos;
    }

    public void setRequestNos(List<String> requestNos) {
        this.requestNos = requestNos;
    }
}
