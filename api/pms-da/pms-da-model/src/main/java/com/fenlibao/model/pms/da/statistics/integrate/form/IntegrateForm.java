package com.fenlibao.model.pms.da.statistics.integrate.form;

import org.springframework.util.StringUtils;

/**
 * 积分统计高级查询
 * 
 * @author Administrator
 *
 */
public class IntegrateForm {

	private String startDate;// 统计开始日期
	private String endDate;// 统计结束日期
	private String productCode;// 商品编码
	private String typeName;// 商品名称
	private String cmd;//排序方式

	public String getStartDate() {
		return startDate;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getProductCode() {
		return StringUtils.hasLength(productCode) ? productCode : null;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getTypeName() {
		return StringUtils.hasLength(typeName) ? typeName : null;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
