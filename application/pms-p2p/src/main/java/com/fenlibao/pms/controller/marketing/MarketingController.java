package com.fenlibao.pms.controller.marketing;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Louis Wang on 2016/3/9.
 */
@RestController
@RequestMapping("marketing")
public class MarketingController {

    @RequestMapping("index")
    public ModelAndView financeIndex() {
        return new ModelAndView("marketing/index");
    }
}
