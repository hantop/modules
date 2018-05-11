package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.TProductInfoDao;
import com.fenlibao.p2p.model.entity.TProductInfo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TProductInfoDaoImpl implements TProductInfoDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "ProductInfoMapper.";
	
	@Override
	public List<TProductInfo> getProductInfo() {
		return this.sqlSession.selectList(MAPPER+"getProductInfo");
	}

	@Override
	public TProductInfo getProductById(int id) {
		TProductInfo info=this.sqlSession.selectOne(MAPPER+"getProductInfo", id);
		return info;
	}

}
