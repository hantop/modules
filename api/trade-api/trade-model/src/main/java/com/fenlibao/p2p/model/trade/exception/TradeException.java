package com.fenlibao.p2p.model.trade.exception;

import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.api.global.IResponseMessage;

/**
 * Created by zcai on 2016/10/21.
 */
public class TradeException extends APIException {

    TradeException() {
        super();
    }

    public TradeException(IResponseMessage resp) {
        super(resp);
    }

    public TradeException(String code, String message) {
        super(code, message);
    }

    public TradeException(String message) {
        super(message);
    }

    public TradeException(String message, Throwable cause) {
        super(message, cause);
    }
}
