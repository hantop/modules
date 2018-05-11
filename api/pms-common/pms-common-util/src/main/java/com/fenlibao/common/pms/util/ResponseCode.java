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
package com.fenlibao.common.pms.util;

/**
 * @ClassName: ResponseCode
 * @Description: 业务返回码规范
 * @author: laubrence
 * @date: 2015-10-26 上午9:53:18  
 */
public enum ResponseCode {

    //前缀表示消息的权重（1=给开发者，2=用户弱提示，3=用户强提示）
    //定义状态码之前先确认相应的状态有没有

    /** 	错误码前缀	       错误码描述	                     错误码
     COMMON_	           公用错误码	                     10xxx
     USER_	           用户相关错误码	                 11xxx
     TRADE_	           交易相关错误码	                 12xxx
     WEIXIN_            微信公众号相关错误码            13xxx
     ACTIVITY_          活动相关错误码	                 14xxx
     BID_               标相关错误码                   2xxxx
     KDB_	           开店宝相关错误码	             20xxx
     XJB_	           薪金宝相关错误码	             21xxx
     HQB_	           活期宝相关错误码	             22xxx
     ZQZR_	           债权转让相关错误码              23xxx
     ORDER_             订单相关错误码		             30xxx
     MP_                积分系统相关错误码	             31xxx
     PAYMENT_           支付模块			             5xxxx

     OTHER_             其他                          99xxx

     NOTE: 1 10 x xx
     1  开发者看
     10 为大类型，如common
     x  为子类型，如1为不合法参数。。   2.为缺少参数。。  3.
     xx 为错误码，如01、02
     */

////公用错误码(10xxx) start
    //params
    EMPTY_PARAM("110200","请求参数为空"),
    COMMON_PARAM_TYPE_WRONG("110101", "参数类型错误"),
    COMMON_PARAM_WRONG("110102", "参数错误"),
    COMMON_PARAM_DECRYPT_FAILURE("110125","参数解密失败"),
    COMMON_PARAM_ENCRYPT_FAILURE("110126","参数加密失败"),
    //token
    COMMON_NOT_AUTHORIZED("110104","未认证"),
    COMMON_NOT_VALID_TOKEN("110105","用户token验证不通过"),
    COMMON_NOT_VALID_ACCESSTOKEN("110106","accessToken不合法"),
    COMMON_ACCESSTOKEN_TIMEOUT("110107","accessToken已过期"),
    COMMON_ACCESSTOKEN_BUILD_FAILURE("110308","accessToken创建失败"),
    COMMON_USER_TOKEN_BUILD_FAILURE("110309","用户登录token生成失败"),
    COMMON_ACCESSTOKEN_CLIENT_TYPE_NOT_SAME("110110","clientType不能和targetClientType相同"),
    COMMON_ACCESSTOKEN_CLIENT_TYPE_INVALID("110111","clientType不匹配"),
    //captcha
    COMMON_CAPTCHA_KEY_WRONG("110112", "验证码key错误"),
    COMMON_CAPTCHA_INVALID("210113", "验证码不正确"),
    COMMON_CAPTCHA_TIMEOUT("210114", "验证码已失效"), //验证码过期
    COMMON_CAPTCHA_TYPE_ERROR("110115", "验证码类型不存在"),
    COMMON_PHONE_CAPTCHA_FREQUENTLY("210116", "验证码发送太频繁，请稍后再试"),
    COMMON_PHONE_CAPTCHA_MAXTIME("310117", "验证码发送次数超限，请明日再试"),
    COMMON_PHONE_CAPTCHA_HOUR_MAXTIME("310118", "验证码发送太频繁，请1小时后再试"),
    COMMON_PHONE_CAPTCHA_HALFOUR_MAXTIME("210119", "验证码发送太频繁，请半小时后再试"),

    COMMON_PLATFORM_ACCOUNT_NOT_EXIST("110120","平台账号不存在"),
    COMMON_PLATFORM_WLZH_ACCOUNT_NOT_EXIST("110121","平台往来账号不存在"),
    COMMON_PLATFORM_ACCOUNT_BALANCE_LACK("110122","系统平台往来账户余额不足"),
    COMMON_OPERATION_IS_TOO_FREQUENT("210123", "操作太频繁，请稍候再试"), //操作过于频繁
    COMMON_TEXT_LENGTH_TOO_LONG("210124","文本长度过长"),
    COMMON_PHONE_FORMAT_WRONG("210127","手机号码不正确"), //手机号码格式不正确

