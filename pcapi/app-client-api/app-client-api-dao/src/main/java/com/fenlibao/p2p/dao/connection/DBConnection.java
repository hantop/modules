package com.fenlibao.p2p.dao.connection;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;

/**
 * Created by Lullaby on 2015/9/11.
 */
@Repository
public class DBConnection {

    @Resource
    private SqlSession sqlSession;

    public static DBConnection getInstance() {
        return DBConnectionInstanceEnum.INSTANCE.getInstance();
    }

    public Connection getConnection() {
        System.out.println(sqlSession);
        return sqlSession.getConnection();
    }

}
