package com.fenlibao.platform.common.response.member;

import com.fenlibao.platform.model.ApiResponse;

import java.io.Serializable;

/**
 * Created by Lullaby on 2016/2/18.
 */
public class ResponseMemberRecord extends ApiResponse implements Serializable {

    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

}
