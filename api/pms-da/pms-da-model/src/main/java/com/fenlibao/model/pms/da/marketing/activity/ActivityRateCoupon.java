package com.fenlibao.model.pms.da.marketing.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 加息券
 */
public class ActivityRateCoupon {
	
	private Integer id;
    private String couponCode;// 优惠券编码
    private int couponTypeId;// 优惠券类型ID
    private int effectDay;// 优惠券有效天数
    private int maxInvestMoney;// 投资金额上限
    private int minInvestMoney;// 投资金额下限
    private int maxInvestDay;// 投资期限上限
    private int minInvestDay;// 投资期限下限
    private BigDecimal scope;// 优惠幅度
    private Date createTime;
    private Date updateTime;
    private Integer grantStatus;//发放状态
    
  //==================增加标的类型限制,投资期限,以及来源(实际是备注)add Lee==============
    // @NotNull(message = "标的类型不能为空")
     private List<Integer> bidTypeIds = new ArrayList<Integer>(); //对象映射,来自对应的标的限制,一对多(不存入这个实体类中对应的数据库表)
     private boolean investDeadLineType;//默认为false,此时表示投资期限不限.反之,按天来计算
     private String bidTypeStr;
     
     private Activity activity;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public int getCouponTypeId() {
        return couponTypeId;
    }

    public void setCouponTypeId(int couponTypeId) {
        this.couponTypeId = couponTypeId;
    }

    public int getEffectDay() {
        return effectDay;
    }

    public void setEffectDay(int effectDay) {
        this.effectDay = effectDay;
    }

    public int getMaxInvestMoney() {
        return maxInvestMoney;
    }

    public void setMaxInvestMoney(int maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public int getMinInvestMoney() {
        return minInvestMoney;
    }

    public void setMinInvestMoney(int minInvestMoney) {
        this.minInvestMoney = minInvestMoney;
    }

    public int getMaxInvestDay() {
        return maxInvestDay;
    }

    public void setMaxInvestDay(int maxInvestDay) {
        this.maxInvestDay = maxInvestDay;
    }

    public int getMinInvestDay() {
        return minInvestDay;
    }

    public void setMinInvestDay(int minInvestDay) {
        this.minInvestDay = minInvestDay;
    }

    public BigDecimal getScope() {
        return scope;
    }

    public void setScope(BigDecimal scope) {
        this.scope = scope;
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

    public Integer getGrantStatus() {
        return grantStatus;
    }

    public void setGrantStatus(Integer grantStatus) {
        this.grantStatus = grantStatus;
    }

    public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public List<Integer> getBidTypeIds() {
        return bidTypeIds;
    }

    public void setBidTypeIds(List<Integer> bidTypeIds) {
        this.bidTypeIds = bidTypeIds;
    }

    public boolean isInvestDeadLineType() {
        return investDeadLineType;
    }

    public void setInvestDeadLineType(boolean investDeadLineType) {
        this.investDeadLineType = investDeadLineType;
    }

    public String getBidTypeStr() {
        return bidTypeStr;
    }

    public void setBidTypeStr(String bidTypeStr) {
        this.bidTypeStr = bidTypeStr;
    }
    
  //返回一个json字符串
  		public String getJsonString(){
  			Map<String,Object> json = new HashMap<>();
  			json.put("id", this.getId());
  			json.put("couponCode", this.getCouponCode());
  			json.put("effectDay", this.getEffectDay());
  			json.put("maxInvestMoney", this.getMaxInvestMoney());
  			json.put("minInvestMoney", this.getMinInvestMoney());
  			json.put("maxInvestDay", this.getMaxInvestDay());
  			json.put("minInvestDay", this.getMinInvestDay());
  			json.put("scope", this.getScope());
  			return JSONObject.toJSONString(json);
  		}
}