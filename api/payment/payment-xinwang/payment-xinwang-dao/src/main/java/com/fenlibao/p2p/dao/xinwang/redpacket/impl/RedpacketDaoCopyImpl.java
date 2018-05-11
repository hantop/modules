package com.fenlibao.p2p.dao.xinwang.redpacket.impl;

import com.fenlibao.p2p.dao.xinwang.redpacket.RedpacketCopyDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/16.
 */
@Repository
public class RedpacketDaoCopyImpl implements RedpacketCopyDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "RedpacketCopyMapper.";


    @Override
    public int addTruninFundsRecord(Map<String, Object> paramMap) {
        sqlSession.insert(MAPPER + "addTruninFundsRecord", paramMap);
        return Integer.parseInt(String.valueOf(paramMap.get("id")));
    }

    @Override
    public int addTrunoutFundsRecord(Map<String, Object> paramMap) {
        return sqlSession.insert(MAPPER + "addTrunoutFundsRecord", paramMap);
    }

}
