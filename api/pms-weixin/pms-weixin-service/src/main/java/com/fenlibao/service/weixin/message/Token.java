package com.fenlibao.service.weixin.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2015/8/17.
 */
public class Token extends WxMsg {

    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @JsonProperty(value = "scope")
    private String scope;

    @JsonProperty(value = "unionid")
    private String unionid;

    private Long createTime;

    public Token() {
    }

    public Token(Long expiresIn, Long createTime,String accessToken) {
        super(accessToken);
        this.expiresIn = expiresIn;
        this.createTime = createTime;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
