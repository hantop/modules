package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.TEnum;

import java.util.List;
import java.util.Map;

public interface EnumDao {

	List<TEnum> getEnum(String enumTable, String enumColumn);

	int addEnum(TEnum tenum);

	int updateEnum(TEnum tenum);

	int deleteEnum(String id);

	List<Map<String, Object>> testMultiDataSource();
	
	TEnum getEnum(String enumTable, String enumColumn, String enumKey);

    int testTransaction1(TEnum tEnum);

    int testTransaction2(TEnum tEnum);

}
