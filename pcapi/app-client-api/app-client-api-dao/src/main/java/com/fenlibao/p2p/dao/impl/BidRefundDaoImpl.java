package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.BidRefundDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class BidRefundDaoImpl implements BidRefundDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "BidRefundMapper.";
	
	@Override
	public double getPredictEarnings(Map<String, Object> map) {
		double earnings=this.sqlSession.selectOne(MAPPER+"getPredictEarnings", map);
		return earnings;
	}

}
