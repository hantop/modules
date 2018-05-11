package com.fenlibao.p2p.common.util.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenlibao.p2p.common.util.json.init.JacksonMapperEnum;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lullaby on 2015/7/9.
 */
public class Jackson {

	public static String getBaseJsonData(Object obj) {
		StringWriter writer = new StringWriter();
		if (obj != null) {
			ObjectMapper mapper = JacksonMapperEnum.INSTANCE.getInstance();
			try {
				mapper.writeValue(writer, obj);
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writer.toString();
	}

	/**
	 * 根据对象获取对象的字段和字段值map
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> getMapData(Object obj) {
		String baseJsonData = getBaseJsonData(obj);
		ObjectMapper mapper = JacksonMapperEnum.INSTANCE.getInstance();
		Map<String, Object> result = new HashMap<>();
		try {
			result = mapper.readValue(baseJsonData, Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Object getBeanJsonData(String json, Class<?> cls) {
		ObjectMapper mapper = JacksonMapperEnum.INSTANCE.getInstance();
		Object obj = null;
		try {
			obj = mapper.readValue(json, cls);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}

}
