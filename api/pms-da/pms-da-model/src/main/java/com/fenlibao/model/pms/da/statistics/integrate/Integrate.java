package com.fenlibao.model.pms.da.statistics.integrate;
import java.math.BigDecimal;
/**
 * 积分统计
 * @author Administrator
 *
 */
public class Integrate {

	private String productCode;//商品编码
	private String typeName;//商品名称(对应的积分类型表中的积分类型名称)
	private Integer restStock;//剩余库存
	private int saleAmount;//商品销量(对应的是在会员积分表中的numbers)
	private int purchaseNumber;//购买人数(只统计用户id)
	private int integrateCost;//消费总积分
	private BigDecimal totalAmount;//总现金(部分商品是积分+现金购买)
	public String getproductCode() {
		return productCode;
	}
	public void setproductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getRestStock() {
		return restStock;
	}
	public void setRestStock(Integer restStock) {
		this.restStock = restStock;
	}
	public int getSaleAmount() {
		return saleAmount;
	}
	public void setSaleAmount(int saleAmount) {
		this.saleAmount = saleAmount;
	}
	public int getPurchaseNumber() {
		return purchaseNumber;
	}
	public void setPurchaseNumber(int purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}
	public int getIntegrateCost() {
		return integrateCost;
	}
	public void setIntegrateCost(int integrateCost) {
		this.integrateCost = integrateCost;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Override
	public String toString() {
		return "Integrate [productCode=" + productCode + ", typeName=" + typeName + ", restStock=" + restStock
				+ ", saleAmount=" + saleAmount + ", purchaseNumber=" + purchaseNumber + ", integrateCost="
				+ integrateCost + ", totalAmount=" + totalAmount + "]";
	}
	
}
