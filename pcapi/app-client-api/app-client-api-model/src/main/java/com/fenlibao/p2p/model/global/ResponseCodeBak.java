/**   
 * Copyright © 2015 fenlibao . All rights reserved.
 * 
 * @Title: ErrorCode.java 
 * @Prject: app-client-api-model
 * @Package: com.fenlibao.p2p.model.global 
 * @Description: TODO
 * @author: laubrence   
 * @date: 2015-10-26 上午9:53:18 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.model.global;

/** 
 * @ClassName: ResponseCode 
 * @Description: 业务返回码规范
 * @author: laubrence
 * @date: 2015-10-26 上午9:53:18  
 */
public enum ResponseCodeBak {
	
	/** 	错误码前缀	       错误码描述	                       错误码
			COMMON_	      公用错误码	       10xxx
			USER_	      用户相关错误码	       11xxx
			TRADE_	      交易相关错误码	       12xxx
			WEIXIN_   微信公众号相关错误码           13xxx
			ACTIVITY_  活动相关错误码	   14xxx
			KDB_	      开店宝相关错误码	   20xxx
			XJB_	      薪金宝相关错误码	   21xxx
			HQB_	     活期宝相关错误码	   22xxx
			ZQZR_	     债权转让相关错误码                23xxx
		    ORDER_    订单相关错误码		   30xxx
		    MP_       积分系统相关错误码	   31xxx
			
		NOTE: 10 x xx
		10 为大类型，如common
		x  为子类型，如1为不合法参数。。   2.为缺少参数。。
		xx 为错误码，如01、02
	*/
	
	/**
	 * 公用错误码 start
	 */
	COMMON_PLATFORM_ACCOUNT_NOT_EXIST("10301","系统平台账号不存在"),
	COMMON_PLATFORM_ACCOUNT_BALANCE_LACK("10302","系统平台往来账户余额不足"),

	COMMON_DECRYPT_FAILURE("10801","AES解密失败"),
	COMMON_ENCRYPT_FAILURE("10802","AES加密失败"),
	COMMON_NOT_VALID_ACCESSTOKEN("10803","accessToken不合法"),
	COMMON_ACCESSTOKEN_TIMEOUT("10804","accessToken已过期"),
	COMMON_CAPTCHA_KEY_WRONG("10805", "验证码key错误"),
	
	/**
	 * 公用错误码 end
	 */
	
	/**
	 *  用户相关错误码 start 
	 */
	
	USER_IDENTITY_VERIFY_FAIL("11001", "实名验证失败"),
	USER_IDENTITY_UNAUTH("11002", "未实名认证"),
	USER_PHONE_UNAUTH("11003", "手机未认证"),
	USER_WLZH_ACCOUNT_NOT_EXIST("11301","用户往来账户不存在"),
	USER_WLZH_ACCOUNT_BALANCE_LACK("11302","用户往来账户余额不足"),
	
	USER_IS_HAVE_BID("11303","用户不是首次出借"),
	USER_NOT_EXIST("11304", "用户不存在"),
	
	USER_SDZH_ACCOUNT_NOT_EXIST("11305","用户锁定账户不存在"),
	USER_SDZH_ACCOUNT_BALANCE_LACK("11306","用户锁定账户余额不足"),
	/**
	 *  用户相关错误码 end
	 */
	
	/**
	 *  交易相关错误码 start
	 *  充值：1,提现：2
	 */
	TRADE_NOT_SUPPORT_BANK("12101", "暂不支持该银行"),
	TRADE_QUERY_CARD_INFO_FAIL("12102", "查询银行卡信息失败"),
	
