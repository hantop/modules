package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.SmsValidCodeDao;
import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.model.entity.SmsValidcode;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SmsValidCodeDaoImpl extends BaseDao implements SmsValidCodeDao {

	public SmsValidCodeDaoImpl() {
		super("SmsValidcodeMapper");
	}
	@Override
	public void addSmsCode(SmsValidcode code) {
		this.sqlSession.insert(MAPPER+"insertSmsValidcode",code);

	}

	@Override
	public List<SmsValidcode> getCode(Map<String, String> map) {
		return this.sqlSession.selectList(MAPPER+"getSmsValidcode", map);
	}
	
	@Override
	public int getSendSmsCount(Map<String, Object> map) {
		return this.sqlSession.selectOne(MAPPER+"getSendSmsCount", map);
	}

}
