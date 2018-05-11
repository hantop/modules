package com.fenlibao.p2p.dao.autoRecharge.impl;

import com.fenlibao.p2p.dao.autoRecharge.AutoRechargeDao;
import com.fenlibao.p2p.model.entity.TransferApplication;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */
@Repository
public class AutoRechargeDaoImpl implements AutoRechargeDao{

    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "AutoRechargeMapper.";

    @Override
    public List<TransferApplication> findHardList(){
        return sqlSession.selectList(MAPPER + "findHardList");
    }

    @Override
    public int update(TransferApplication ta){
        return sqlSession.update(MAPPER + "updateTransferApplication",ta);
    }
}
