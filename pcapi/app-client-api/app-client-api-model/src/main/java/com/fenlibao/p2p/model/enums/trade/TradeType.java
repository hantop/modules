package com.fenlibao.p2p.model.enums.trade;

/**
 * 交易记录类型
 * @author junda.feng
 * @date 2016年5月10日
 */
public enum TradeType {

	//充值 1 =2
	cz_1001(1001, "充值"),
	cz_1004(1004, "线下充值"),
	
	//提现 2 =2
	tx_2001(2001, "提现"),
	tx_2004(2004, "提现失败,本金返还"),
	
	//手续费 3 =5
	ssf_1201(1201, "成交服务费"),
	ssf_2002(2002, "提现手续费"),
	ssf_2003(2003, "提现成本"),
	ssf_2005(2005, "提现失败,手续费返还"),
	ssf_4001(4001, "债权转让手续费"),
	
	//出借 4 =11
	tb_3001(3001, "出借"),
	tb_3002(3002, "流标"),
	tb_3003(3003, "申购开店宝"),
	tb_3004(3004, "加入薪金宝"),
	tb_3005(3005, "续买薪金宝"),
	tb_3006(3006, "活期宝转入"),
	tb_3007(3007, "活期宝转出"),
	tb_13001(13001, "出借开店王"),
	tb_33001(33001, "出借车贷王"),
	tb_43001(43001, "出借房贷王"),
	tb_53001(53001, "出借散标"),
	
	//回收 5=11
	hs_7001(7001, "本金"),
	hs_7002(7002, "利息"),
	hs_7003(7003, "逾期管理费"),
	hs_7004(7004, "逾期罚息"),
	hs_7005(7005, "提前还款违约金"),
	hs_7009(7009, "开店宝本金"),
	hs_7010(7010, "薪金宝本金"),
	hs_7011(7011, "开店宝利息"),
	hs_7012(7012, "薪金宝利息"),
	hs_7013(7013, "活期宝利息"),
	hs_7014(7014, "体验金利息"),

	//奖励 6   =11
	jl_5112(5112, "红包发放"),
	jl_5113(5113, "注册红包"),
	jl_5114(5114, "邀请红包"),
	jl_5115(5115, "出借红包"),
	jl_5116(5116, "返现红包"),
	jl_5118(5118, "现金红包"),
	jl_5201(5201, "积分兑换现金"),
	jl_7008(7008, "出借奖励"),
	jl_8001(8001, "活动奖励"),
	jl_9001(9001, "推广奖励"),
	jl_9002(9002, "推广奖励")
	;
	
	private Integer code;
	private String name;
	
	private TradeType(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public static String getNameByCode(Integer code) {
		for (TradeType tt : TradeType.values()) {
			if (tt.getCode()==tt.getCode()) {
				return tt.getName();
			}
		}
		return null;
	}
}
