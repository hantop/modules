package com.fenlibao.dao.pms.da.statistics.integrate;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.fenlibao.model.pms.da.statistics.integrate.Integrate;

/**
 * 积分统计
 * @author Administrator
 *
 */
public interface IntegrateMapper {

	/**
	 * 高级查询
	 * @param paramMap
	 * @param bounds
	 * @return
	 */
	List<Integrate> query(Map<String, Object> paramMap,
            RowBounds bounds);
}