    COMMON_RECORD_NOT_EXIST("110128", "记录不存在"),
    COMMON_GRAPHICS_CAPTCHA_INVALID("210129", "图形验证码不正确"),
    COMMON_GRAPHICS_CAPTCHA_TIMEOUT("210130", "图形验证码已过期"), //验证码过期
////公用错误码 end


    ////用户相关错误码(11xxx) start
    //password
    USER_PASSWORD_ERROR_("211000", "密码不正确"),//密码错误
    USER_PASSWORD_ERROR("211100", "密码必须为6-16位字母、数字组合"),//密码错误
    USER_TRADE_PASSWORD_ERROR("311101","交易密码不正确，建议您重置交易密码"),//交易密码错误
    USER_NOT_SET_TRADE_PASSWORD("311102", "您尚未设置交易密码"),//未设置交易密码
    USER_TRADE_PASSWORD_TYPE_WRONG("211103","交易密码为6个数字"),//交易密码类型不正确
    //phonenum
    USER_PHONE_UNAUTH("211104", "手机未认证"),
    USER_REFERRAL_PHONENUM_SAME("211105", "手机号不能与推荐人手机号一致"),
    USER_PHONE_REGISTERED("311106", "您手机号码已注册，请点击登录"),
    USER_PHONENUM_IS_EXIST("211108", "手机号码已存在"),
    //account
    USER_AUTH_REALNAME_EXIST("211109","您已经实名认证过"),
    USER_AUTH_REALNAME_FAIL("30813", "实名认证失败"),
    USER_AUTH_UN_MATCH("30815", "姓名或者身份证信息不一致"),
    GUOXIN_LIB_NULL("30816", "实名认证国信库中无此号"),
    GUOXIN_CHANNEL_ERROR("30817", "实名认证国信通道出现问题"),
    USER_WLZH_ACCOUNT_NOT_EXIST("111110","用户往来账户不存在"),
    USER_WLZH_ACCOUNT_BALANCE_LACK("111111","用户往来账户余额不足"),
    USER_IS_HAVE_BID("211112","您不是首次投资"),
    USER_NOT_EXIST("111113", "用户不存在"), //开发者看（有时候不能让用户看）
    USER_NOT_EXIST_FOR_USER("211113", "用户不存在"), //给用户看的
    USER_SDZH_ACCOUNT_NOT_EXIST("111114","用户锁定账户不存在"),
    USER_ACCOUNT_BALANCE_INSUFFICIENT("311115", "您账户余额不足，请及时充值"),//账户余额不足

    USER_IDENTITY_VERIFY_FAIL("211116", "认证失败"),//实名验证失败
    USER_IDENTITY_UNAUTH("311117", "您尚未通过实名认证"),//未实名认证
    USER_SDZH_ACCOUNT_BALANCE_LACK("111118","用户锁定账户余额不足"),
    USER_REGISTER_SUCCESS_RELOGIN("211119","注册成功，请重新登录"),
    USER_IDCARD_FORMAT_ERROR("211120","身份证号不正确"),//身份证格式错误
    USER_EMAIL_IS_EXIST("211121", "邮箱已存在"),
    USER_NOT_SUPPORT_TP_FIND_PWD("211122", "暂不支持第三方找回密码"),
    USER_ACCOUNT_NOT_EXIST("311123", "账号不存在，请重新输入或注册新账号"),
    USER_PASSWORLD_OLD_NEW_SAME("211124", "新旧密码相同"),
    USER_PASSWORLD_OLD_WRONG("211125", "原密码错误"),
    USER_NAME_FORMAT_WRONG("211126", "用户名格式错误"),
    USER_NAME_NOT_BE_CHANGED("211127", "用户名不可更改"),
    USER_NAME_IS_EXIST("211128", "用户名已存在"),
    USER_TRADE_PWD_WRONG_COUNT("311129", "交易密码不正确，还可以输入%s次"),
    USER_RISK_UNTEST("311118","根据《网络借贷信息中介机构业务活动管理暂行办法》要求，为加强网络借贷的监督管理、防范资金风险，请投资人完成风险测试后再进行投资。"),
////用户相关错误码 end


