package com.fenlibao.pms.controller.setting;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("setting")
public class SettingController {

    @RequestMapping("index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView("setting/index");
        return view;
    }

}
