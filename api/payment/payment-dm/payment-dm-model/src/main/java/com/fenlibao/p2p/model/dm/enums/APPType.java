package com.fenlibao.p2p.model.dm.enums;

/**
 * 应用标识
 * <p>（dm module不依赖API）这里的客户端类型编码对应 com.fenlibao.p2p.model.global.Constant</p>
 * Created by zcai on 2016/9/22.
 */
public enum APPType {

    IOS(1, "APP"),
    ANDROID(2, "APP"),
    WAP(5, "WX"),
    WECHAT(6, "WX"),
    PC(7, "PC");

    private int code;//客户端类型编码
    private String type;//华兴对应用户标识

    APPType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    /**
     * 获取华兴对应应用标识
     * @param clientTypeCode
     * @return
     */
    public static String getType(int clientTypeCode) {
        APPType[] appTypes = APPType.values();
        for (APPType t : appTypes) {
            if (t.getCode() == clientTypeCode) {
                return t.getType();
            }
        }
        return "";
    }

    public static boolean isPC(int clientTypeCode) {
        if (PC.getCode() == clientTypeCode) {
            return true;
        }
        return false;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
