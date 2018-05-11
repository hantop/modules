package com.fenlibao.p2p.model.mp.vo.mall;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 商城商品(现金兑换等)
 * @author zcai
 * @date 2016年3月25日
 */
public class MallCommodityVO {

	private String name; //名称
	
	private String slogan; //标语
	
	private String pointsTypeCode; //积分类型编码
	
	private Integer pointsQty; //积分数量
	
	private Integer discountPointsQty; //折后积分数量,=0时无折扣
	
	private String iconUrl; //icon连接

	//应iOS APP要求，添加一下字段
	private String nameNum;
	private String nameStr;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.nameNum = getNumbers(name);
		this.nameStr = splitNotNumber(name);
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getPointsTypeCode() {
		return pointsTypeCode;
	}

	public void setPointsTypeCode(String pointsTypeCode) {
		this.pointsTypeCode = pointsTypeCode;
	}

	public Integer getPointsQty() {
		return pointsQty;
	}

	public void setPointsQty(Integer pointsQty) {
		this.pointsQty = pointsQty;
	}

	public Integer getDiscountPointsQty() {
		return discountPointsQty;
	}

	public void setDiscountPointsQty(Integer discountPointsQty) {
		this.discountPointsQty = discountPointsQty;
	}

	public String getNameNum() {
		return nameNum;
	}

	public void setNameNum(String nameNum) {
		this.nameNum = nameNum;
	}

	public String getNameStr() {
		return nameStr;
	}

	public void setNameStr(String nameStr) {
		this.nameStr = nameStr;
	}
	
	public String getNumbers(String content) {  
	       Pattern pattern = Pattern.compile("\\d+");  
	       Matcher matcher = pattern.matcher(content);  
	       while (matcher.find()) {  
	           return matcher.group(0);  
	       }  
	       return "";
	   }
	  
	public String splitNotNumber(String content) {  
	    Pattern pattern = Pattern.compile("\\D+");  
	    Matcher matcher = pattern.matcher(content);  
	    while (matcher.find()) {  
	        return matcher.group(0);  
	    }  
	    return "";  
	} 
	
}
