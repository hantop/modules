package com.fenlibao.pms.controller.statistics;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("statistics")
public class StatisticsController {
	
	@RequestMapping("index")
	public ModelAndView statisticsIndex() {
		ModelAndView view = new ModelAndView("statistics/index");
		return view;
	}
	
}
