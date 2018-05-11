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
public enum ResponseCode {
	
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
		    PAYMENT_  支付模块			   50xxx
			
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
    COMMON_PARAM_TYPE_WRONG("110101", "参数类型错误"),
	COMMON_PARAM_WRONG("110102", "参数错误"),

	COMMON_DECRYPT_FAILURE("10801","AES解密失败"),
	COMMON_ENCRYPT_FAILURE("10802","AES加密失败"),
	COMMON_NOT_VALID_ACCESSTOKEN("0110803","accessToken不合法"),
	COMMON_ACCESSTOKEN_TIMEOUT("0110804","accessToken已过期"),
	COMMON_CAPTCHA_KEY_WRONG("10805", "验证码key错误"),
	
	COMMON_OPERATION_IS_TOO_FREQUENT("10806", "用户操作过于频繁"),
	COMMON_VERSION_TOO_OLD("0210807", "该版本已不再支持，请升级到最新版本"),
	COMMON_PARAM_TYPE_ERROR("0110808", "参数类型错误"),
	COMMON_ACCESSTOKEN_BUILD_FAILURE("0110809","accessToken创建失败"),
	COMMON_ACCESSTOKEN_CLIENT_TYPE_NOT_SAME("0110810","clientType不能和targetClientType相同"),
	COMMON_USER_TOKEN_BUILD_FAILURE("0110811","用户登录token生成失败"),
	COMMON_ACCESSTOKEN_CLIENT_TYPE_INVALID("0110812","clientType不匹配"),
	COMMON_INTERFACE_OUT_OF_SERVICE("0110813","该接口已停止服务"),
	COMMON_NOT_VALID_TOKEN("110105","用户token验证不通过"),
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

	USER_ACCOUNT_BALANCE_INSUFFICIENT("311115", "您账户余额不足，请及时充值"),//账户余额不足
	
	USER_IS_HAVE_BID("11303","用户不是首次出借"),
	USER_NOT_EXIST("11304", "用户不存在"),
	USER_IDCARD_FORMAT_ERROR("211120","身份证号不正确"),//身份证格式错误
	
	USER_SDZH_ACCOUNT_NOT_EXIST("11305","用户锁定账户不存在"),
	USER_SDZH_ACCOUNT_BALANCE_LACK("11306","用户锁定账户余额不足"),
	USER_AUTH_REALNAME_EXIST("11307","用户已经实名认证过"),
	USER_OLD_PWD_NOT_RIGHT("11308","原密码错误"),
	USER_AUTH_REALNAME_FAIL("30813", "实名认证失败"),
	USER_AUTH_UN_MATCH("30815", "姓名或者身份证信息不一致"),
	USER_NOT_XW_ACCOUNT("311130", "用户还未开通存管"),

	GUOXIN_LIB_NULL("30816", "实名认证国信库中无此号"),
	GUOXIN_CHANNEL_ERROR("30817", "实名认证国信通道出现问题"),
	/**
	 *  用户相关错误码 end
	 */
	
	/**
	 *  交易相关错误码 start
	 *  充值：1,提现：2
	 */
	TRADE_NOT_SUPPORT_BANK("12101", "暂不支持该银行"),
	TRADE_QUERY_CARD_INFO_FAIL("12102", "查询银行卡信息失败"),
	BANK_CARD_NO_FORMAT_ERROR("12103","银行卡号格式不正确"),
	BIND_BANK_CARD_FAILURE("12104","绑定银行卡失败"),
	BANK_CARD_NO_EMPTY("12105","银行卡号不能为空"),
	TRADE_RECHARGE_AMOUNT_SCOPE("12106","充值金额必须大于%s元小于%s元"),
	
