package com.fenlibao.p2p.sms.config.util;


import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.util.Assert;

/**
 * Created by Administrator on 2015/7/3.
 */
public final class Utils {

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }



    /**
     * 校验数据
     *
     * @param target
     */
    public static void validate(Object target) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
        boolean error = constraintViolations.size() > 0;
        if (error)
            Assert.isTrue(!error, constraintViolations.iterator().next().getMessage());
    }
}
