package com.fenlibao.p2p.dao.district.impl;

import org.springframework.stereotype.Repository;

import com.fenlibao.p2p.dao.base.BaseDao;
import com.fenlibao.p2p.dao.district.IDistrictDao;

@Repository
public class DistrictDaoImpl extends BaseDao implements IDistrictDao {

	public DistrictDaoImpl() {
		super("DistrictMapper");
	}
	
	@Override
	public String getProvinceCodeByCityCode(String cityCode) {
		return sqlSession.selectOne(MAPPER + "getProvinceCode", cityCode);
	}

}
