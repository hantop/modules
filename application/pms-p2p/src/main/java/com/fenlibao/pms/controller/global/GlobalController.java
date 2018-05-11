package com.fenlibao.pms.controller.global;

import com.fenlibao.model.pms.da.Bank;
import com.fenlibao.service.pms.da.global.GlobalService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class GlobalController {

    @Autowired
    private GlobalService globalService;

    @RequestMapping("")
    public ModelAndView root() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new ModelAndView("redirect:index");
        } else {
            return new ModelAndView("redirect:login");
        }
    }

	@RequestMapping("/")
	public ModelAndView _root() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new ModelAndView("redirect:index");
        } else {
            return new ModelAndView("redirect:login");
        }
	}
    /**
     *获取可用的银行列表
     */
    @RequestMapping("getBankList")
    @ResponseBody
    public List<Bank> getBankList() {
        return globalService.getBankList();
    }

	@RequestMapping("404")
	public ModelAndView _404() {
		return new ModelAndView("404");
	}

	@RequestMapping("500")
	public ModelAndView _500() {
		return new ModelAndView("500");
	}

	@RequestMapping("unauthorized")
	public ModelAndView unauthorized() {
		return new ModelAndView("unauthorized");
	}

}
