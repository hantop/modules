package com.fenlibao.lianpay.v_1_0.vo;

import java.io.Serializable;

/**
 * 连连回调响应
 * @author zcai
 * @date 2016年4月19日
 */
public class CallbackResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String SUCCESS_CODE = "0000";
	private static final String SUCCESS_MSG = "交易成功";
	private static final String FAILURE_CODE = "9999";
	private static final String FAILURE_MSG = "交易失败";

    private String ret_code;

    private String ret_msg;
    
    public CallbackResponse() {
    	this.ret_code = SUCCESS_CODE;
    	this.ret_msg = SUCCESS_MSG;
    }

    public String getRet_code()
    {
        return ret_code;
    }

    public void setRet_code(String ret_code)
    {
        this.ret_code = ret_code;
    }

    public String getRet_msg()
    {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg)
    {
        this.ret_msg = ret_msg;
    }

    public void failure() {
    	this.ret_code = FAILURE_CODE;
    	this.ret_msg = FAILURE_MSG;
    }
    
    public void failure(String msg) {
    	this.ret_code = FAILURE_CODE;
    	this.ret_msg = msg;
    }
    
}