    ////交易相关错误码(12xxx) start
    LOAN_HAS_UNTREATED("212300", "您当前还有未处理的借款申请"),
    LOAN_FREQUENCY_OVER("212301", "借款申请次数已超限，请明日再试"),
////交易相关错误码 end


    ////微信公众号相关错误码(13xxx)  start
    WEIXIN_UNBIND("113100","openId未绑定"),
    WEIXIN_BIND_FAILED("213101","微信绑定失败"),
    WEIXIN_NOT_BIND("213102","微信未绑定"),
    WEIXIN_IS_BINDED("213103","微信已绑定过"),
    WEIXIN_CANCEL_SUCCESS("213104", "取消成功"),
////微信公众号相关错误码  end


    ////营销活动相关错误码(14xxx)  start
    ACTIVITY_RECORD_EXIST("214100", "记录已存在"),
    ACTIVITY_LOTTERY_ACTIVITY_NOT_EXIST("214101","抽奖活动不存在"),
    ACTIVITY_LOTTERY_ACTIVITY_HAVE_NOT_STARTED("214102","抽奖活动未开始"),
    ACTIVITY_LOTTERY_ACTIVITY_HAVE_EXPIRE("214103","抽奖活动已结束"),
    ACTIVITY_USER_LOTTERY_DRAW_ERROR("114104","抽奖处理失败"),
    ACTIVITY_USER_LOTTERY_TIMES_LACK("114105","用户抽奖次数不足"),
    ACTIVITY_LOTTERY_LACK_EFFECTIVE_PRIZES("114106","抽奖没有有效奖品信息"),
    ACTIVITY_LOTTERY_DRAW_RECORD_NOT_FOUND("214107","抽奖记录不存在"),
    ACTIVITY_LOTTERY_DAY_RICHEST_BOARD_NOT_START("214108","未到活动开榜时间"),
    ACTIVITY_LOTTERY_DAY_RICHEST_BOARD_EXPIRED("214109","活动开榜时间已结束"),
    ACTIVITY_OLYMPIC_REDPACKET_UNUSED("214110","领取的红包还没使用"),
    ACTIVITY_END("214111","活动已结束"),
    ACTIVITY_NOT_ACTIVITY_TIME("214112","当前时间不在活动时间范围"),
    ACTIVITY_NO_START("214113","活动未开始"),
    ACTIVITY_FRUIT_BE_PICKED("214114","该分利果已被摘取或不存在"),
    ACTIVITY_FRUIT_PICK_ERROR("214115","摘取分利果失败"),
////营销活动相关错误码  end


    ////标相关错误码(2xxxx)  start
    BID_INVESTMENT_FAILURE("220300", "投资失败"),
    BID_DETAILS_EMPTY("220301","该详情不存在"),
    BID_NOT_EXIST("120302", "指定的标不存在"),
    BID_INVESTMENT_AMOUNT_GT0("120103", "投标金额必须大于零"),
    BIT_LOAN_OVERDUE("220304", "您有借款逾期未还，不满足投资条件"),
    BID_INVESTMENT_AMOUNT_LT100("220105", "投资金额需为100元起"),
    BID_BORROWER_CANNOT_INVESTMENT("220306", "您是该项目借款人，不满足投资条件"),
    BID_OUT_OF_DATE("220307", "您加入的标已过期，请于下次投资日加入"),
    BID_FULLED("220308", "该项目已满额，请投资其他项目"),
    BID_INVEST_AMOUNT_TOO_MUCH("220309", "投资金额已超过剩余可投金额"),
    BID_CONDITIONS_NOT_SATISFIED("220310", "不满足投资条件"),

    BID_RED_ENVELOPE_IS_USED("220311", "返现券已使用"),
    BID_RED_ENVELOPE_IS_OVERDUE("220312", "返现券已过期"),
    BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED("120313", "返现券使用条件不满足"),
    BID_RED_ENVELOPE_NOT_EXIST("120314", "红包不存在"),
    BID_RED_ENVELOPE_TYPE_NOT_EXIST("120114", "红包类型不存在"),

