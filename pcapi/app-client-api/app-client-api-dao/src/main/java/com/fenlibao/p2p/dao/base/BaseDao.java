package com.fenlibao.p2p.dao.base;

import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;

public abstract class BaseDao {

    @Resource
    protected SqlSession sqlSession;

    protected String MAPPER;

    public BaseDao() {
        
    }

    public BaseDao(String mapper) {
        this.MAPPER = mapper + ".";
    }

}
