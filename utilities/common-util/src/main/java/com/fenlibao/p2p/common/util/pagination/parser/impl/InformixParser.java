package com.fenlibao.p2p.common.util.pagination.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.common.util.pagination.Constant;
import com.fenlibao.p2p.common.util.pagination.Page;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

public class InformixParser extends AbstractParser {

	public String getPageSql(String sql) {
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 40);
		sqlBuilder.append("select skip ? first ? * from ( ");
		sqlBuilder.append(sql);
		sqlBuilder.append(" ) temp_t");
		return sqlBuilder.toString();
	}

	public List<ParameterMapping> getPageParameterMapping(Configuration configuration,
			BoundSql boundSql) {
		List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>();
		newParameterMappings.add(new ParameterMapping.Builder(configuration, Constant.PAGEPARAMETER_FIRST,
				Integer.class).build());
		newParameterMappings.add(new ParameterMapping.Builder(configuration, Constant.PAGEPARAMETER_SECOND,
				Integer.class).build());
		if (boundSql.getParameterMappings() != null) {
			newParameterMappings.addAll(boundSql.getParameterMappings());
		}
		return newParameterMappings;
	}

	public Map<Object, Object> setPageParameter(MappedStatement ms, Object parameterObject,
			BoundSql boundSql, Page<Object> page) {
		Map<Object, Object> paramMap = super.setPageParameter(ms, parameterObject, boundSql, page);
		paramMap.put(Constant.PAGEPARAMETER_FIRST, page.getStartRow());
		paramMap.put(Constant.PAGEPARAMETER_SECOND, page.getPageSize());
		return paramMap;
	}

}