package com.fenlibao.pms.controller.publicity;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("publicity")
public class PublicityController {

	@RequiresPermissions("publicity:view")
	@RequestMapping("index")
	public ModelAndView publicityIndex() {
		ModelAndView view = new ModelAndView("publicity/index");
		return view;
	}
	
}
