package com.fenlibao.platform.common.json;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Lullaby on 2015/7/9.
 */
public enum JacksonMapper {

	INSTANCE;

	private ObjectMapper mapper;

	JacksonMapper() {
		mapper = new ObjectMapper();
	}

	public ObjectMapper getInstance() {
		return mapper;
	}

}
