package com.fenlibao.p2p.model.xinwang.entity.coupon;

import java.math.BigDecimal;
import java.util.Date;

/**
 * flb.t_user_coupon
 */
public class XWUserCoupon {
   private Integer id;
   private Integer userId;
   private Integer couponId;
   private Integer activityId;
   private Integer tenderId;
   private Date validTime;
   private Integer couponStatus;
   private Integer grantId;
   private Integer grantStatus;
   private Date createTime;
   private Date updateTime;
   private BigDecimal scope;

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

   public Integer getCouponId() {
      return couponId;
   }

   public void setCouponId(Integer couponId) {
      this.couponId = couponId;
   }

   public Integer getActivityId() {
      return activityId;
   }

   public void setActivityId(Integer activityId) {
      this.activityId = activityId;
   }

   public Integer getTenderId() {
      return tenderId;
   }

   public void setTenderId(Integer tenderId) {
      this.tenderId = tenderId;
   }

   public Date getValidTime() {
      return validTime;
   }

   public void setValidTime(Date validTime) {
      this.validTime = validTime;
   }

   public Integer getCouponStatus() {
      return couponStatus;
   }

   public void setCouponStatus(Integer couponStatus) {
      this.couponStatus = couponStatus;
   }

   public Integer getGrantId() {
      return grantId;
   }

   public void setGrantId(Integer grantId) {
      this.grantId = grantId;
   }

   public Integer getGrantStatus() {
      return grantStatus;
   }

   public void setGrantStatus(Integer grantStatus) {
      this.grantStatus = grantStatus;
   }

   public Date getCreateTime() {
      return createTime;
   }

   public void setCreateTime(Date createTime) {
      this.createTime = createTime;
   }

   public Date getUpdateTime() {
      return updateTime;
   }

   public void setUpdateTime(Date updateTime) {
      this.updateTime = updateTime;
   }

   public BigDecimal getScope() {
      return scope;
   }

   public void setScope(BigDecimal scope) {
      this.scope = scope;
   }
}
