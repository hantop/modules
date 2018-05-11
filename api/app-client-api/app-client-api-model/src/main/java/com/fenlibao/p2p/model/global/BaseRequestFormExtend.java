package com.fenlibao.p2p.model.global;

import org.apache.commons.lang3.StringUtils;

/**
 * 请求参数扩展bean
 * 基于BaseRequestForm扩展token, userId字段
 */
public class BaseRequestFormExtend extends BaseRequestForm {

    /**
     * 用户token
     */
    private String token;

    /**
     * 用户id
     */
    private Integer userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return super.toString() + "BaseRequestFormExtend [token=" + token + ", userId=" + userId + "]";
    }

    /**
     * 验证属性是否为空
     *
     * @return
     */
    public boolean validate() {
        return super.validate()  && this.userId != null;
    }

}
