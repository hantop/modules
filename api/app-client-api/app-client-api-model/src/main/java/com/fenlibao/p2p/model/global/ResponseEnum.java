package com.fenlibao.p2p.model.global;


/**
 * Created by Lullaby on 2015/8/4.
 * update : 不再使用该枚举的状态码，使用 ResponseCode
 */
@Deprecated
public enum ResponseEnum {

    RESPONSE_INTERAL_SEVER_ERROR("12138", "Internal server error"),
    RESPONSE_LOGIN_ANOTHER_DEVICE("10010", "很抱歉！您的帐号不能正常使用，请联系客服"),
    RESPONSE_UNAVAILABLE_ACCOUNT("10011", "您的帐号在另一台设备登录，请重新登录"),
    RESPONSE_SUCCESS("200", "请求成功"),
    RESPONSE_FAILURE("513", "请求失败"),
    RESPONSE_EMPTY_PARAM("40010", "参数为空"),
    RESPONSE_INVALID_PARAM("40011", "参数错误"),
    RESPONSE_SERVER_ERROR("500", "服务器异常，请稍候再试"),
    RESPONSE_NOT_SIGNIN("1001", "未登录"),
    RESPONSE_SESSION_TIMEOUT("1002", "会话过期"),
    RESPONSE_CAPTCHA_INVALID("1003", "验证码不正确"),
    RESPONSE_CAPTCHA_TIMEOUT("1004", "验证码过期"),
    RESPONSE_VERIFY_MAXTIME("1005", "当日验证错误次数已达上限"),
    RESPONSE_PHONE_EXIST("1006", "您输入的手机号码已绑定，你可以通过手机找回密码"),
    RESPONSE_EMAIL_EXIST("1007", "邮箱已存在"),
    RESPONSE_PHONE_CAPTCHA_MAXTIME("1008", "验证码发送次数超限，请明日再试"),
    RESPONSE_PHONE_CAPTCHA_FREQUENTLY("1009", "验证码发送太频繁，请稍后再试"),
    RESPONSE_OPENTICHET_INVALID("1010", "未获取到openticket"),
    RESPONSE_OPENTICKET_MISMATCH("1011", "openticket不匹配"),
    RESPONSE_OPENTICKET_EXPIRED("1012", "openticket过期"),
    RESPONSE_PHONE_HALFOUR_MAXTIME("1013", "验证码发送太频繁，请半小时后再试"),
    RESPONSE_PHONE_HOUR_MAXTIME("1014", "验证码发送太频繁，请1小时后再试"),

