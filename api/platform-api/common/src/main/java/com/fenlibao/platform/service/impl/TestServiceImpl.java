package com.fenlibao.platform.service.impl;

import com.fenlibao.platform.dao.TestMapper;
import com.fenlibao.platform.service.TestService;
import org.mybatis.guice.transactional.Transactional;

import javax.inject.Inject;

public class TestServiceImpl implements TestService {

    @Inject
    private TestMapper testMapper;

//    @Transactional
    public String getMessage() {
        return testMapper.test();
    }

}
