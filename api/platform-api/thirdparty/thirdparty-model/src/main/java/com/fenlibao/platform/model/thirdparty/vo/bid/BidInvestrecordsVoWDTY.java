package com.fenlibao.platform.model.thirdparty.vo.bid;

import com.fenlibao.platform.common.util.DateUtil;
import com.fenlibao.platform.common.util.StringHelper;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInvestrecordsEntity;


/**
 * 投资信息--网贷天眼
 * @author junda.feng
 * @date 2016年6月13日
 * 
 */
public class BidInvestrecordsVoWDTY {
	private String id;//标的唯一编号(不为空,很重要)
	private String username;//投标人的用户名称,登录账号,可辨识区分,可支持加密数据.
	private String userid;//投标人ID
	private String type;//例如:手动、自动.
	private Double money;//投标金额实际生效部分(保留两位小数).
	private Double account;//投标金额实际生效部分(保留两位小数),请过滤掉投资金额小于10块的记录.
	private String status;//例如:成功、部分成功、失败.
	private String add_time;//投标时间

	public BidInvestrecordsVoWDTY(BidInvestrecordsEntity records)
			throws Throwable {
		this.id=String.valueOf(records.getProjectId());
		this.username=StringHelper.encode(StringHelper.deviation(records.getSubscribeUserName()));
		this.userid=StringHelper.encode(StringHelper.deviation(records.getSubscribeUserName()));
		this.type="F".equals(records.getType())?"手动":"自动";
		this.money=records.getAmount().doubleValue();
		this.account=records.getAmount().doubleValue();
		this.status="成功";
		this.add_time=DateUtil.getDateTime(records.getAddDate());
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getAccount() {
		return account;
	}

	public void setAccount(Double account) {
		this.account = account;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	
	
}
