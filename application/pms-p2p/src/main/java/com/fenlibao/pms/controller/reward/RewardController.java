package com.fenlibao.pms.controller.reward;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("reward")
public class RewardController {

    @RequestMapping("index")
    public ModelAndView rewardIndex() {
        ModelAndView view = new ModelAndView("reward/index");
        return view;
    }

    @RequestMapping("cashBackVoucher")
    public ModelAndView cashBackVoucher() {
        ModelAndView view = new ModelAndView("redirect:/reward/red-packet/back-voucher-list");
        return view;
    }
}
