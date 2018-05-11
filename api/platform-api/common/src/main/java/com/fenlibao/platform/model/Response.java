package com.fenlibao.platform.model;

/**
 * 系统状态码返回统一规范
 * 系统全局状态码以SYSTEM开头
 * 会员相关状态码以MEMBER开头
 * 积分相关状态码以INTEGRAL开头
 * 商户相关状态码以MERCHANT开头
 * 第三方相关状态以TP开头
 * 第三方借款状态码以LOAN开头
 * Created by Lullaby on 2016/2/18.
 */
public enum Response {

    RESPONSE_FAILURE(-1, "请求失败"),
    RESPONSE_SUCCESS(0, "请求成功"),
    SYSTEM_UNTHORIZED(100001, "Unauthorized"),
    SYSTEM_SIGN_FAILRUE(100002, "签名不通过"),
    SYSTEM_EMPTY_PARAMETERS(100011, "参数为空"),
    SYSTEM_REQUEST_IP_ILLEGAL(100012, "非法IP请求"),
    SYSTEM_REQUEST_FREQUENT(100013, "请求过于频繁"),
    SYSTEM_REQUEST_FORBIDDEN(100014, "Forbidden"),
    SYSTEM_ERROR_PARAMETERS(100015, "参数错误"),

    MEMBER_NOT_EXIST(200001, "商户会员信息不存在"),
    //    MEMBER_BUSINESS_NOT_EXIST(200002, "商户不存在"),
    MEMBER_CREATE_USER_FAILURE(200003, "创建分利宝账号失败"),
    MEMBER_PHONE_NUM_FORMAT_ERROR(200004, "手机号码格式错误"),


    INTEGRAL_ACCOUNT_NOT_EXIST(300001, "积分账户不存在"),
    INTEGRAL_POS_SN_FORMAT_ERROR(300002, "流水号[pos_sn]格式错误"),
    INTEGRAL_TYPE_CODE_FORMAT_ERROR(300003, "积分类型[typecode]格式错误"),
    INTEGRAL_AMOUNT_FORMAT_ERROR(300004, "金额[amount]格式错误"),
    INTEGRAL_POS_SN_EXISTS(300005, "订单已存在"),
    INTEGRAL_TYPE_NOT_EXIST(300006, "积分类型不存在"),
    INTEGRAL_EXCHANGE_RULE_NOT_EXIST(300007, "积分兑换规则不存在"),

    MERCHANT_NOT_EXIST(400001, "商户不存在"),
    MERCHANT_CONFIG_NOT_EXIST(400002, "商户配置不存在"),
    MERCHANT_CHANNEL_CODE_NOT_EXIST(400003, "商户渠道编码不存在"),
    MERCHANT_MEMBER_NOT_EXIST(400004, "商户不存在此用户"),

    TP_USER_NOT_EXIST(500001, "用户不存在或密码不正确"),
    TP_USER_DISABLED(500002, "账号已被停用"),

    LOAN_SERIAL_NUM_EXIST(600001, "借款流水号已存在"),
    LOAN_SERIAL_NUM_NOT_EXIST(600002, "借款流水号不存在"),
    LOAN_FILE_SERIAL_NUM_EXIST(600003, "协议文件流水号已存在"),
    LOAN_FILE_SERIAL_NUM_NOT_EXIST(600004, "协议文件流水号不存在"),
    LOAN_FILE_UPLOAD_ERROR(600005, "文件上传失败"),
    LOAN_FILE_SIZE_ERROR(600006, "文件大小超限"),
    LOAN_DECODE_ERROR(600009, "参数解密失败"),
    REPAYMENT_ERROR(600010, "错误的还款方式"),
    REPAYMENT_UNSUPPORT_ERROR(600011, "不支持的还款方式"),
    AGREEMENT_PARAMETERS_ERROR(600012, "签名文档参数错误"),
    IDCARD_PARAMETERS_ERROR(600013, "身份信息格式错误"),
    CYCLETYPE_PARAMETERS_ERROR(600014, "借款周期类型错误"),
    AMOUNT_PARAMETERS_ERROR(600015, "借款金额超限"),
    SERIALNUM_PARAMETERS_ERROR(600016, "serialNum序列号超过长度"),
    LOAN_TRIPLE_AGREEMENT_EXIST(600017, "该借款流水号对应三方协议已存在"),
    LOAN_AGREEMENT_ID_ERROR(600018, "合同ID超过长度"),

    BUSINESS_EXIST_ERROR(700001,"该用户提供的安全四要素已开户"),
    BUSINESS_NOTREGISTER_ERROR(700003,"该用户还未委托开户"),
    BUSINESS_REGISTER_ERROR(700002,"委托开户失败"),
    BUSINESS_EXIST_PHONE(700004,"用户提供手机号码已提交注册"),
    BUSINESS_EXIST_IDCARD(700005,"用户提供身份证号码已提交注册"),
    BUSINESS_EXIST_BANKCARD(700006,"用户提供银行卡号已提交注册"),

    WRONGFUL_ANTI_FRAUD(800001,"不合法的反欺诈信息")
    ;

    public static final String CODE_KEY = "code";
    public static final String MESSAGE_KEY = "message";

    private Integer code;

    private String message;

    Response(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