	TRADE_WITHDRAW_FAIL("12201", "提现失败"),
	TRADE_WITHDRAW_LIMIT("12202", "提现金额不能低于%s元，不能高于%s元"),
	TRADE_LOAN_OVERDUE("12203","有逾期借款未还"),
	TRADE_BANK_CARD_NOT_EXIST("12204","银行卡不存在"),
	TRADE_UNBOUND_BANK_CARD("12205","未绑定银行卡"),
	TRADE_AMOUNT_LESSTHAN_POUNDAGE("12206","提现金额不能小于提现手续费"),
	TRADE_INSUFFICIENT_BALANCE("12207","账户余额不足"),
	TRADE_OFFLINE_CANNOT_WITHDRAW("12208","线下充值金额%s小时内不可提现.如有问题,请联系客服"),
	TRADE_BLACKLIST("12209","该用户已被拉黑不能提现"),
	
	TRADE_PASSWORD_DECRYPT_FAILURE("12301", "交易密码解密失败"),
	TRADE_USER_NOT_SET_PASSWORD("12302", "未设置交易密码"),
	TRADE_USER_NOT_RIGHT_PASSWORD("12303","交易密码错误"),
	
	/**
	 *  交易相关错误码 end
	 */
	
	/**
	 *  微信公众号相关错误码  start
	 */
	
	/**
	 *  微信公众号相关错误码  end
	 */
	
	/**
	 *  营销活动相关错误码  start
	 */
	ACTIVITY_RECORD_EXIST("14001","记录已存在"),
	
	ACTIVITY_PHONE_REG_NOT_RIGHT("14002","手机号码格式不正确"),
	
	/**
	 *  营销活动相关错误码  end
	 */
	
	/**
	 *  开店宝相关错误码 start
	 */
	
	KDB_NOVICE_NOT_ZQZR("20301","开店宝新手标不能债权转让"),
	
	KDB_NOVICE_OVER_MAX_AMOUNT("20302","开店宝新手标出借金额大于最大限制金额"),
	
	KDB_NOVICE_NOT_USE_FXHB("20303","开店宝新手标不能使用返现红包"),
	
	KDB_NOVICE_NOT_NEW_USER("20304","新手标只限新用户出借"),
	
	/**
	 *  开店宝相关错误码 end
	 */
	
	/**
	 *   薪金宝相关错误码 start
	 */
	
	/**
	 *   薪金宝相关错误码 end
	 */
	
	/**
	 *   活期宝相关错误码 start
	 */
	
	/**
	 *   活期宝相关错误码 end
	 */
	
	/**
	 *   债权转让相关错误码 start
	 */
	
	ZQZR_CSGD("23002","转让次数超限，该债权不可转让"),
	
	ZQZR_SJWD("23003","持有不足5天，该债权不可转让"),
	
	ZQZR_SJKD("23004","5天内到期，该债权不可转让"),
	
	ZQZR_TRANSFER_VALUE_UNDER_ZERO("23006","转让价格不能小于0"),
	
	ZQZR_IS_TRANSFER("23007","该债权正在转让"),
	
	ZQZR_IS_REPAYMENT("23008","该债权正在还款中"),
	
	ZQZR_ZQVALUE_LESS_TRANSFERVALUE("23009","转让金额范围异常"),
	
	ZQZR_PASSEDEARNING_UNDER_ZERO("23010","债权已过天数利息不能小于0"),
	
	ZQZR_NOT_WH("23011","不存在未还记录"),
	
	ZQZR_APPLY_DATA_NOT_FOUND("23101","线上债权转让申请不存在"),
	
	ZQZR_OVER_REPAY("23102","借款逾期未还"),
	
	ZQZR_APPLY_NOT_ZRZ("23103","线上债权转让申请不是转让中状态,不能转让"),
	
	ZQZR_APPLY_HAVE_OVER_DATE("23104","线上债权转让申请已到截至日期,不能转让"),
	
	ZQZR_CANNOT_BUY_MYSELF("23105","不能购买自己的债权"),
	
	ZQZR_ZQ_INFO_NOT_FOUND("23106","债权信息不存在"),
	
	ZQZR_ZQ_VALUE_NOT_ZERO("23107","债权价值不能为0"),
	
