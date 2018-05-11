package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.StartpageImageDao;
import com.fenlibao.p2p.model.entity.TStartupImage;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class StartpageImageDaoImpl implements StartpageImageDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "StartpageImageMapper.";
	
	@Override
	public TStartupImage getStartpageImage(Map<String, String> map) {
		return this.sqlSession.selectOne(MAPPER+"getStartpageImage", map);
	}

}
