package com.fenlibao.p2p.dao.mq.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.mq.IMallDao;
import com.fenlibao.p2p.model.mp.vo.mall.MallCommodityVO;
import com.fenlibao.p2p.model.mp.vo.mall.MallConsumePatternsVO;

@Repository
public class MallImpl implements IMallDao {

    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "MallMapper.";

	@Override
	public List<MallConsumePatternsVO> getConsumePatterns() {
		return sqlSession.selectList(MAPPER + "getConsumePatterns");
	}

	@Override
	public List<MallCommodityVO> getCommodity() {
		return sqlSession.selectList(MAPPER + "getCommodity");
	}
	
}
