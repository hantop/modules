package com.fenlibao.platform.model.thirdparty.vo.bid;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;

import com.fenlibao.platform.common.config.Config;
import com.fenlibao.platform.common.util.DateUtil;
import com.fenlibao.platform.common.util.StringHelper;
import com.fenlibao.platform.model.thirdparty.entity.bid.BidInfoEntity;
import com.fenlibao.platform.model.thirdparty.enums.RepaymentType;

/**
 * 借款标信息--网贷天眼
 * 
 * @author junda.feng
 * @date 2016年6月7日
 * 
 */
public class BidInfoVoWDTY  {
	
    private String id;//项目主键
    private String platform_name;//平台中文名称.
    private String url;//标的详细页面地址链接
    private String title;//借款标题
    private String userName;//发标人??????????????
    private String userid;//发标人ID
    private Integer status;//标的状态,0,正在投标中的借款标;1,已完成(包括还款中和已完成的借款标).
    private Integer c_type;//借款类型,0 代表信用标,1 担保标;2 抵押,质押标, 3 秒标;4 债权转让标(流转标,二级市场标的);5 理财计划(宝类业务_活期);6 其它;7 净值标;8 活动标(体验标).9 理财计划(宝类业务_定期).3，4，5标类型不参与贷款余额计算；请注意5【理财计划(宝类业务_活期)】和9【理财计划(宝类业务_定期)】的区分；4债权转让标指的是不会产生新待还的转让，如果会产生新待还，请返回其他标类型.
    private Double amount;//借款金额
    private Double rate;//年化利率,如果为月利率或天利率,统一转换为年利率并使用小数表示;精度4位,如:0.0910.
    private Integer period;//借款期限
    private Integer p_type;//期限单位,0 代表天,1 代表月.
    private Integer pay_way;//还款方式,0 代表其他;1 按月等额本息还款;2按月付息,到期还本;3 按天计息,一次性还本付息;4,按月计息,一次性还本付息;5 按季分期还款;6 为等额本金,按月还本金;7 先息期本;8 按季付息,到期还本;9 按半年付息,到期还本;10 按年付息，到期还本.
    private Double process;//进度
//  private Double reward;//奖励 
//  private Double guarantee;//担保奖励
    private String start_time;//标的创建时间(发标时间)
    private String end_time;//满标时间
    private Integer invest_num;//这笔借款标有多少个投标记录.
//  private Double c_reward;//继续投标的奖励.
    

    private static Config config = ConfigFactory.create(Config.class);
    
	public BidInfoVoWDTY(BidInfoEntity bid) throws Throwable {
		this.id=String.valueOf(bid.getProjectId());
		this.platform_name=config.getPlatformName();
		this.url=config.getBidDetail()+bid.getProjectId();
		this.title=bid.getTitle();
		if(title.length()>16 && title.substring(0,5).equals(BaseBidInfoVo.XFXDB)){
			String uid=title.substring(7,title.length());
			this.userName=StringHelper.encode(uid);
			this.userid=StringHelper.encode(uid);
		}else{
			this.userName=StringHelper.encode(StringHelper.deviation(bid.getUserID()));
			this.userid=StringHelper.encode(StringHelper.deviation(bid.getUserID()));
		}
		if("TBZ".equals(bid.getState()))this.status=0;
		if(!"TBZ".equals(bid.getState()))this.status=1;
		//0信用认证,6实地认证,2抵押担保
		if(StringUtils.isNoneBlank(bid.getTypeName())){
			String [] str=bid.getTypeName().split(",");
			if(str.length!=1){
				this.c_type=6;
			}else if("信用认证".equals(str[0])){
				this.c_type=0;
			}else if("抵押担保".equals(str[0])){
				this.c_type=2;
			}
		}else{
			this.c_type=6;
		}
		
		this.amount=bid.getAmount().doubleValue();
		DecimalFormat df=new DecimalFormat("#.0000"); 
		this.rate=Double.parseDouble(df.format(bid.getInterestRate()));
		
		this.period=bid.getDeadline_d()==0?bid.getDeadline_m():bid.getDeadline_d();
		this.p_type=bid.getDeadline_d()==0?1:0;
		//还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;
		this.pay_way=3;
		if(bid.getRepaymentType().equals(RepaymentType.DEBX))this.pay_way=1;
		if(bid.getRepaymentType().equals(RepaymentType.MYFX))this.pay_way=2;
		if(bid.getRepaymentType().equals(RepaymentType.YCFQ))this.pay_way=3;
		if(bid.getRepaymentType().equals(RepaymentType.DEBJ))this.pay_way=6;
		
		BigDecimal amount=bid.getAmount();
    	BigDecimal voteAmount=bid.getVoteAmount();
    	BigDecimal oh=new BigDecimal(100);
    	BigDecimal process1= amount.subtract(voteAmount).divide(bid.getAmount(),10,BigDecimal.ROUND_HALF_DOWN);
		this.process=voteAmount.compareTo(oh)<0?1.0:Double.parseDouble(df.format(process1));
		this.start_time=DateUtil.getDateTime(bid.getPublishTime());
		this.end_time=DateUtil.getDateTime(bid.getSuccessTime());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlatform_name() {
		return platform_name;
	}

	public void setPlatform_name(String platform_name) {
		this.platform_name = platform_name;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getC_type() {
		return c_type;
	}

	public void setC_type(Integer c_type) {
		this.c_type = c_type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}



	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getP_type() {
		return p_type;
	}

	public void setP_type(Integer p_type) {
		this.p_type = p_type;
	}

	public Integer getPay_way() {
		return pay_way;
	}

	public void setPay_way(Integer pay_way) {
		this.pay_way = pay_way;
	}

	public Double getProcess() {
		return process;
	}

	public void setProcess(Double process) {
		this.process = process;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public Integer getInvest_num() {
		return invest_num;
	}

	public void setInvest_num(Integer invest_num) {
		this.invest_num = invest_num;
	}

	public static Config getConfig() {
		return config;
	}

	public static void setConfig(Config config) {
		BidInfoVoWDTY.config = config;
	}

	
}
