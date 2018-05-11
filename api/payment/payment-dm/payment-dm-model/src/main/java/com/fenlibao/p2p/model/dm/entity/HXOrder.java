package com.fenlibao.p2p.model.dm.entity;

import com.fenlibao.p2p.model.dm.enums.OrderSource;

import java.io.Serializable;
import java.util.Date;

/**
 * 华兴订单
 * Created by zcai on 2016/10/11.
 */
public class HXOrder implements Serializable {

    private Integer id;
    private Integer userId;
    private Integer typeCode;//类型编码
    private String flowNum;//流水号
    private String thirdPartyFlowNum;//第三方流水号
    private Integer status;//订单状态
    private Date createTime;//创建时间
    private Date completeTime;//完成时间
    private Integer businessId = 0;//业务订单ID(有的没有)

    private Integer bgUserId;//后台用户ID
    private Integer source;//订单来源(1=用户，2=pms后台，3=系统)
    private String parentFlowNum;//父级业务流水号

    /**
     * 订单来自用户
     * @param userId
     * @param typeCode
     * @param businessId 没有相应的业务订单为 null
     * @return
     */
    public HXOrder fromUser(Integer userId, Integer typeCode, Integer businessId) {
        this.userId = userId;
        this.typeCode = typeCode;
        this.source = OrderSource.USER.getCode();
        this.businessId = businessId == null ? 0 : businessId;
        return this;
    }

    /**
     * 订单来自PMS
     * @param pmsUserId pms系统用户ID
     * @param typeCode
     * @param businessId 没有相应的业务订单为 null
     * @return
     */
    public HXOrder fromPms(Integer pmsUserId, Integer typeCode, Integer businessId) {
        this.bgUserId = pmsUserId;
        this.typeCode = typeCode;
        this.source = OrderSource.PMS.getCode();
        this.businessId = businessId == null ? 0 : businessId;
        return this;
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

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public String getFlowNum() {
        return flowNum;
    }

    public void setFlowNum(String flowNum) {
        this.flowNum = flowNum;
    }

    public String getThirdPartyFlowNum() {
        return thirdPartyFlowNum;
    }

    public void setThirdPartyFlowNum(String thirdPartyFlowNum) {
        this.thirdPartyFlowNum = thirdPartyFlowNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getBgUserId() {
        return bgUserId;
    }

    public void setBgUserId(Integer bgUserId) {
        this.bgUserId = bgUserId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

	public String getParentFlowNum() {
		return parentFlowNum;
	}

	public void setParentFlowNum(String parentFlowNum) {
		this.parentFlowNum = parentFlowNum;
	}
}
