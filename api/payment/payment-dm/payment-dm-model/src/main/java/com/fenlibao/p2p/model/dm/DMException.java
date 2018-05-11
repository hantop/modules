package com.fenlibao.p2p.model.dm;

import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.api.global.IResponseMessage;

/**
 * Created by zcai on 2016/10/23.
 */
public class DMException extends APIException {

    public DMException() {}

    public DMException(IResponseMessage resp) {
        super(resp);
    }

    public DMException(String code, String message) {
        super(code, message);
    }

    public DMException(String message) {
        super(message);
    }

    public DMException(String message, Throwable cause) {
        super(message, cause);
    }
}
