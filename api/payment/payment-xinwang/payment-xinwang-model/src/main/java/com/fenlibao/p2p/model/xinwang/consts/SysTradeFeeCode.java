package com.fenlibao.p2p.model.xinwang.consts;

/**
 * t5122
 */
public class SysTradeFeeCode {
	private SysTradeFeeCode(){}

	// 充值类
	/**
	 * 在线充值金额
	 */
	public static final int CZ = 1001;

	/**
	 * 充值手续费
	 */
	public static final int CZ_SXF = 1002;
	/**
	 * 充值成本
	 */
	public static final int CZ_CB = 1003;
	/**
	 * 线下充值金额
	 */
	public static final int CZ_XX = 1004;
	// 提现类
	/**
	 * 提现金额
	 */
	public static final int TX = 2001;
	/**
	 * 提现手续费
	 */
	public static final int TX_SXF = 2002;
	/**
	 * 提现成本
	 */
	public static final int TX_CB = 2003;
	// 投标类
	/**
	 * 投标
	 */
	public static final int TZ = 3001;
	/**
	 * 投标撤销
	 */
	public static final int TZ_CX = 3002;
	/**
	 * 体验金投资
	 */
	public static final int TYJTZ = 3003;
	// 债权转让
	/**
	 * 债权转让手续费
	 */
	public static final int ZQZR_SXF = 4001;
	/**
	 * 卖出债权
	 */
	public static final int ZQZR_MC = 4002;
	/**
	 * 债权转让买入
	 */
	public static final int ZQZR_MR = 4003;
	// 借款类
	/**
	 * 借款本金
	 */
	public static final int JK = 6001;
	/**
	 * 借款管理费
	 */
	public static final int JK_GLF = 6002;
	// 还款类
	/**
	 * 本金
	 */
	public static final int TZ_BJ = 7001;
	/**
	 * 利息
	 */
	public static final int TZ_LX = 7002;


	/**
	 *利息管理费
	 */
	public static final int LX_GLF = 7035;

	/**
	 * 逾期管理费
	 */
	public static final int TZ_YQGLF = 7003;
	/**
	 * 逾期罚息
	 */
	public static final int TZ_FX = 7004;
	/**
	 * 提前还款违约金
	 */
	public static final int TZ_WYJ = 7005;

	/**
	 * 2018/01/11
	 * 提前还款违约金分成
	 */
	public static final int TZ_WYJ_FC=7015;

	/**
	 * 垫付
	 */
	public static final int TZ_DF = 7006;
	/**
	 * 违约金手续费
	 */
	public static final int TZ_WYJ_SXF = 7007;
	/**
	 * 投标奖励
	 */
	public static final int TZ_TBJL = 7008;
	/**
	 * 逾期手续费
	 */
	public static final int TZ_YQ_SXF = 7020;
	/**
	 * 投资加息
	 */
	public static final int TZ_JX = 7022;
	/**
	 * 标加息奖励
	 */
	public static final int BID_JX = 7023;
	/**
	 * 计划标回款转入复投
	 */
	public static final int PLAN_BID_REPAY_LOCK = 7024;
	/**
	 * 借款人还款资金冻结
	 */
	public static final int REPAY_LOCK = 7025;

	/**
	 * 委托收款
     */
	public static final int ENTRUST_PAY=7032;
	/**
	 * 活动费用
	 */
	public static final int HD = 8001;
	/**
	 * 有效推广
	 */
	public static final int TG_YX = 9001;
	/**
	 * 持续推广
	 */
	public static final int TG_CX = 9002;
	// 信用类
	/**
	 * 发标审核信用扣除
	 */
	public static final int XY_FB_TZ = 1101;
	/**
	 * 还款信用返还
	 */
	public static final int XY_HK_FH = 1102;
	/**
	 * 流标信用返还
	 */
	public static final int XY_LB_FH = 1103;
	/**
	 * 人工调整信用额度
	 */
	public static final int XY_CZ = 1104;
	// 费用类
	/**
	 * 成交服务费
	 */
	public static final int CJFWF = 1201;
	/**
	 * 理财管理费/投资管理费
	 */
	public static final int GLF = 1202;
	/**
	 * 代收服务费
	 */
	public static final int DSFWF = 1204;
	/**
	 * 平台服务费
	 */
	public static final int PTFWF = 1205;
	// 优选理财
	/**
	 * 投优选理财
	 */
	public static final int YXLC = 1301;
	/**
	 * 优选加入费
	 */
	public static final int YXLC_JR = 1302;
	/**
	 * 优选服务费
	 */
	public static final int YXLC_FW = 1303;
	/**
	 * 优选退出费
	 */
	public static final int YXLC_TC = 1304;
	/**
	 * 优选理财还款
	 */
	public static final int YXLC_HK = 1305;
	/**
	 * 优选理财利息
	 */
	public static final int YXLC_LX = 1306;
	/**
	 * 风险保证金充值
	 */
	public static final int FXBZJ = 1401;
	/**
	 * 风险保证金转出
	 */
	public static final int FXBZJ_ZC = 1402;

	/**
	 * 身份验证手续费
	 */
	public static final int SFYZSXF = 1501;

	/**
	 * 查标服务费
	 */
	public static final int CBFWF = 1502;

	/**
	 * 担保费
	 */
	public static final int DBF = 1503;

	/**
	 * 平台划拨
	 */
	public static final int PTHB = 1701;

	/****************** 统计费用 *****************************/
	/**
	 * 公司虚拟户到借款人虚拟户的总额
	 */
	public static final int GSDJKRZE = 1601;

	/**
	 * 借款人虚拟户到投资人卡的总额；
	 */
	public static final int JKRDTZRZE = 1602;
	/**
	 * 扣除红包金额；数据库上写的是转账？？
	 */
	public static final int HBJE = 1801;

	/**
	 * 内部转账
	 */
	public static final int NBZZ = 1901;

	/**
	 * 提前还款时原利息备份
	 */
	public static final int TZ_LX_BF = 70022;

	/**
	 * 提现失败,本金返还
	 */
	public static final int TX_SB = 2004;
	/**
	 * 提现失败,手续费返还
	 */
	public static final int TX_SB_SXF = 2005;

	/**
	 * 计划资金解冻
	 */
	public static final int JHZJJD = 7029;

	/**
	 * 现金红包
	 */
	public static final int CASH_VOUCHER = 5118;
}
