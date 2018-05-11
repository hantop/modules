package com.fenlibao.p2p.model.global;

import org.apache.commons.lang3.StringUtils;

public class BaseRequestForm {

    private String clientType;

    private String screenType;

    private String deviceId;

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String toString() {
        return "BaseRequestForm [clientType=" + clientType +
                ", screenType=" + screenType +
                ", deviceId=" + deviceId + "]";
    }

    public boolean validate() {
        if (StringUtils.isNotEmpty(clientType)) {
            if (clientType.equals(String.valueOf(Constant.CLIENTTYPE_WAP))
                    || clientType.equals(String.valueOf(Constant.CLIENTTYPE_WEIXIN))
                    || clientType.equals(String.valueOf(Constant.CLIENTTYPE_PC)) ) {
                return true;
            }
        } else {
            return false;
        }
        return StringUtils.isNoneEmpty(this.deviceId, this.screenType);
    }

}
