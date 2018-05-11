package com.fenlibao.p2p.model.dm.message;

import com.fenlibao.p2p.model.dm.message.body.ResponseBody;
import com.fenlibao.p2p.model.dm.message.header.ResponseHeader;

/**
 * 华兴响应报文封装
 * Created by zcai on 2016/8/30.
 */
public class ResponseDocument {

    private ResponseHeader header;
    private ResponseBody body;

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public ResponseBody getBody() {
        return body;
    }

    public void setBody(ResponseBody body) {
        this.body = body;
    }
}
