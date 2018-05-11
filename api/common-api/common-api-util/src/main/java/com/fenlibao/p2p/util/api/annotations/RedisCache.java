package com.fenlibao.p2p.util.api.annotations;

import java.lang.annotation.*;

/**
 * Created by zcai on 2017/2/27.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisCache {

    Class type();

    String cacheFlag() default "";

}
