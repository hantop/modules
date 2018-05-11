package com.fenlibao.p2p.model.xinwang.enums.exception;

import com.fenlibao.p2p.model.api.global.IResponseMessage;

public enum XWResponseCode implements IResponseMessage {

    //前缀表示消息的权重（1=给开发者，2=用户弱提示，3=用户强提示）
    //定义状态码之前先确认相应的状态有没有

    /** 	错误码前缀	       错误码描述	                     错误码
     COMMON_	           公用错误码	                     10xxx
     USER_	           用户相关错误码	                 11xxx
     TRADE_	           交易相关错误码	                 12xxx
     WEIXIN_            微信公众号相关错误码            13xxx
     ACTIVITY_          活动相关错误码	                 14xxx
     BID_               标相关错误码                   2xxxx
     ORDER_             订单相关错误码		             30xxx
     MP_                积分系统相关错误码	             31xxx
     PAYMENT_           支付模块			             5xxxx

     XW_TRADE_          新网交易相关错误码              66xxx

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
    COMMON_PHONE_FORMAT_WRONG("210127","手机号码不正确"), //手机号码格式不正确

    COMMON_RECORD_NOT_EXIST("110128", "记录不存在"),
    COMMON_VERIFY_SIGN_FAIL("110129", "验签失败"),
    COMMON_REQUEST_FAIL("110130", "请求新网接口失败"),
    COMMON_ORDER_STATUS_WRONG("110131","订单状态不正确"),
////公用错误码 end

////资金账户相关 start
    FUND_ACCOUNT_NOT_EXIST("111001", "存管资金账户不存在"),
    FUND_FREEZE_FAIL("111002","冻结新网资金失败" ),
    USER_XW_ACCOUNT_NOT_EXIST("111115", "新网账户不存在"), //开发者看（有时候不能让用户看）
////资金账户相关 end

    ////支付相关(50xxx) start
    PAYMENT_ORDER_NOT_EXIST("150300", "支付订单不存在"),

    PAYMENT_AMOUNT_FORMAT_ERROR("250101","金额格式错误"),
    PAYMENT_NOT_SUPPORT_BANK("250102", "暂不支持该银行"),
    PAYMENT_QUERY_CARD_INFO_FAIL("250003", "查询银行卡信息失败"),
    PAYMENT_WITHDRAW_FAIL("250004", "提现失败，请联系客服400-930-5559"),//提现失败
    PAYMENT_WITHDRAW_LIMIT("250105", "提现金额不能低于%s元，不能高于%s元"),
    PAYMENT_LOAN_OVERDUE("250306","有逾期借款未还"),
    PAYMENT_BANK_CARD_NOT_EXIST("150307","银行卡不存在"),
    PAYMENT_UNBOUND_BANK_CARD("250308","您尚未绑定银行卡"),//未绑定银行卡
    PAYMENT_BIND_CARD_ONE("250109","只能添加一张银行卡"),
    PAYMENT_BANK_REPEAT("250111","当前银行卡号已存在"),
    PAYMENT_AMOUNT_LESSTHAN_POUNDAGE("150112","提现金额不能小于提现手续费"),
    PAYMENT_INSUFFICIENT_BALANCE("250113","账户余额不足"),
    PAYMENT_OFFLINE_CANNOT_WITHDRAW("250114","线下充值金额%s小时内不可提现.如有问题,请联系客服"),
    PAYMENT_BLACKLIST("250115","用户已被拉黑不能提现"),
    PAYMENT_BANK_CARD_FORMAT_WRONG("250116", "银行卡号不正确"),//银行卡格式错误
    PAYMENT_BANK_CARD_CANNOT_EMPTY("250117", "银行卡号不能为空"),
    PAYMENT_RECHARGE_AMOUNT_SCOPE("250118", "充值金额需为%s-%s元"),
    PAYMENT_WITHDRAW_LIMIT_LOW("250119", "提现金额不能低于1元"),
    PAYMENT_RECHARGE_LIMIT_LOW("250120", "充值金额不能低于%s元"),
    PAYMENT_RECHARGE_LIMIT_EXCEED("250121", "充值金额已超过银行限额"),
    PAYMENT_RECHARGE_LIMIT_NOT_EXIST("150121", "充值限额信息不存在"),
    PAYMENT_RECHARGE_ONLY_SUPPORT_DEBIT_CARD("250122", "目前只支持储蓄卡"),
    PAYMENT_PUBLICKEY_WRONG("150123", "解密公钥不正确"),
    PAYMENT_BIND_CARD_FAIL("250124", "绑定银行卡失败"),
    PAYMENT_REQUEST_FAIL("150125", "请求接口失败"),
    PAYMENT_WITHDRAW_RESULT_QUERY_FAIL("150126", "查询提现结果失败"),
    PAYMENT_TOPUP_FAIL("250126", "充值失败"),
    PAYMENT_IS_BINDING_BAOFOO("250127", "您已绑过卡"),
    PAYMENT_RECHARGE_LIMIT_EXCEED_CURDATE("250128", "当天充值金额已超过银行限额"),
    PAYMENT_RECHARGE_RESULT_QUERY_FAIL("150129", "查询充值结果失败"),
    PAYMENT_UNBIND_NO_CARD("250130", "用户没有绑卡"),
    PAYMENT_UNBIND_FAIL("250131", "解绑失败"),
////支付相关 end

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

    BID_RED_ENVELOPE_IS_USED("220311", "返现券已使用"),
    BID_RED_ENVELOPE_IS_OVERDUE("220312", "返现券已过期"),
    BID_RED_ENVELOPE_CONDITIONS_NOT_SATISFIED("120313", "返现券使用条件不满足"),
    BID_RED_ENVELOPE_NOT_EXIST("120314", "红包不存在"),
    BID_RED_ENVELOPE_TYPE_NOT_EXIST("120114", "红包类型不存在"),

    BID_NOVICE_NOT_ZQZR("220315","新手标不能债权转让"),
    BID_NOVICE_OVER_MAX_AMOUNT("220116","新手标出借金额大于最大限制金额"),
    BID_NOVICE_NOT_USE_FXHB("220317","新手标不能使用返现券"),
    BID_NOVICE_NOT_NEW_USER("220318","新手标只限新用户出借"),
    
    BID_TYPE_NOT_DM("120319","该标非存管"),
    BID_INVEST_RECORD_NOT_EXIST("120320","出借记录不存在"),
    BID_REPAY_PLAN_NOT_EXIST("120321","还款计划不存在"),
    BID_ESTABLISH_FAIL("220322","标%s发布失败：%s"),

    COUPON_REDPACKET_INCORRECT("121101", "返现券信息不正确"),
    COUPON_REDPACKET_INVESTDEADLINE_INCORRECT("121102", "返现券出借期限不正确"),
    COUPON_REDPACKET_INVESTAMOUNT_INCORRECT("121103", "返现券出借金额不正确"),
    COUPON_REDPACKET_OVERDUE("121104", "返现券已过期"),
    COUPON_INFO_INCORRECT("121105", "找不到符合条件的加息券"),
    COUPON_CONDITIONS_OF_USE_INCORRECT("121106", "优惠券使用条件不正确"),
    
    TRADE_OPERATION_REPEAT("212301","请勿重复操作"),
    TRADE_FLOW_CONDITIONS_NOT_SATISFIED("212302","不是出借中或待放款状态,不能流标"),
    TRADE_REPAY_CONDITIONS_NOT_SATISFIED("212303","不是还款中状态,不能还款"),
    TRADE_MAKE_A_LOAN_CONDITIONS_NOT_SATISFIED("212304","不是待放款状态,不能放款"),
    TRADE_RELEASE_BID_CONDITIONS_NOT_SATISFIED("212305","该标不是待发布或预发布状态，不能进行发布操作"),
    TRADE_DISPLAYED_BID_NOT_RELEASE("212306","该标已显示，不能再进行发布操作"),
    TRADE_DISPLAYED_BID_NOT_MODIFY("212307","该标已显示，不能再进行修改"),
    TRADE_RELEASE_TIME_BEFORE_CURRENT_TIME("212308","可出借标时间不能小于当前时间"),
    TRADE_REPAY_BORROWER_WRONG("112309","申请还款的用户非该标借款人"),
    TRADE_REPAY_NOT_TIME_YET("212310","还未到还款时间,不能还款"),
    TRADE_PREPAY_NOT_ENOUGH_A_MONTH("212311","借款满1个自然月后才可申请提前还款"),
    TRADE_REPAY_IN_PROGRESS_OF_REPAY("212312","该标正在还款，请勿重复操作"),

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
    USER_WLZH_ACCOUNT_NOT_EXIST("111110","用户往来账户不存在"),
    USER_WLZH_ACCOUNT_BALANCE_LACK("111111","用户往来账户余额不足"),
    USER_IS_HAVE_BID("211112","您不是首次出借"),
    USER_NOT_EXIST("111113", "用户不存在"), //开发者看（有时候不能让用户看）
    USER_NOT_EXIST_FOR_USER("211113", "用户不存在"), //给用户看的
    USER_SDZH_ACCOUNT_NOT_EXIST("111114","用户锁定账户不存在"),
    USER_ACCOUNT_BALANCE_INSUFFICIENT("311115", "您账户余额不足，请及时充值"),//账户余额不足
    USER_E_ACCOUNT_NOT_EXIST("111115", "华兴E账户不存在"), //开发者看（有时候不能让用户看）

    USER_IDENTITY_VERIFY_FAIL("211116", "认证失败"),//实名验证失败
    USER_IDENTITY_UNAUTH("311117", "您尚未通过实名认证"),//未实名认证
    USER_SDZH_ACCOUNT_BALANCE_LACK("111118","用户%s锁定账户余额不足"),
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
    USER_HAS_BEEN_BIND_CARD("211130", "您已绑卡"),

    ORDER_TRADE_TYPE_WRONG("130301","订单交易类型错误"),
    ORDER_NOT_EXIST("130302","订单不存在"),
    ORDER_STATUS_WRONG("130303","订单状态错误"),

    XW_TRADE_UNFREEZE_TRADE_PASSWORD_WRONG("266001","交易密码解冻错误：%s"),
    XW_QUERY_TRANSACTION_WRONG("266002","单笔交易查询错误：%s"),
    XW_QUERY_PROJECT_INFORMATION_WRONG("266003","标的信息查询错误：%s"),
    XW_SYNC_TRANSACTION_WRONG("166004","单笔交易错误：%s"),
    XW_ASSEMBLE_REQUEST_PARAM_WRONG("166005","组装参数错误"),
    XW_PRE_TRANSACTION_WRONG("266006", "授权预处理错误：%s"),
    XW_CHANGE_PROJECT_STATUS_WRONG("166007","标%s改变状态为%s失败：%s"),
    XW_ZQZR_CANCEL_FAIL("166008", "取消债权转让失败：%s"),
    XW_ZQZR_FAIL("166009", "债权转让失败：%s"),
    XW_REPAY_PRETREATMENT_FAIL("166010", "标%s还款预处理失败：%s"),
    XW_REPAY_ACCEPT_FAIL("166011", "标%还款请求受理失败，请重新提交失败的批次"),
    XW_REPAY_MARKETING_ACCEPT_FAIL("166012", "标%s还款营销请求受理失败，请重新提交失败的批次"),
    XW_BANKCODE_NOT_SUPPORT("266007", "暂不支持该银行"),
    XW_CANCEL_PRE_TRANSACTION_FAIL("166013", "取消交易预处理失败：%s"),
    XW_ENTRUST_IMPORT_USER_FAIL("266014","个人委托开户失败：%s"),
    XW_ENTRUST_AUTHORIZATION_FAIL("266015","委托开户授权失败：%s"),
    XW_ENTRUST_USER_EXIST_FAIL("26617","委托用户已开户：%s"),
    XW_CONFIRM_TENDER_FAIL("266016","放款：%s：%s"),
    XW_ENTRUST_AUTHORIZATION_RECORD_QUERY_FAIL("26616", "委托支付授权记录查询失败"),
    ;
    private String code;
    private String message;

    XWResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
