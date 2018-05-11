package com.fenlibao.p2p.common.util.json.init;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Lullaby on 2015/7/9.
 */
public class JacksonMapper {

	private static final ObjectMapper mapper = new ObjectMapper();

	private JacksonMapper() {

	}

	public static ObjectMapper getInstance() {
		return mapper;
	}

}
