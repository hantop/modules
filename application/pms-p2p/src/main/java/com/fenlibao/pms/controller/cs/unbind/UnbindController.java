/*
 * Copyright (c) 2015 by FENLIBAO NETWORK TECHNOLOGY CO.
 *             All rights reserved
 */
package com.fenlibao.pms.controller.cs.unbind;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author <a href="mailto:toby.xiong2@qq.com">Toby</a>
 * @Data 2015年11月30日
 * @Version 1.0.0
 */

@RestController
@RequestMapping("cs/unbindBankCard")
public class UnbindController {
	
	public ModelAndView unbindBankCard() {
		ModelAndView view = new ModelAndView("cs/unbindBankCard/index");
		return view;
	}
}
