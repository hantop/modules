package com.fenlibao.p2p.model.dm.message;

import com.fenlibao.p2p.model.dm.message.body.RequestBody;
import com.fenlibao.p2p.model.dm.message.header.RequestHeader;

import javax.xml.bind.annotation.*;

/**
 * 华兴请求报文封装
 * <p>之所以全部分开，是为了xml组装</p>
 * Created by zcai on 2016/8/29.
 */
@XmlRootElement(name = "Document")
@XmlType(propOrder = { "header", "body"})
public class RequestDocument {

    private RequestHeader header;
    private RequestBody body;

    public RequestDocument() {}

    public RequestDocument(RequestBody body, RequestHeader header) {
        this.body = body;
        this.header = header;
    }


    public RequestHeader getHeader() {
        return header;
    }

    public void setHeader(RequestHeader header) {
        this.header = header;
    }

    public RequestBody getBody() {
        return body;
    }

    public void setBody(RequestBody body) {
        this.body = body;
    }
}
