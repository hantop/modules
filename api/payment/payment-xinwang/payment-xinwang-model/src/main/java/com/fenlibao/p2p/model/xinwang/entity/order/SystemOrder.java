package com.fenlibao.p2p.model.xinwang.entity.order;

import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.enums.order.Source;

import java.sql.Timestamp;
import java.util.Date;

/**
 * s61.t6501
 */
public class SystemOrder {

    /**
     * 主键
     */
    public int id;
    /**
     * 类型编码
     */
    public int typeCode;
    /**
     * 状态,DTJ:待提交;YTJ:已提交;DQR:待确认;CG:成功;SB:失败;MJL:在连连没记录;WAITING等待支付(连连);REFUND退款(连连)
     */
    public XWOrderStatus orderStatus;
    /**
     * 创建时间
     */
    public Date createTime;
    /**
     * 提交时间
     */
    public Date commitTime;
    /**
     * 完成时间
     */
    public Date completeTime;
    /**
     * 订单来源,XT:系统;YH:用户;HT:后台;
     */
    public Source source;
    /**
     * 用户ID,参考T6110.F01
     */
    public int userId;
    /**
     * 后台帐号ID,参考T7110.F01
     */
    public int backstageId;
    /**
     * 流水号
     */
    public String flowNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public XWOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(XWOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBackstageId() {
        return backstageId;
    }

    public void setBackstageId(int backstageId) {
        this.backstageId = backstageId;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }
}
