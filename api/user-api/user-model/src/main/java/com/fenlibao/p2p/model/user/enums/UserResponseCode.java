package com.fenlibao.p2p.model.user.enums;

import com.fenlibao.p2p.model.api.global.IResponseMessage;

/**
 * Created by zcai on 2016/10/21.
 */
public enum UserResponseCode implements IResponseMessage {

    //前缀表示消息的权重（1=给开发者，2=用户弱提示，3=用户强提示）
    //定义状态码之前先确认相应的状态有没有

    /** 	错误码前缀	       错误码描述	                     错误码
     COMMON_	           公用错误码	                     10xxx
     USER_	           用户相关错误码	                 11xxx
     TRADE_	           交易相关错误码	                 12xxx
     WEIXIN_            微信公众号相关错误码            13xxx
     ACTIVITY_          活动相关错误码	                 14xxx
     BID_               标相关错误码                   2xxxx
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
    USER_HAS_BEEN_BIND_CARD("211130", "您已绑卡"),
////用户相关错误码 end

    ;

    private String code;
    private String message;

    UserResponseCode(String code, String message) {
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