	TRADE_WITHDRAW_FAIL("12201", "提现失败"),
	TRADE_WITHDRAW_LIMIT("12202", "提现金额不能低于%s元，不能高于%s元"),
	TRADE_LOAN_OVERDUE("12203","有逾期借款未还"),
	TRADE_BANK_CARD_NOT_EXIST("12204","银行卡不存在"),
	TRADE_UNBOUND_BANK_CARD("12205","未绑定银行卡"),
	TRADE_AMOUNT_LESSTHAN_POUNDAGE("12206","提现金额不能小于提现手续费"),
	TRADE_INSUFFICIENT_BALANCE("12207","账户余额不足"),
	TRADE_OFFLINE_CANNOT_WITHDRAW("12208","线下充值金额%s小时内不可提现.如有问题,请联系客服"),
	TRADE_BLACKLIST("12209","您的账户资金已被锁定，请联系客服400-930-5559"),
	
	TRADE_PASSWORD_DECRYPT_FAILURE("12301", "交易密码解密失败"),
	TRADE_USER_NOT_SET_PASSWORD("12302", "未设置交易密码"),
	TRADE_USER_NOT_RIGHT_PASSWORD("12303","交易密码错误"),
	TRADE_AMOUNT_FORMAT_ERROR("0112304","金额格式错误"),
	
	/**
	 *  交易相关错误码 end
	 */

	////标相关错误码(2xxxx)  start
	BID_INVESTMENT_FAILURE("220300", "出借失败"),
	BID_DETAILS_EMPTY("220301","该详情不存在"),
	BID_NOT_EXIST("120302", "指定的标不存在"),
	BID_INVESTMENT_AMOUNT_GT0("120103", "出借金额必须大于零"),
	BIT_LOAN_OVERDUE("220304", "您有借款逾期未还，不满足出借条件"),
	BID_INVESTMENT_AMOUNT_LT100("220105", "出借金额需为100元起"),
	BID_BORROWER_CANNOT_INVESTMENT("220306", "您是该项目借款人，不满足出借条件"),
	BID_OUT_OF_DATE("220307", "您加入的标已过期，请于下次出借日加入"),
	BID_FULLED("220308", "该项目已满标，请出借其他项目"),
	BID_INVEST_AMOUNT_TOO_MUCH("220309", "出借金额已超过剩余可出借额"),
	BID_CONDITIONS_NOT_SATISFIED("220310", "不满足出借条件"),
	BID_INVESTMENT_AMOUNT_INTEGER("220324","出借金额必须为整数"),

	BID_RED_ENVELOPE_IS_USED("220311", "返现券已使用"),
	BID_RED_ENVELOPE_IS_OVERDUE("220312", "返现券已过期"),
	BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED("120313", "返现券使用条件不满足"),
	BID_RED_ENVELOPE_TYPE_NOT_EXIST("120114", "红包类型不存在"),

	BID_NOVICE_NOT_ZQZR("220315","新手标不能债权转让"),
	BID_NOVICE_OVER_MAX_AMOUNT("220116","新手标出借金额大于最大限制金额"),
	BID_NOVICE_NOT_USE_FXHB("220317","新手标不能使用返现券"),
	BID_NOVICE_NOT_NEW_USER("220318","新手标只限新用户出借"),

	BID_COUPON_CONDITIONS_NOT_SATISFIED("120319", "加息券使用条件不满足"),
	BID_COUPON_NOT_EXIST("120320", "加息券不存在"),
	BID_COUPON_IS_USED("120321", "加息券已使用"),
	BID_COUPON_IS_OVERDUE("120322", "加息券已过期"),
	BID_NOVICE_NOT_USE_JXQ("120323","新手标不能使用加息券"),

	BID_NOT_CG("220325","该存管项目不存在"),




	//债权转让相关错误码  end
	//出借分享 start
	BID_SHARE_WITHOUT_INVESTMENT("124301", "该用户没有出借该标"),
	BID_SHARE_RECORD_NOT_EXIST("124302", "出借分享红包记录不存在"),
	BID_SHARE_FINISH("224303", "红包已抢光啦"),
	BID_SHARE_ALREADY_GET("224304", "您已领过啦"),
	BID_SHARE_ALREADY("124305", "已经分享过红包"),
	//出借分享 end

	//出借随时退出标
	BID_ANYTIME_QUIT_USER_INVEST("324307","只限符合条件的用户出借"),
	//出借定向标 end
	////标相关错误码 end


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

	ACTIVITY_LOTTERY_ACTIVITY_NOT_EXIST("0214101","抽奖活动不存在"),

