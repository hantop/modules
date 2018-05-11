package com.fenlibao.model.pms.common.global;

/**
 * Created by Lullaby on 2015-09-23 17:08
 */
public enum ResponseEnum {

    SUCCESS("200", "请求成功"),
    NOTFOUND("404", "资源不存在"),
    OPERATION_ERROR("1004", "操作失败"),
    INTERAL_SEVER_ERROR("500", "服务器内部错误"),
    NORMAL("10001", "请求响应成功"),
    ERROR("10002", "请求响应失败"),
    WRONG_PARAMETER("10003", "参数错误"),
    EMPTY_PARAMETER("10004", "参数为空"),
    EMPTY_USERNAME_PASSWORD("30001", "用户名或密码为空"),
    WRONG_USERNAME_PASSWORD("30002", "用户名或密码错误"),
    EMPTY_CAPTCHA("30003", "验证码为空"),
    WRONG_CAPTCHA("30004", "验证码错误"),
    TIMEOUT_CAPTCHA("30005", "验证码已过期"),
    EMPTY_CLIENT_SESSION("30006", "页面已过期，请刷新重试"),
    ROLE_ALREADYEXISTS("40000", "此角色已存在"),
    FAILED_QINIU_UPLOAD("50000", "上传七牛失败")
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
