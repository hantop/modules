package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.AdImageDao;
import com.fenlibao.p2p.model.entity.AdImage;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class AdImageDaoImpl implements AdImageDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "AdImageMapper.";
	
	@Override
	public List<AdImage> getAdImg(Map<String, Object> map) {
		return this.sqlSession.selectList(MAPPER+"getAdImg", map);
	}

}
