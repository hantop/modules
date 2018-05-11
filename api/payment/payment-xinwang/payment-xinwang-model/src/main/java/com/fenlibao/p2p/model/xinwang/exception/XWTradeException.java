package com.fenlibao.p2p.model.xinwang.exception;

import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.api.global.IResponseMessage;

public class XWTradeException extends APIException {

    XWTradeException() {
        super();
    }

    public XWTradeException(IResponseMessage resp) {
        super(resp);
    }

    public XWTradeException(String code, String message) {
        super(code, message);
    }

    public XWTradeException(String message) {
        super(message);
    }

    public XWTradeException(String message, Throwable cause) {
        super(message, cause);
    }
}
