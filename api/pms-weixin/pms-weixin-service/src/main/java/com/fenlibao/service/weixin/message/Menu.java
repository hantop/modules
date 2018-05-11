package com.fenlibao.service.weixin.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bogle on 2016/3/2.
 */
public class Menu implements Serializable {
    private static final long serialVersionUID = -1303815752811941960L;

    @JsonProperty(value = "button")
    private List<Button> buttons;

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }
}
