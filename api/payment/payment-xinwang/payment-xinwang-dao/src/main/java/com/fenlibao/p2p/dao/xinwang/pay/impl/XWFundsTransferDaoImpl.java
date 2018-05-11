package com.fenlibao.p2p.dao.xinwang.pay.impl;

import com.fenlibao.p2p.dao.xinwang.pay.XWFundsTransferDao;
import com.fenlibao.p2p.model.xinwang.entity.pay.XWFundsTransfer;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @date 2017/7/1 11:33
 */
@Repository
public class XWFundsTransferDaoImpl implements XWFundsTransferDao {
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "XWFundsTransferMapper.";

    @Override
    public void insertFlow(XWFundsTransfer xwFundsTransfer) {
        sqlSession.insert(MAPPER + "insertFlow", xwFundsTransfer);
    }
}
