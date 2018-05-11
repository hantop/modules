package com.fenlibao.p2p.dao.base;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public abstract class BaseDao {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    protected SqlSession sqlSession;

    protected String MAPPER;

    public BaseDao() {

    }

    public BaseDao(String mapper) {
        this.MAPPER = mapper + ".";
    }

}
