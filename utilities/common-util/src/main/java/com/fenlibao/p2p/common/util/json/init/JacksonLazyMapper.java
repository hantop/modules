package com.fenlibao.p2p.common.util.json.init;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Lullaby on 2015/7/14.
 */
public class JacksonLazyMapper {

    private static ObjectMapper mapper = null;

    private JacksonLazyMapper() {

    }

    public static ObjectMapper getInstance() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

}
