package com.fenlibao.pms.controller.system;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("system")
public class SystemController {

	@RequestMapping("index")
	public ModelAndView systemIndex() {
		ModelAndView view = new ModelAndView("system/index");
		return view;
	}
	
}
