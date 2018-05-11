package com.fenlibao.p2p.model.xinwang.param.account;

/**
 * 迁移用户激活service接口参数
 */
public class ActivateStockedUserRequestParams {
    private Integer userId;
    private String platformUserNo;
    private String uri;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPlatformUserNo() {
        return platformUserNo;
    }

    public void setPlatformUserNo(String platformUserNo) {
        this.platformUserNo = platformUserNo;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
