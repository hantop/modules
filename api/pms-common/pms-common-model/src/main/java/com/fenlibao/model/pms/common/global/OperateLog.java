package com.fenlibao.model.pms.common.global;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by chen on 2018/3/2.
 */
public class OperateLog implements Serializable {
    private Integer id;
    private String username; //用户账号
    private String ip; //ip
    private Integer operation;//'操作：201解绑银行卡-审核,301放款,302流标，303还款，304提前还款，305担保代偿，306提前担保代偿，401奖励发送，501营销新增，502营销编辑，601更改手机号，701代充值，702代充值审核通过，703代充值审核不通过，801平台账户充值，802平台账户提现，803平台账户划拨，804平台账户解绑卡，805平台账户绑定卡'
    private String requestMessage; //请求报文
    private Integer status; //操作状态0:失败 1:成功
    private String remarks; //备注

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