    BID_NOVICE_NOT_ZQZR("220315","新手标不能债权转让"),
    BID_NOVICE_OVER_MAX_AMOUNT("220116","新手标投标金额大于最大限制金额"),
    BID_NOVICE_NOT_USE_FXHB("220317","新手标不能使用返现券"),
    BID_NOVICE_NOT_NEW_USER("220318","新手标只限新用户投资"),

    BID_COUPON_CONDITIONS_NOT_SATISFIED("120319", "加息券使用条件不满足"),
    BID_COUPON_NOT_EXIST("120320", "加息券不存在"),
    BID_COUPON_IS_USED("120321", "加息券已使用"),
    BID_COUPON_IS_OVERDUE("120322", "加息券已过期"),
    BID_NOVICE_NOT_USE_JXQ("120323","新手标不能使用加息券"),

    BID_INVESTMENT_AMOUNT_INTEGER("220324","投标金额必须为整数"),


    //债权转让相关错误码 start
    ZQZR_CSGD("223002","转让次数超限，该债权不可转让"),
    ZQZR_SJWD("223003","持有不足3天，该债权不可转让"),
    TCJH_SJWD("223003","持有天数不足，该计划不可退出"),
    ZQZR_SJWD_DAY_1("223004","持有不足1天，该债权不可转让"),
    ZQZR_SJKD("223005","3天内到期，该债权不可转让"),
    TCJH_SJKD("223005","3天内回款，该计划不可退出"),
    ZQZR_TRANSFER_VALUE_UNDER_ZERO("123006","转让价格不能小于0"),
    ZQZR_IS_TRANSFER("223007","该债权正在转让"),
    ZQZR_IS_REPAYMENT("223008","该债权正在还款中"),
    ZQZR_ZQVALUE_LESS_TRANSFERVALUE("123009","转让金额范围异常"),
    ZQZR_PASSEDEARNING_UNDER_ZERO("223010","债权已过天数收益不能小于0"),
    ZQZR_NOT_WH("123011","不存在未还记录"),
    ZQZR_NOT_SUPPORT_BY_REPAYMENT("223012","该还款方式暂不支持债权转让"),
    ZQZR_APPLY_DATA_NOT_FOUND("223101","该债权已被购买或者已被撤销"), //线上债权转让申请不存在
    ZQZR_OVER_REPAY("223102","借款逾期未还"),
    ZQZR_APPLY_NOT_ZRZ("223103","该债权已被撤销"),//线上债权转让申请不是转让中状态,不能转让
    ZQZR_APPLY_HAVE_OVER_DATE("223104","项目已逾期，该债权不可转让"),//线上债权转让申请已到截至日期,不能转让
    TCJH_OVER_DATE("223104","债权有逾期，该计划不可退出"),
    ZQZR_CANNOT_BUY_MYSELF("223105","不能购买自己的债权"),
    ZQZR_ZQ_INFO_NOT_FOUND("223106","债权信息不存在"),
    ZQZR_ZQ_VALUE_NOT_ZERO("223107","债权价值不能为0"),
    ZQZR_ZQ_REPAY_PLAN_NOT_FOUND("123108","债权还款计划不存在"),
    ZQZR_SRR_WLZH_ACCOUNT_NOT_EXIST("123109","受让人用户往来账户不存在"),
    ZQZR_SRR_USER_WLZH_ACCOUNT_BALANCE_LACK("123110","受让人用户往来账户余额不足"),
    ZQZR_CRR_USER_WLZH_ACCOUNT_NOT_EXIST("123111","出让人用户往来账户不存在"),
    ZQZR_CRR_USER_WLZH_ACCOUNT_BALANCE_LACK("123112","出让用户往来账户余额不足"),
    ZQZR_ZQ_HAVE_CANCEL("223113","该债权已被撤销"),
    ZQZR_APPLY_HAVE_OVER_FIVE_DAY("223114","线上债权转让申请已超过5天,不能转让"),
    ZQZR_ASSIGNED("223115","该债权已被购买"),//债权已转让
    ZQZR_IS_NOVICEBID("223116","新手标,不能转让"),
    TCJH_IS_NOVICEBID("223116","新手计划,不能退出"),
    ZQZR_IS_XFXD("223116","消费信贷标,不能转让"),
    ZQZR_IS_DOWN("223117","当前没有需要平台购买的随时退出标债权"),
    TCJH_COTAIN_NOT_REPAYMENT("223108","计划中含有未回款的标，该计划不可退出"),
    FT_ZQ_NOT_FOUND("123109","找不到债权对应的用户计划"),

