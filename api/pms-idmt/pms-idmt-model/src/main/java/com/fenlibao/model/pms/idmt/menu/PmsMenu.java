package com.fenlibao.model.pms.idmt.menu;

import com.fenlibao.common.pms.util.tree.Node;

/**
 * Created by Lullaby on 2015-11-04 14:27
 */
public class PmsMenu extends Node {

    private String menuName;

    private String menuUri;

    private int priority;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUri() {
        return menuUri;
    }

    public void setMenuUri(String menuUri) {
        this.menuUri = menuUri;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