	ZQZR_ZQ_REPAY_PLAN_NOT_FOUND("23108","债权还款计划不存在"),
	
	ZQZR_SRR_WLZH_ACCOUNT_NOT_EXIST("23109","受让人用户往来账户不存在"),
	
	ZQZR_SRR_USER_WLZH_ACCOUNT_BALANCE_LACK("23110","受让人用户往来账户余额不足"),
	
	ZQZR_CRR_USER_WLZH_ACCOUNT_NOT_EXIST("23111","出让人用户往来账户不存在"),
	
	ZQZR_CRR_USER_WLZH_ACCOUNT_BALANCE_LACK("23112","出让用户往来账户余额不足"),
	
	ZQZR_ZQ_HAVE_CANCEL("23113","该债权已下架"),
	
	ZQZR_APPLY_HAVE_OVER_FIVE_DAY("23114","线上债权转让申请已超过5天,不能转让"),
	
	ZQZR_ASSIGNED("23115","债权已转让"),
	
	ZQZR_IS_NOVICEBID("23116","是新手标,不能转让"),
	/**
	 *   债权转让相关错误码  end
	 */
	
	/**
	 *   订单信息相关错误码 start
	 */
	ORDER_INFO_NOT_FOUND("30001","订单详细信息不存在"),
	
	/**
	 *   订单信息相关错误码 end
	 */
	
	/**
	 *   积分系统相关错误码 start
	 */
	MP_MY_POINTS_ACCOUNT_NOT_FOUND("31001","用户积分账户不存在"),
	MP_MY_POINTS_ACCOUNT_FORBID("31002","用户积分账户禁用"),
	MP_MY_POINTS_ACCOUNT_FREEZE("31003","用户积分账户冻结"),
	MP_MY_POINTS_ACCOUNT_REMAIN_LACK("31004","用户积分账户积分余额不足"),
	
	MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_VALID("31009","积分兑换金额验证不通过"),
	MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND("31010","未找到积分兑换配置信息"),
	MP_POINTS_OVER_MAX_EXCHANGE_NUM("31101","积分兑换超过[#{pTypeCode}]类型最高兑换积分数量"),
	MP_POINTS_BELOW_MIN_EXCHANGE_NUM("31102","积分兑换少于[#{pTypeCode}]类型最低兑换积分数量"),
	MP_POINTS_OVER_MAX_EXCHANGE_FREQUENCY("31103","积分兑换超过[#{pTypeCode}]类型最大兑换积分次数"),
	MP_POINTS_OVER_YEAR_EXCHANGE_FREQUENCY("31104","积分兑换超过[#{pTypeCode}]类型年兑换积分次数"),
	MP_POINTS_OVER_MONTH_EXCHANGE_FREQUENCY("31105","积分兑换超过[#{pTypeCode}]类型月兑换积分次数"),
	MP_POINTS_OVER_DAY_EXCHANGE_FREQUENCY("31106","积分兑换超过[#{pTypeCode}]类型天兑换积分次数"),
	
	MP_PARVALUE_NOT_EXIST("31107","充值面值不存在"),
	MP_ADD_MOBILE_TOPUP_ORDER_FAILURE("31108","添加充值订单失败"),
	MP_INTEGRAL_NOT_EXIST("31109","积分类型不存在"),
	MP_RECHARGE_ERROR("31110","充值异常"),
	MP_POINTS_CODE_ERROR("31111","充值面额与可积分类别不符"),
	
	/**
	 *   积分系统相关错误码 end
	 */
	
	SUCCESS("200", "操作成功"),
	FAILURE("500", "操作失败"),
	NOT_AUTHORIZED("401","未认证"),
	NOT_VALID_TOKEN("1029","用户token验证不通过"),
	EMPTY_PARAM("601","请求参数为空");
	
	private String code;

    private String message;

    ResponseCodeBak(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
	
}
