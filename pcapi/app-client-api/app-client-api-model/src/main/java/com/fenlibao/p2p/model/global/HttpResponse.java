package com.fenlibao.p2p.model.global;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.api.global.IResponseMessage;
import com.fenlibao.p2p.model.exception.BusinessException;

/**
 * Created by Lullaby on 2015/7/9.
 */
public class HttpResponse implements Serializable {

    /**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_CODE = ResponseCode.SUCCESS.getCode();

    private static final String DEFAULT_MESSAGE = ResponseCode.SUCCESS.getMessage();

    private String code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    public HttpResponse() {
        code = DEFAULT_CODE;
        message = DEFAULT_MESSAGE;
    }
    
    public void setCodeMessage(ResponseCode resp) {
    	this.code = resp.getCode();
    	this.message = resp.getMessage();
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
    public void setCodeMessage(BusinessException busi) {
    	this.code = busi.getCode();
    	this.message = busi.getMessage();
    }
    public void setCodeMessage(APIException ae) {
    	this.code = ae.getCode();
    	this.message = ae.getMessage();
    }
    public void setCodeMessage(IResponseMessage resp) {
    	this.code = resp.getCode();
    	this.message = resp.getMessage();
    }

}
