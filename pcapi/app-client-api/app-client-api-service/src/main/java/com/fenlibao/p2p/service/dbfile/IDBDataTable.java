package com.fenlibao.p2p.service.dbfile;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * sqlite数据文件基类
 * Created by chenzhixuan on 2015/9/9.
 */
public interface IDBDataTable {

    /**
     * 获取创建表语句
     * @return
     */
    String getCreateTableSql();

    /**
     * 获取插入语句
     * @return
     */
    String getInsertSql();

    /**
     * 把所有数据放入PreparedStatement中
     * @param pstmt
     */
    void addBatch(PreparedStatement pstmt) throws SQLException;

}
