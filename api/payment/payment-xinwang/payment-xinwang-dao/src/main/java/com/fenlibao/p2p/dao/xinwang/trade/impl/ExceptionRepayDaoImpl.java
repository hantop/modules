package com.fenlibao.p2p.dao.xinwang.trade.impl;

import com.fenlibao.p2p.dao.xinwang.trade.ExceptionRepayDao;
import com.fenlibao.p2p.model.xinwang.entity.trade.ExceptionRepay;
import com.fenlibao.p2p.model.xinwang.entity.trade.XWExceptionRepayPO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/18.
 */
@Repository
public class ExceptionRepayDaoImpl implements ExceptionRepayDao {
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "ExceptionRepayMapper.";

    @Override
    public List<ExceptionRepay> getDebtUsers() {
        return sqlSession.selectList(MAPPER + "getDebtUsers");
    }

    @Override
    public void saveExceptionRepayRequestNo(Map<String, Object> saveRequestNoParams) {
        sqlSession.insert(MAPPER + "saveExceptionRepayRequestNo", saveRequestNoParams);
    }

    @Override
    public XWExceptionRepayPO findExceptionRepayPoByRequestNo(String requestNo) {
        return sqlSession.selectOne(MAPPER + "findExceptionRepayPoByRequestNo", requestNo);
    }

    @Override
    public void updateExceptionRepay(Map<String, Object> exceptionRepayPO) {
        sqlSession.update(MAPPER + "updateExceptionRepay", exceptionRepayPO);
    }
}
