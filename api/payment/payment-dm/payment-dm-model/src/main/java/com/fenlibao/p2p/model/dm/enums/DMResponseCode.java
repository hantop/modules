package com.fenlibao.p2p.model.dm.enums;

import com.fenlibao.p2p.model.api.global.IResponseMessage;

/**
 * 存管相关响应信息
 * code定义规范参考
 * @see IResponseMessage
 * Created by zcai on 2016/10/23.
 */
public enum DMResponseCode implements IResponseMessage {
//    前缀表示消息的权重（1=给开发者，2=用户弱提示，3=用户强提示）
    //DM_    6xxxx
    //601xx   开户
    //602xx   绑卡
    //603xx   充值


    DM_HAS_BEEN_OPEN_ACCOUNT("160101", "该用户已经开户"),

    ;

    private String code;
    private String message;

    DMResponseCode(String code, String message) {
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
