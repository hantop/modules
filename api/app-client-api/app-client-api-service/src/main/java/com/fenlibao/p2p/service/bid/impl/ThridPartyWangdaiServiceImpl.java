package com.fenlibao.p2p.service.bid.impl;

import com.fenlibao.p2p.service.bid.ThridPartyWangdaiService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/12.
 */
@Service
public class ThridPartyWangdaiServiceImpl implements ThridPartyWangdaiService {

    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "BidInfoMapper.";

    @Override
    public BigDecimal getBidInfoSumAmout(Map<String, Object> map) throws Exception {
        if(map.isEmpty())
            return null;
        return sqlSession.selectOne(MAPPER + "getThridPartySunAmout", map);
    }

    @Override
    public Date getThridPartyFristDate(Map<String, Object> map) throws Exception {
        return sqlSession.selectOne(MAPPER + "getThridPartyFristDate", map);
    }

    @Override
    public List<HashMap> getThridPartyRandomDayBids(Map<String, Object> map) throws Exception {
        if (map.isEmpty())
            return null;
        return sqlSession.selectList(MAPPER + "getThridPartyRandomDayBids", map);
    }


}
