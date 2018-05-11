package com.fenlibao.p2p.controller.noversion;

import com.fenlibao.p2p.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.Connection;

/**
 * Created by Lullaby on 2015/9/11.
 */
@RestController("v_1_0_0")
public class BaseController {

    @Resource
    private BaseService baseService;

    public Connection getConnection() {
        return baseService.getConnection();
    }

}
