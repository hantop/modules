package com.fenlibao.p2p.weixin.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by lenovo on 2015/11/21.
 */
public class JSONUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> T readValue(byte[] bytes, Class<T> t) {
        try {
            return objectMapper.readValue(bytes, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readValue(byte[] src, TypeReference valueTypeRef) {
        try {
            return objectMapper.readValue(src, valueTypeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String writeValueAsString(Object target) {
        try {
            return objectMapper.writeValueAsString(target);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
