package com.fenlibao.pms.controller.biz;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 业务管理
 *
 * Created by chenzhixuan on 2016/4/11.
 */
@RestController
@RequestMapping("biz")
public class BizController {
    @RequestMapping("index")
    public ModelAndView index() {
        return new ModelAndView("biz/index");
    }
}
