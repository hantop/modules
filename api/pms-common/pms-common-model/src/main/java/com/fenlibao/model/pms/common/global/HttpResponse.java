package com.fenlibao.model.pms.common.global;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lullaby on 2015/7/28.
 */
public class HttpResponse implements Serializable {

    private static final String DEFAULT_CODE = "200";

    private static final String DEFAULT_MESSAGE = "success";

    private String code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    public HttpResponse() {
        code = DEFAULT_CODE;
        message = DEFAULT_MESSAGE;
    }

    public void setCodeMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
