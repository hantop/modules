package com.fenlibao.p2p.model.xinwang.param.account;

/**
 * Created by Administrator on 2017/5/10.
 */
public class UnbindBankCardRequestParams {
    private int userId;
    private String userRole;
    private String authList;
    private String uri;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getAuthList() {
        return authList;
    }

    public void setAuthList(String authList) {
        this.authList = authList;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
