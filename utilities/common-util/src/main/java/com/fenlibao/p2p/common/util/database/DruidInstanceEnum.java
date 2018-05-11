package com.fenlibao.p2p.common.util.database;

/**
 * Created by Lullaby on 2015/8/5.
 */
public enum DruidInstanceEnum {

    INSTANCE;

    private DruidManager manager;

    DruidInstanceEnum() {
        manager = new DruidManager();
    }

    public DruidManager getInstance() {
        return manager;
    }

}
