package com.fenlibao.p2p.model.user.exception;

import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.api.global.IResponseMessage;

/**
 * Created by zcai on 2016/10/21.
 */
public class UserException extends APIException {

    public UserException() {
        super();
    }

    public UserException(IResponseMessage resp) {
        super(resp);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String code, String message) {
        super(code, message);
    }
}
