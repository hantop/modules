package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.UserThirdpartyDao;
import com.fenlibao.p2p.model.entity.UserThirdparty;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * Created by chenzhixuan on 2015/8/21.
 */
@Repository
public class UserThirdpartyDaoImpl implements UserThirdpartyDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "UserThirdpartyMapper.";

    @Override
    public int addUserThirdparty(Map map) {
        return sqlSession.insert(MAPPER+"addUserThirdparty", map);
    }

	@Override
	public int isBindThirdparty(Map map) {
		List list =  sqlSession.selectList(MAPPER+"isBindThirdparty", map);
		return list.size();
	}

	@Override
	public int getUserByOpenId(Map map) {
		UserThirdparty user=  sqlSession.selectOne(MAPPER+"getUserByOpenId", map);
		if(user!=null){
			return user.getUserId();
		}
		return 0;
	}

	@Override
	public int cancelAutoLogin(Map map) {
		return sqlSession.update(MAPPER+"cancelAutoLogin", map);
	}

	/* (non Javadoc) 
	 * @Title: isBindThirdpartyOpenId
	 * @Description: TODO
	 * @param map
	 * @return 
	 * @see com.fenlibao.p2p.dao.UserThirdpartyDao#isBindThirdpartyOpenId(java.util.Map) 
	 */
	@Override
	public int isBindThirdpartyOpenId(Map map) {
		List list =  sqlSession.selectList(MAPPER+"isBindThirdpartyOpenId", map);
		return list.size();
	}
}
