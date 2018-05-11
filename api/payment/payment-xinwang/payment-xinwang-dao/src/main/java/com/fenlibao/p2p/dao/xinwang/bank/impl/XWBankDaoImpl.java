package com.fenlibao.p2p.dao.xinwang.bank.impl;

import com.fenlibao.p2p.dao.xinwang.bank.XWBankDao;
import com.fenlibao.p2p.model.xinwang.entity.account.XWBankInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @date 2017/6/2 14:25
 */
@Repository
public class XWBankDaoImpl implements XWBankDao {
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "XWBankMapper.";

    @Override
    public XWBankInfo getBankInfo(String bankCode) {
        return sqlSession.selectOne(MAPPER + "getBankInfo", bankCode);
    }
}
