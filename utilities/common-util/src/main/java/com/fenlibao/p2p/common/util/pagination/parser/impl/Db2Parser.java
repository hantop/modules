package com.fenlibao.p2p.common.util.pagination.parser.impl;

import java.util.Map;

import com.fenlibao.p2p.common.util.pagination.Constant;
import com.fenlibao.p2p.common.util.pagination.Page;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

public class Db2Parser extends AbstractParser {

	public String getPageSql(String sql) {
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
		sqlBuilder.append("select * from (select tmp_page.*,rownumber() over() as row_id from ( ");
		sqlBuilder.append(sql);
		sqlBuilder.append(" ) as tmp_page) where row_id between  ? and ?");
		return sqlBuilder.toString();
	}

	public Map<Object, Object> setPageParameter(MappedStatement ms, Object parameterObject,
			BoundSql boundSql, Page<Object> page) {
		Map<Object, Object> paramMap = super.setPageParameter(ms, parameterObject, boundSql, page);
		paramMap.put(Constant.PAGEPARAMETER_FIRST, page.getStartRow() + 1);
		paramMap.put(Constant.PAGEPARAMETER_SECOND, page.getEndRow());
		return paramMap;
	}

}