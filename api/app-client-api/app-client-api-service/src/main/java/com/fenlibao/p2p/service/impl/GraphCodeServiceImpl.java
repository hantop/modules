package com.fenlibao.p2p.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.GraphValidateCodeDao;
import com.fenlibao.p2p.model.entity.GraphValidateCode;
import com.fenlibao.p2p.service.GraphCodeService;

@Service
public class GraphCodeServiceImpl implements GraphCodeService {

	
	@Resource
	private GraphValidateCodeDao graphValidateCodeDao;
	
	@Override
	public void addGraphCode(GraphValidateCode code) {
		this.graphValidateCodeDao.addGraphCode(code);
	}

	@Override
	public GraphValidateCode getGraphValidateCode(String vkey) {
		return this.graphValidateCodeDao.getGraphCode(vkey);
	}

	@Override
	public void updateGraphCode(String key,int status) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("vkey", key);
		map.put("status", status);
		this.graphValidateCodeDao.updateGraphCode(map);
	}

}
