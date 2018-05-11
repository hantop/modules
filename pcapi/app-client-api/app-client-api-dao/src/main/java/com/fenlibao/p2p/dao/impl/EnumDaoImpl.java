package com.fenlibao.p2p.dao.impl;

import com.fenlibao.p2p.dao.EnumDao;
import com.fenlibao.p2p.model.entity.TEnum;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class EnumDaoImpl implements EnumDao {

	@Resource
	private SqlSession sqlSession;

	private static final String MAPPER = "EnumMapper.";

	public List<TEnum> getEnum(String enumTable, String enumColumn) {
		TEnum tenum = new TEnum();
		tenum.setEnumTable(enumTable);
		tenum.setEnumColumn(enumColumn);
		return sqlSession.selectList(MAPPER + "getEnum", tenum);
	}

	public int addEnum(TEnum tenum) {
		return sqlSession.insert(MAPPER + "addEnum", tenum);
	}

	public int updateEnum(TEnum tenum) {
		return sqlSession.update(MAPPER + "updateEnum", tenum);
	}

	public int deleteEnum(String id) {
		return sqlSession.delete(MAPPER + "deleteEnum", id);
	}

	public List<Map<String, Object>> testMultiDataSource() {
		return sqlSession.selectList(MAPPER + "testMultiDataSource");
	}

	@Override
	public TEnum getEnum(String enumTable, String enumColumn, String enumKey) {
		TEnum tenum = new TEnum();
		tenum.setEnumTable(enumTable);
		tenum.setEnumColumn(enumColumn);
		tenum.setEnumKey(enumKey);
		return sqlSession.selectOne(MAPPER + "getEnum", tenum);
	}

    public int testTransaction1(TEnum tEnum) {
        return sqlSession.update(MAPPER + "testTransaction1", tEnum);
    }

    public int testTransaction2(TEnum tEnum) {
        return sqlSession.update(MAPPER + "testTransaction2", tEnum);
    }

}