    //债权转让相关错误码  end
    //投资分享 start
    BID_SHARE_WITHOUT_INVESTMENT("124301", "该用户没有投资该标"),
    BID_SHARE_RECORD_NOT_EXIST("124302", "投资分享红包记录不存在"),
    BID_SHARE_FINISH("224303", "红包已抢光啦"),
    BID_SHARE_ALREADY_GET("224304", "您已领过啦"),
    BID_SHARE_ALREADY("124305", "已经分享过红包"),
    //投资分享 end
    //投资定向标
    BID_DIRECTIONAL_TOTAL_USER_ASSETS("324306","定向标只限符合条件的用户投资"),
    //投资随时退出标
    BID_ANYTIME_QUIT_USER_INVEST("324307","只限符合条件的用户投资"),
    //投资定向标 end
////标相关错误码 end


    ////订单信息相关错误码(30xxx) start
    ORDER_INFO_NOT_FOUND("130001","订单详细信息不存在"),
////订单信息相关错误码 end


    ////积分系统相关错误码(31xxx) start
    MP_MY_POINTS_ACCOUNT_NOT_FOUND("131001","用户积分账户不存在"),
    MP_MY_POINTS_ACCOUNT_FORBID("131002","用户积分账户禁用"),
    MP_MY_POINTS_ACCOUNT_FREEZE("331003","积分异常，请联系客服400-930-5559"), //1 用户积分账户冻结 //应运营需求
    MP_MY_POINTS_ACCOUNT_REMAIN_LACK("231004","您的积分不足"),

    MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_VALID("131009","积分兑换金额验证不通过"),
//    MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND("131010","未找到积分兑换配置信息"),
    MP_POINTS_EXCHANGE_CASH_CONFIG_NOT_FOUND("231010","活动已结束"),
    MP_POINTS_OVER_MAX_EXCHANGE_NUM("231101","您输入的积分不能高于最高兑换积分"),
    MP_POINTS_BELOW_MIN_EXCHANGE_NUM("231102","您输入的积分不能低于最低兑换积分"),
    MP_POINTS_OVER_MAX_EXCHANGE_FREQUENCY("231103","您的兑换次数已用完"),
    MP_POINTS_OVER_YEAR_EXCHANGE_FREQUENCY("231104","您本年度的兑换次数已用完"),
    MP_POINTS_OVER_MONTH_EXCHANGE_FREQUENCY("231105","您本月的兑换次数已用完"),
    MP_POINTS_OVER_DAY_EXCHANGE_FREQUENCY("231106","您今天的兑换次数已用完"),

    MP_PARVALUE_NOT_EXIST("131107","充值面值不存在"),
    MP_ADD_MOBILE_TOPUP_ORDER_FAILURE("131108","添加充值订单失败"),
    MP_INTEGRAL_NOT_EXIST("131109","积分类型不存在"),
    MP_RECHARGE_ERROR("131110","充值异常"),
    MP_POINTS_CODE_ERROR("131111","充值面额与可积分类别不符"),
    MP_TOPUP_FREQUENCY_OVER("231112", "会员积分系统升级中，手机充值暂停服务，敬请期待"), //会员积分系统升级中，手机充值暂停服务，敬请期待
    MP_YISHANG_CANRECHARGE_ERROR("131113", "手机号码查询异常"),
    MP_YISHANG_CANRECHARGE_NOT_VALID("231114", "抱歉，无法识别该号码运营商"),
    MP_YISHANG_RECHARGE_FAILURE("131115", "兑换失败"),
    MP_YISHANG_PHONRN_ERROR("231116", "对不起，目前只能为账号绑定手机号码充值"),
    MP_YISHANG_CONSUMPTIONORDER_NOT_EXIST("131117", "消费订单不存在"),
    MP_YISHANG_ORDER_NOT_EXIST("131118", "消费订单关联手机充值订单不存在"),
    MP_YISHANG_ORDER_NOT_WEIT("131119", "手机充值订单,非待提交状态"),
    MP_YISHANG_PARVALUE_CODE_NOT_EXIST("131120", "手机充值面值code不存在"),

