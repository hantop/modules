package com.fenlibao.p2p.common.util.pagination.parser.impl;

import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.common.util.pagination.parser.SqlServer;
import com.fenlibao.p2p.common.util.pagination.Page;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;

import com.fenlibao.p2p.common.util.pagination.SqlUtil;

public class SqlServerParser extends AbstractParser {
			private static final SqlServer pageSql = new SqlServer();

			@Override
			public boolean isSupportedMappedStatementCache() {
				return false;
			}

			@Override
			public List<ParameterMapping> getPageParameterMapping(Configuration configuration,
																  BoundSql boundSql) {
				return boundSql.getParameterMappings();
	}

	@Override
	public String getPageSql(String sql) {
		Page<Object> page = SqlUtil.getLocalPage();
		return pageSql.convertToPageSql(sql, page.getStartRow(), page.getPageSize());
	}

	@Override
	public Map<Object, Object> setPageParameter(MappedStatement ms, Object parameterObject,
			BoundSql boundSql, Page<Object> page) {
		return super.setPageParameter(ms, parameterObject, boundSql, page);
	}
}