package com.fenlibao.p2p.common.util.json.init;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Lullaby on 2015/7/9.
 */
public class JacksonMapperDoubleCheck {

	private volatile static ObjectMapper mapper;

	private JacksonMapperDoubleCheck() {

	}

	public static ObjectMapper getInstance() {
		if (mapper == null) {
			synchronized (JacksonMapperDoubleCheck.class) {
				if (mapper == null) {
					mapper = new ObjectMapper();
				}
			}
		}
		return mapper;
	}

}