	ACTIVITY_LOTTERY_ACTIVITY_HAVE_NOT_STARTED("0214102","抽奖活动未开始"),

	ACTIVITY_LOTTERY_ACTIVITY_HAVE_EXPIRE("0214103","抽奖活动已结束"),

	ACTIVITY_USER_LOTTERY_DRAW_ERROR("0114104","抽奖处理失败"),

	ACTIVITY_USER_LOTTERY_TIMES_LACK("0114105","用户抽奖次数不足"),

	ACTIVITY_LOTTERY_LACK_EFFECTIVE_PRIZES("0114106","抽奖没有有效奖品信息"),

	ACTIVITY_LOTTERY_DRAW_RECORD_NOT_FOUND("0214107","抽奖记录不存在"),

	ACTIVITY_LOTTERY_DAY_RICHEST_BOARD_NOT_START("0214108","未到活动开榜时间"),

	ACTIVITY_LOTTERY_DAY_RICHEST_BOARD_EXPIRED("0214109","活动开榜时间已结束"),

	ACTIVITY_OLYMPIC_REDPACKET_UNUSED("214110","领取的红包还没使用"),
	ACTIVITY_END("214111","活动已结束"),
	ACTIVITY_NOT_ACTIVITY_TIME("214112","当前时间不在活动时间范围"),

	/**
	 *  营销活动相关错误码  end
	 */
	
	/**
	 *  开店宝相关错误码 start
	 */
	
	NOVICE_NOT_ZQZR("20301","新手标不能债权转让"),
	
	NOVICE_OVER_MAX_AMOUNT("20302","新手标出借金额大于最大限制金额"),
	
	NOVICE_NOT_USE_FXHB("20303","新手标不能使用返现红包"),
	
	NOVICE_NOT_NEW_USER("20304","新手标只限新用户出借"),


	/**
	 * 版本限制
	 */
	NOT_SUPPURT_VERSION("21001","为了您的资金可靠，请前往存管版操作"),

	/**
	 * 限制普通版充值功能
	 */
	NOT_SUPPURT_TOPUP("21002","按监管要求，请前往存管版操作"),

	NOT_SUPPURT_BUY_TRANFER("21003","资金可靠升级，如需购买请到新版"),
	NOT_SUPPURT_OUT_TRANFER("21004","应监管要求，本功能已停用，详情咨询客服"),

	/**
	 *  开店宝相关错误码 end
	 */

	//出借定向标
	BID_DIRECTIONAL_TOTAL_USER_ASSETS("324306","只限符合条件的用户出借"),
	BID_RED_ENVELOPE_NOT_EXIST("120314", "红包不存在"),
	//出借定向标 end
	
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
	
	ZQZR_SJWD("23003","持有不足3天，该债权不可转让"),
	
	ZQZR_SJKD("23004","3天内到期，该债权不可转让"),
	
	ZQZR_TRANSFER_VALUE_UNDER_ZERO("23006","转让价格不能小于0"),
	
	ZQZR_IS_TRANSFER("23007","该债权正在转让"),
	
	ZQZR_IS_REPAYMENT("23008","该债权正在还款中"),
	
	ZQZR_ZQVALUE_LESS_TRANSFERVALUE("23009","转让金额范围异常"),
	
	ZQZR_PASSEDEARNING_UNDER_ZERO("23010","债权已过天数利息不能小于0"),
	
	ZQZR_NOT_WH("23011","不存在未还记录"),

	ZQZR_NOT_SUPPORT_BY_REPAYMENT("23012","该还款方式暂不支持债权转让"),
	
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

	TCJH_IS_NOVICEBID("223116","新手计划,不能退出"),

	TCJH_COTAIN_NOT_REPAYMENT("223108","计划中含有未回款的标，该计划不可退出"),

	TCJH_OVER_DATE("223104","债权有逾期，该计划不可退出"),

	TCJH_SJKD("223005","3天内回款，该计划不可退出"),

	TCJH_SJWD("223003","持有天数不足，该计划不可退出"),