    MP_MY_POINTS_QUOTA_LACK("231121","您的可用积分不足，请确认！"),
    MP_YISHANG_OVERTOP("231122", "话费兑换金额已超出每月最高限制"),

////积分系统相关错误码 end


    ////支付相关(50xxx) start
    PAYMENT_ORDER_NOT_EXIST("150300", "支付订单不存在"),

    PAYMENT_AMOUNT_FORMAT_ERROR("250101","金额格式错误"),
    PAYMENT_NOT_SUPPORT_BANK("250102", "暂不支持该银行"),
    PAYMENT_QUERY_CARD_INFO_FAIL("250003", "查询银行卡信息失败"),
    PAYMENT_WITHDRAW_FAIL("350004", "提现失败，请联系客服400-930-5559"),//提现失败
    PAYMENT_WITHDRAW_LIMIT("250105", "提现金额不能低于%s元，不能高于%s元"),
    PAYMENT_LOAN_OVERDUE("250306","有逾期借款未还"),
    PAYMENT_BANK_CARD_NOT_EXIST("150307","银行卡不存在"),
    PAYMENT_UNBOUND_BANK_CARD("350308","您尚未绑定银行卡"),//未绑定银行卡
    PAYMENT_BIND_CARD_ONE("250109","只能添加一张银行卡"),
    PAYMENT_BANK_REPEAT("250111","当前银行卡号已存在"),
    PAYMENT_AMOUNT_LESSTHAN_POUNDAGE("150112","提现金额不能小于提现手续费"),
    PAYMENT_INSUFFICIENT_BALANCE("250113","账户余额不足"),
    PAYMENT_OFFLINE_CANNOT_WITHDRAW("250114","线下充值金额%s小时内不可提现.如有问题,请联系客服"),
    PAYMENT_BLACKLIST("350115","您的账户资金已被锁定，请联系客服400-930-5559"),
    PAYMENT_BANK_CARD_FORMAT_WRONG("250116", "银行卡号不正确"),//银行卡格式错误
    PAYMENT_BANK_CARD_CANNOT_EMPTY("250117", "银行卡号不能为空"),
    PAYMENT_RECHARGE_AMOUNT_SCOPE("250118", "充值金额需为%s-%s元"),
    PAYMENT_WITHDRAW_LIMIT_LOW("250119", "提现金额不能低于1元"),
    PAYMENT_RECHARGE_LIMIT_LOW("250120", "充值金额不能低于10元"),
    PAYMENT_RECHARGE_LIMIT_EXCEED("250121", "充值金额已超过银行限额"),
    PAYMENT_RECHARGE_LIMIT_NOT_EXIST("150121", "充值限额信息不存在"),
    PAYMENT_RECHARGE_ONLY_SUPPORT_DEBIT_CARD("250122", "目前只支持储蓄卡"),
    PAYMENT_WITHDRAW_EXCESS("350123", "提现超额，可提现金额为%s元"),
    PAYMENT_WITHDRAW_CANNOT_FIND_SUBBRANCH("250124", "找不到支行信息"),
    PAYMENT_WITHDRAW_CANNOT_FIND_SUBBRANCH_PROVINCE("150125", "找不到支行信息：省编码"),
    PAYMENT_WITHDRAW_REQUEST_FAIL_LLP("150125", "向连连发起请求出现异常"),
    PAYMENT_HUAXING_CANNOT_FIND("150126","当前没有启用华兴通道"),
    PAYMENT_CHANNEL_CANNOT_FIND("150127","支付通道使用错误"),
////支付相关 end


    ////其他(99xxx) start
    OTHER_DBFILE_TYPE_NO_EXISTS("199001", "DB文件类型不存在"),

    COMMON_INTERFACE_OUT_OF_SERVICE("199002","该接口已停止服务"),
    OTHER_CHANNEL_OUT_OF_SERVICE("399003", "该通道已停止服务，请升级到最新版本"),
    OTHER_VERSION_TOO_OLD("399004", "该版本已不再支持，请升级到最新版本"),
    OTHER_REQUEST_METHOD_NOT_SUPPORTED("199005", "不支持当前请求方式"),
////其他 end


    SUCCESS("200", "请求成功"),
    FAILURE("500", "请求失败");

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
