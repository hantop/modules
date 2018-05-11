package com.fenlibao.service.pms.common.global.impl;

import com.fenlibao.dao.pms.common.global.IndexMapper;
import com.fenlibao.model.pms.common.global.HttpResponse;
import com.fenlibao.service.pms.common.global.IndexService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IndexServiceImpl implements IndexService {

	@Resource
	private IndexMapper indexMapper;

	public HttpResponse menu() {
		HttpResponse response = new HttpResponse();
		return response;
	}

}
