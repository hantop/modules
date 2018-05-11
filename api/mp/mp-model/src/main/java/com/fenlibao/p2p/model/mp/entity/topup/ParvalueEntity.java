package com.fenlibao.p2p.model.mp.entity.topup;

import java.math.BigDecimal;

public class ParvalueEntity {

	private BigDecimal parvalue;//面额
	private BigDecimal sellingPrice;//销售价格
	public BigDecimal getParvalue() {
		return parvalue;
	}
	public void setParvalue(BigDecimal parvalue) {
		this.parvalue = parvalue;
	}
	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	
	
}
