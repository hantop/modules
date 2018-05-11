package com.fenlibao.pms.controller.planCenter;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2017/3/17.
 */
@RestController

@RequestMapping("planCenter")
public class PlanCenterController {
    private static final Logger logger = LoggerFactory.getLogger(PlanCenterController.class);

    @RequestMapping("index")
    @RequiresPermissions("planCenter:view")
    public ModelAndView index() {
        return new ModelAndView("planCenter/index");
    }
}
