package com.fenlibao.pms.controller.global;

import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.service.pms.common.global.IndexService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class IndexController {
	
	@Resource
	private IndexService indexService;

    @RequiresAuthentication
	@RequestMapping("index")
	public ModelAndView index() {
		ModelAndView view = new ModelAndView("index");
		return view;
	}
	
	@RequestMapping("menu")
	public HttpResponse menu() {
		return indexService.menu();
	}

}
