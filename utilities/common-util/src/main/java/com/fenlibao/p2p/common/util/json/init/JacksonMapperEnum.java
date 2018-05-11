package com.fenlibao.p2p.common.util.json.init;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Lullaby on 2015/7/9.
 */
public enum JacksonMapperEnum {

	INSTANCE;

	private ObjectMapper mapper;

	JacksonMapperEnum() {
		mapper = new ObjectMapper();
	}

	public ObjectMapper getInstance() {
		return mapper;
	}

}
