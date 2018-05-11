package com.fenlibao.service.weixin.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Bogle on 2016/3/2.
 */
public class WeixinButton extends WxMsg {

    @JsonProperty(value = "menu")
    private Menu menu;

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
