package com.fenlibao.p2p.dao.xinwang.pay.impl;

import com.fenlibao.p2p.dao.xinwang.pay.XWRechargeDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @date 2017/5/23 13:53
 */
@Repository
public class XWRechargeDaoImpl implements XWRechargeDao {
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "XWRechargeMapper.";

    @Override
    public int insertRechargeRequest(Map<String,Object> param) {
        return sqlSession.insert(MAPPER + "setRechargeRequest", param);
    }
}
