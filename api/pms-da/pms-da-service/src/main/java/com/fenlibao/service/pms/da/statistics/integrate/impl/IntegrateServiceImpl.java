package com.fenlibao.service.pms.da.statistics.integrate.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.fenlibao.dao.pms.da.statistics.integrate.IntegrateMapper;
import com.fenlibao.model.pms.da.statistics.integrate.Integrate;
import com.fenlibao.service.pms.da.statistics.integrate.IntegrateService;

@Service
public class IntegrateServiceImpl implements IntegrateService {

	@Resource
	private IntegrateMapper integrateMapper;

	@Override
	public List<Integrate> query(Date startDate, Date endDate, String productCode, String typeName, String cmd, RowBounds bounds) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
        paramMap.put("productCode", productCode);
        paramMap.put("typeName", typeName);
        paramMap.put("cmd", cmd);
		return this.integrateMapper.query(paramMap, bounds);
	}

}
