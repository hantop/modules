package com.fenlibao.p2p.model.entity.redenvelope;

import java.util.Date;

/**
 * 投资分享红包记录
 * Created by zcai on 2016/6/24.
 */
public class TenderShareEntity {

    private Integer id;
    private Integer tenderId; //投标记录ID
    private Integer userId; //用户ID
    private String code; //分享记录编码
    private Integer quantity; //分享数量
    private Integer remainingQty; //剩余数量
    private Date createTime; //创建时间
    private Date effectiveTime; //有效时间
    private Integer settingId;//对应分享设置的id
    private Integer itemType;//标 0；计划 1

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRemainingQty() {
        return remainingQty;
    }

    public void setRemainingQty(Integer remainingQty) {
        this.remainingQty = remainingQty;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Integer getTenderId() {
        return tenderId;
    }

    public void setTenderId(Integer tenderId) {
        this.tenderId = tenderId;
    }

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

}
