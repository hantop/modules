package com.fenlibao.p2p.model.trade.entity;

import java.math.BigDecimal;
import java.util.Date;

public class UserCoupon {
   public int id;
   public int userId;
   public int couponId;
   public int activityId;
   public int tenderId;
   public Date validTime;
   public int couponStatus;
   public int grantId;
   public int grantStatus;
   public Date createTime;
   public Date updateTime;
   public BigDecimal scope;
}
