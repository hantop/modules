package com.fenlibao.pms.controller.finance;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 财务管理
 * Created by chenzhixuan on 2016/1/12.
 */
@RestController
@RequestMapping("finance")
public class FinanceController {

    @RequestMapping("index")
    public ModelAndView financeIndex() {
        return new ModelAndView("finance/index");
    }

}
