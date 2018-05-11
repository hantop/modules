package com.fenlibao.p2p.dao.xinwang.checkfile.impl;

import com.fenlibao.p2p.dao.xinwang.checkfile.XWCheckfileDownloadDao;
import com.fenlibao.p2p.model.xinwang.checkfile.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/16.
 */
@Repository
public class XWCheckfileDownloadDaoImpl implements XWCheckfileDownloadDao {
    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "XWCheckfileDownloadMapper.";


    @Override
    public void insertCheckfileRecharge(CheckfileRecharge cr) {
        sqlSession.insert(MAPPER + "insertCheckfileRecharge", cr);
    }

    @Override
    public void insertCheckfileWithdraw(CheckfileWithdraw cw) {
        sqlSession.insert(MAPPER + "insertCheckfileWithdraw", cw);
    }

    @Override
    public void insertCheckfileCommission(CheckfileCommission cc) {
        sqlSession.insert(MAPPER + "insertCheckfileCommission", cc);
    }

    @Override
    public void insertCheckfileUser(CheckfileUser cu) {
        sqlSession.insert(MAPPER + "insertCheckfileUser", cu);
    }

    @Override
    public void insertCheckfileBackrollRecharge(CheckfileBackrollRecharge cbr) {
        sqlSession.insert(MAPPER + "insertCheckfileBackrollRecharge", cbr);
    }

    @Override
    public void insertCheckfileTransaction(CheckfileTransaction ct) {
        sqlSession.insert(MAPPER + "insertCheckfileTransaction", ct);
    }

    @Override
    public void insertCheckFileStatus(CheckfileDateStatus cfds){
        sqlSession.insert("XWCheckfileDateStatusMapper.insert", cfds);
    }

    @Override
    public CheckfileDateStatus getCheckFileStatus(CheckfileDateStatus cfds) {
        return sqlSession.selectOne( "XWCheckfileDateStatusMapper.getCheckFileStatus",cfds);
    }

    @Override
    public void updateCheckFileStatus(CheckfileDateStatus cfds) {
        sqlSession.update( "XWCheckfileDateStatusMapper.updateByPrimaryKeySelective",cfds);
    }

    @Override
    public List<Map<String, Object>> getCheckDiffData(String dateString) {
        return sqlSession.selectList( "XWCheckfileDateStatusMapper.getCheckDiffData", dateString);
    }

    @Override
    public List<Map<String, Object>> getCheckRechargeDiffData(String dateString) {
        return sqlSession.selectList( "XWCheckfileDateStatusMapper.getCheckRechargeDiffData", dateString);
    }
}
