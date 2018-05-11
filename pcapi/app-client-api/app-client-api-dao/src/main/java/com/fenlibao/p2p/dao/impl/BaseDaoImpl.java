package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Lullaby on 2015/9/11.
 */
@Repository
public class BaseDaoImpl implements BaseDao {

    @Resource
    private SqlSession sqlSession;

    @Override
    public Connection getConnection() {
        Connection conn =  sqlSession.getConnection();
        System.out.println(conn);
        try {
            System.out.println(conn.isClosed());
            System.out.println(conn.isReadOnly());
            System.out.println(conn.isValid(-1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
