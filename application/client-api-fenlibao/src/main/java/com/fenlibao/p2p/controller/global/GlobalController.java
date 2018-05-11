package com.fenlibao.p2p.controller.global;

import com.fenlibao.p2p.model.global.HttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalController {

    private enum GlobalResponseEnum {

        $ROOT("200", "Hello world"),
        $INDEX("200", "Welcome"),
        $404("404", "Not Found"),
        $500("500", "Internal Server Error");

        private String code;

        private String message;

        GlobalResponseEnum(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

    }

	@RequestMapping("/")
	public HttpResponse root() {
        HttpResponse response = new HttpResponse();
        response.setCode(GlobalResponseEnum.$ROOT.getCode());
        response.setMessage(GlobalResponseEnum.$ROOT.getMessage());
        return response;
	}

	@RequestMapping("index")
	public HttpResponse index() {
        HttpResponse response = new HttpResponse();
        response.setCode(GlobalResponseEnum.$INDEX.getCode());
        response.setMessage(GlobalResponseEnum.$INDEX.getMessage());
        return response;
	}

	@RequestMapping("404")
	public HttpResponse _404() {
        HttpResponse response = new HttpResponse();
        response.setCode(GlobalResponseEnum.$404.getCode());
        response.setMessage(GlobalResponseEnum.$404.getMessage());
        return response;
	}

	@RequestMapping("500")
	public HttpResponse _500() {
        HttpResponse response = new HttpResponse();
        response.setCode(GlobalResponseEnum.$500.getCode());
        response.setMessage(GlobalResponseEnum.$500.getMessage());
        return response;
	}

}
