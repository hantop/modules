package com.fenlibao.platform.model.thirdparty.vo.bid;

import java.util.List;

import com.fenlibao.platform.common.util.DateUtil;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;

/**
 * 借款满标信息--融360
 * 
 * @author junda.feng
 * @date 2016年6月7日
 * 
 */
public class BidInfoVoRong360 extends BaseBidInfoVo<BidInfoVoRong360> {
	//private String plateType;//标所属平台频道板块,非必需
	//private String guarantorsType;//保障担保机构名称,非必需
	private String province;// 借款人所在省份。,非必需
	private String city;// 借款人所在城市。,非必需
	//private String userAvatarUrl;//发标人头像的URL,非必需
	private String amountUsedDesc;// 借款用途
	//private double revenue;//营收,非必需
	private String successTime;// 满标的时间
	private String publishTime;// 发标时间*格式为标准时间格式：’2014-07-23 12:23:22’,非必需
	private int repaymentType;//还款方式

	public BidInfoVoRong360(BidInfoEntity bid) throws Throwable {
		super(bid);
		this.province=bid.getProvince()==null?null:bid.getProvince().replace("省", "");
		this.city=bid.getCity();
		this.amountUsedDesc=bid.getAmountUsedDesc();
		this.successTime=DateUtil.getDateTime(bid.getSuccessTime());
		this.publishTime=DateUtil.getDateTime(bid.getPublishTime());
		this.repaymentType=bid.getRepaymentType().getCode();
	}

	List<BidInvestrecordsVoRong360> subscribes;

	
	public int getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(int repaymentType) {
		this.repaymentType = repaymentType;
	}



	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAmountUsedDesc() {
		return amountUsedDesc;
	}

	public void setAmountUsedDesc(String amountUsedDesc) {
		this.amountUsedDesc = amountUsedDesc;
	}

	public String getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(String successTime) {
		this.successTime = successTime;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}


	public List<BidInvestrecordsVoRong360> getSubscribes() {
		return subscribes;
	}


	public void setSubscribes(List<BidInvestrecordsVoRong360> subscribes) {
		this.subscribes = subscribes;
	}


}