    RESPONSE_MODIFYPASSWORD_ERROR("20000", "找回密码失败，服务器异常"),
    RESPONSE_RETRIEVEPASSWORD_ERROR("21000", "修改密码失败，服务器异常"),
    RESPONSE_REGSUCCESS_REDISERROR("30008", "注册成功，请重新登录"),
    RESPONSE_SPREADPHONENUM_ERROR("30009", "推荐人手机号错误"),
    RESPONSE_USERNAME_NOT_EXIST("30010", "用户名不存在"),
    RESPONSE_USERNAME_EXIST("30011", "此用户名已存在"),
    RESPONSE_WRONG_PASSWORD("30012", "密码错误"),
    RESPONSE_INVALID_PASSWORD_PATTERN("30013", "密码格式错误"),
    RESPONSE_PASSWORD_MISMATCH("30014", "新旧密码不一致"),
    RESPONSE_PHONE_REGISTERED("30015", "您登记的手机号码已注册，你可以通过手机找回密码"),
    RESPONSE_REGISTER_ERROR("300151", "用户注册失败，服务器异常"),
    RESPONSE_WRONG_PHONE_PATTERN("30016", "手机号码格式不正确"),
    RESPONSE_WRONG_USERNAME_PATTERN("30017", "用户名格式不正确"),
    RESPONSE_USERNAME_NO_CHANGE("30018", "用户名不可更改"),
    RESPONSE_USER_NOT_EXIST("30019", "用户不存在"),
    RESPONSE_CHANNEL_CODE_NOT_EXISTS("30020", "渠道编号不存在"),
    RESPONSE_PAYMENT_EXCEPTION("30300", "充值异常"),
    RESPONSE_USER_SECURITY_INVLID("30301", "用户安全认证信息不存在"),
    RESPONSE_AUTHENTICATION_FAILURE("30302", "身份认证或手机认证不通过"),
    RESPONSE_AUTHENTICATION_IDCARD_FAILURE("31000", "身份证认证失败"),
    RESPONSE_IDCARDNUM_ERROR("31001", "身份证号码格式错误"),
    RESPONSE_IDCARD_FORMAT_ERROR("1888","身份证格式错误"),
    RESPONSE_TOKEN_INVALID("30303", "token验证不通过"),
    RESPONSE_BID_EMPTY("30304","该详情不存在"),
    RESPONSE_PASSWORD_SAME("30305","交易密码和登录密码不能一致"),
    RESPONSE_TRADE_PASSWORD_TYPE_WRONG("30306","交易密码类型不正确"),
    VERIFICATION_CODE_ERROR_OR_OVERDUE("30307","验证码无效或已过期"),
    RESPONSE_BANK_REPEAT("30501","当前银行卡号已存在"),
    RESPONSE_BANK_TRADERS_TYPE ("30502","交易密码类型不正确"),
    RESPONSE_BANK_AMOUNT("30503","只能添加一张银行卡"),
    RESPONSE_BANK_NAME("30504","请选择添加的银行"),
    RESPONSE_BANK_ERROR("30505","银行卡错误"),
    RESPONSE_BANK_CERTIFICATION("30506","未实名认证"),
    BIND_BANK_CARD_FAILURE("30507","绑定银行卡失败"),
    BANK_CARD_NO_EMPTY("30508","银行卡号不能为空"),
    BANK_CARD_NO_FORMAT_ERROR("30509","银行卡号格式不正确"),
    RESPONSE_DISTRICT_ERROR("31000", "获取行政区划失败"),
    RESPONSE_USERASSET_NOTFOUND("32000", "用户资产信息不存在"),
    RESPONSE_USERASSET_FAILED("32001", "用户资产信息查询失败"),
    RESPONSE_WX_UNBIND("50000","openId未绑定"),
    RESPONSE_WX_BIND_FAILED("50001","微信绑定失败"),
    RESPONSE_WX_HAVE_BIND("50002","微信已绑定成功，请勿重复绑定"),
    RESPONSE_WX_NOT_BIND("50003","微信未绑定"),
    RESPONSE_WX_HAVE_BINDED("50004","微信已绑定过"),
    RESPONSE_WX_CANCEL_SUCCESS("200", "取消成功"),
    
    WITHDRAW_FAILURE("30510","提现异常"),
    WITHDRAW_SUCCESS_WAIT_CHECK("30511","您提交的提现申请我们正在处理，如您填写的账户信息正确无误，您的资金将会于2个工作日内到达您的银行账户。"),
    ADD_BRANCH_INFO_FAIL("30512", "添加支行信息失败"),
    NOT_BIND_BANK_CARD("30513", "未绑定银行卡"),

    //薪金宝加入返回码定义4开头表示强提示
    RESPONSE_XJB_TIME_OUT("40101","您加入的薪金宝已过期，请于下次出借日#{investDay}加入"),
    RESPONSE_XJB_HAVE_BID("40102","您已加入薪金宝，请于下次出借日#{investDay}加入"),
    RESPONSE_XJB_CAN_NOT_BID("40103","下次薪金宝的加入时间还未开放，请您稍后加入"),
    RESPONSE_XJB_SUM_OVERLOAD("40105","本期薪金宝已满，请于下次出借日#{investDay}加入"),
    RESPONSE_XJB_LAST_ONE("40106","您已经成功加入最后一期薪金宝"),
    
    TEXT_LENGTH_TOO_LONG("40104","文本长度过长"),

    RESPONSE_DBFILE_TYPE_NO_EXISTS("60000", "类型不存在"),
    
    CONTACT_TOO_LONG("40107", "联系方式过长"),
    REDIS_INVALID_SESSION("10301", "未获取到服务器会话")
    ;
    
    private String code;

    private String message;

    ResponseEnum(String code, String message) {
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
