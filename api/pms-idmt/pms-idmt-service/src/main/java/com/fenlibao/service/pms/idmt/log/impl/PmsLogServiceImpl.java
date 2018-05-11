package com.fenlibao.service.pms.idmt.log.impl;

import com.fenlibao.dao.pms.idmt.log.PmsLogMapper;
import com.fenlibao.model.pms.common.global.OperateLog;
import com.fenlibao.model.pms.idmt.log.PmsLog;
import com.fenlibao.service.pms.idmt.log.PmsLogService;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PmsLogServiceImpl implements PmsLogService{

	private PmsLogMapper pmsLogMapper;
	
	public PmsLogMapper getPmsLogMapper() {
		return pmsLogMapper;
	}

	public void setPmsLogMapper(PmsLogMapper pmsLogMapper) {
		this.pmsLogMapper = pmsLogMapper;
	}
	
	@Override
	public void saveLog(PmsLog log) {
		pmsLogMapper.saveLog(log);
	}

	@Override
	public List<PmsLog> getPmsLog(String user, String name,String status,Date startDate, Date endDate, RowBounds bounds) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("user", user);
		paramMap.put("name", name);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		if ("1".equals(status)){
			return pmsLogMapper.getPmsLog(paramMap,bounds);
		}else {
			paramMap.put("status", status);
			return pmsLogMapper.getAllPmsLog(paramMap, bounds);
		}
	}

	@Override
	public void addOperateLog(OperateLog operateLog) {
		pmsLogMapper.addOperateLog(operateLog);
	}


}
