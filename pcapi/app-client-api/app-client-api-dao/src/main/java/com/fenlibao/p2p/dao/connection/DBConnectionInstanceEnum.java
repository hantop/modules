package com.fenlibao.p2p.dao.connection;

/**
 * Created by Lullaby on 2015/9/11.
 */
public enum DBConnectionInstanceEnum {

    INSTANCE;

    private DBConnection dbConnection;

    DBConnectionInstanceEnum() {
        dbConnection = new DBConnection();
    }

    public DBConnection getInstance() {
        return dbConnection;
    }

}
