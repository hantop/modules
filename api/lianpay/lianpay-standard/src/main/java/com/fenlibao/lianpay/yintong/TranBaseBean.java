package com.fenlibao.lianpay.yintong;

import java.io.Serializable;

/**
 * 交易的基础bean
 * Created by LouisWang on 2015/8/21.
 */
public class TranBaseBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String            ret_code;
    private String            ret_msg;
    private String            sign_type;
    private String            sign;
    private String            correlationID;

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

    public String getSign_type()
    {
        return sign_type;
    }

    public void setSign_type(String sign_type)
    {
        this.sign_type = sign_type;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public String getCorrelationID()
    {
        return correlationID;
    }

    public void setCorrelationID(String correlationID)
    {
        this.correlationID = correlationID;
    }

}
