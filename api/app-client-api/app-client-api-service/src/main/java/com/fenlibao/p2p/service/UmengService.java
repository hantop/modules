package com.fenlibao.p2p.service;

import com.fenlibao.p2p.model.global.HttpResponse;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/24.
 */
public interface UmengService {

    HttpResponse sendUmengMessage(Map<String, String> param) throws Throwable;
}
