package com.fenlibao.pms.controller.client;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("client")
public class ClientController {

	@RequiresPermissions("client:view")
	@RequestMapping("index")
	public ModelAndView clientIndex() {
		ModelAndView view = new ModelAndView("client/index");
		return view;
	}
	
}
