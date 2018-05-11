package com.fenlibao.p2p.sms.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Administrator on 2015/9/1.
 */
@XStreamAlias("response")
public class Response {

    @XStreamAlias("error")
    private int error;

    @XStreamAlias("message")
    private String message;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
