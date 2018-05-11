package com.fenlibao.model.pms.common.screentype;

/**
 * 分辨率类型
 * <p>
 * Created by chenzhixuan on 2016/5/17.
 */
public class ScreenType {
    private byte id;
    private String clienttypeCode;// 客户端类型编码
    private String clienttypeId;// 客户端类型ID
    private String code;
    private String name;
    private String screenHeight;
    private String screenWidth;

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getClienttypeCode() {
        return clienttypeCode;
    }

    public void setClienttypeCode(String clienttypeCode) {
        this.clienttypeCode = clienttypeCode;
    }

    public String getClienttypeId() {
        return clienttypeId;
    }

    public void setClienttypeId(String clienttypeId) {
        this.clienttypeId = clienttypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(String screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(String screenWidth) {
        this.screenWidth = screenWidth;
    }
}
