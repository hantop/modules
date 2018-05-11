package com.fenlibao.model.pms.idmt.menu;

import com.fenlibao.common.pms.util.tree.Node;

import java.util.Map;

/**
 * Created by Lullaby on 2015-11-05 17:29
 */
public class MenuTreeNode extends Node {

    private String text;

    private String state;

    private Map<String, String> attributes;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

}
