package com.fenlibao.pms.controller.global;

import com.fenlibao.p2p.common.util.http.ResponseUtil;
import com.fenlibao.p2p.common.util.json.Jackson;
import com.fenlibao.model.pms.common.global.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@RestController
@RequestMapping("test")
public class TestController {

	private static final Logger logger = LogManager.getLogger(TestController.class);

	@RequestMapping("test")
	public void test(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> headerNames =  request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ":" + request.getHeader(headerName));
        }

		logger.info("testestest");
		HttpResponse resp = new HttpResponse();
		ResponseUtil.response(Jackson.getBaseJsonData(resp), response);
	}

	@RequestMapping("json")
	public HttpResponse json() {
		logger.info("jsonjsonjson");
		logger.debug(new RuntimeException());
		HttpResponse resp = new HttpResponse();
		return resp;
	}
	
	@RequestMapping("page")
	public ModelAndView page() {
		return new ModelAndView("index");
	}

}
