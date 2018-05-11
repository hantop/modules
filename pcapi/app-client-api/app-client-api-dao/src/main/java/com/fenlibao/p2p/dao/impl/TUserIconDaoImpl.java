package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.TUserIconDao;
import com.fenlibao.p2p.model.entity.TUserIcon;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class TUserIconDaoImpl implements TUserIconDao {

	@Resource
	private SqlSession sqlSession;
	
	private static final String MAPPER = "TUserIconMapper.";
	@Override
	public void insertUserIcon(TUserIcon icon) {
		this.sqlSession.insert(MAPPER+"insertUserIcon", icon);
	}

}
