package com.fenlibao.service.pms.da.statistics.integrate;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.fenlibao.model.pms.da.statistics.integrate.Integrate;

public interface IntegrateService {

	/**
	 * 高级查询+分页
	 * 
	 * @param integrateForm
	 * @param bounds
	 * @return
	 */
	List<Integrate> query(Date startDate, Date endDate, String productCode, String typeName,String cmd, RowBounds bounds);
}
