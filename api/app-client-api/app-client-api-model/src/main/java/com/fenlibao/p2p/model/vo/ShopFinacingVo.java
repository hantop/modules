package com.fenlibao.p2p.model.vo;

import java.io.Serializable;

/**
 * 开店宝投资详情
 */
public class ShopFinacingVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int kdbPlantId;//开店宝计划id
	
	private String kdbPlanTitle;//开店宝计划title
	
	private String proUrl;//产品说明书url
	
	private String investInfo;//投资信息

	public int getKdbPlantId() {
		return kdbPlantId;
	}

	public void setKdbPlantId(int kdbPlantId) {
		this.kdbPlantId = kdbPlantId;
	}

	public String getKdbPlanTitle() {
		return kdbPlanTitle;
	}

	public void setKdbPlanTitle(String kdbPlanTitle) {
		this.kdbPlanTitle = kdbPlanTitle;
	}

	public String getProUrl() {
		return proUrl;
	}

	public void setProUrl(String proUrl) {
		this.proUrl = proUrl;
	}

	public String getInvestInfo() {
		return investInfo;
	}

	public void setInvestInfo(String investInfo) {
		this.investInfo = investInfo;
	}
	
}