	ZQZR_CANNOT_BUY_PLAN("23117","不能购买此债权"),
	ZQZR_BUY_FAIL("223118", "购买债权失败，请联系客服400-930-5559"),

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
	MP_MY_POINTS_ACCOUNT_NOT_FOUND("0131001","用户积分账户不存在"),
	MP_MY_POINTS_ACCOUNT_FORBID("0131002","用户积分账户禁用"),
	MP_MY_POINTS_ACCOUNT_FREEZE("0131003","用户积分账户冻结"),
	MP_MY_POINTS_ACCOUNT_REMAIN_LACK("0231004","您的积分不足"),
	
	MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_VALID("0131009","积分兑换金额验证不通过"),
	MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND("0131010","未找到积分兑换配置信息"),
	MP_POINTS_OVER_MAX_EXCHANGE_NUM("0231101","您输入的积分不能高于最高兑换积分"),
	MP_POINTS_BELOW_MIN_EXCHANGE_NUM("0231102","您输入的积分不能低于最低兑换积分"),
	MP_POINTS_OVER_MAX_EXCHANGE_FREQUENCY("0231103","您的兑换次数已用完"),
	MP_POINTS_OVER_YEAR_EXCHANGE_FREQUENCY("0231104","您本年度的兑换次数已用完"),
	MP_POINTS_OVER_MONTH_EXCHANGE_FREQUENCY("0231105","您本月的兑换次数已用完"),
	MP_POINTS_OVER_DAY_EXCHANGE_FREQUENCY("0231106","您今天的兑换次数已用完"),
	
	MP_PARVALUE_NOT_EXIST("0131107","充值面值不存在"),
	MP_ADD_MOBILE_TOPUP_ORDER_FAILURE("0131108","添加充值订单失败"),
	MP_INTEGRAL_NOT_EXIST("0131109","积分类型不存在"),
	MP_RECHARGE_ERROR("0131110","充值异常"),
	MP_POINTS_CODE_ERROR("0131111","充值面额与可积分类别不符"),
	MP_TOPUP_FREQUENCY_OVER("0231112", "会员积分系统升级中，手机充值暂停服务，敬请期待"), //会员积分系统升级中，手机充值暂停服务，敬请期待
	MP_YISHANG_CANRECHARGE_ERROR("0131113", "手机号码查询异常"),
	MP_YISHANG_CANRECHARGE_NOT_VALID("0231114", "抱歉，无法识别该号码运营商"),
	MP_YISHANG_RECHARGE_FAILURE("0131115", "充值失败"),
	MP_YISHANG_PHONRN_ERROR("0231116", "对不起，目前只能为账号绑定手机号码充值"),
	MP_YISHANG_CONSUMPTIONORDER_NOT_EXIST("0131117", "消费订单不存在"),
	MP_YISHANG_ORDER_NOT_EXIST("0131118", "消费订单关联手机充值订单不存在"),
	MP_YISHANG_ORDER_NOT_WEIT("0131119", "手机充值订单,非待提交状态"),
	MP_YISHANG_PARVALUE_CODE_NOT_EXIST("0131120", "手机充值面值code不存在"),
	
	
	
	
	LOAN_HAS_UNTREATED("0240000", "申请失败，您当前还有未处理的申请"),
	LOAN_FREQUENCY_OVER("0240001", "借款申请次数已超限，请明日再试"),
	
	PAYMENT_ORDER_NOT_EXIST("0150000", "支付订单不存在"),
	PAYMENT_WITHDRAW_EXCESS("250123", "提现超额，可提现金额为%s元"),
	
	/**
	 *   积分系统相关错误码 end
	 */
	
	SUCCESS("200", "操作成功"),
	FAILURE("500", "操作失败"),
	NOT_AUTHORIZED("401","未认证"),
	NOT_VALID_TOKEN("1029","token验证不通过"),
	NOT_VALID_USER("10291","userId为空或不通过"),
	EMPTY_PARAM("601","请求参数为空"),

	/**
	 * 企业相关
	 */
	COMPANY_CODE_EXISTS("70000", "企业已存在")
	;

	private String code;

    private String message;

    ResponseCode(String code, String message) {
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
