package com.fenlibao.service.pms.da.exception;

import java.util.List;

/**
 * Created by Bogle on 2015/12/2.
 */
public class ExcelException extends RuntimeException {

    private List<String> errors;

    public ExcelException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
