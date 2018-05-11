package com.fenlibao.dao.pms.idmt.log;

import com.fenlibao.model.pms.common.global.OperateLog;
import com.fenlibao.model.pms.idmt.log.PmsLog;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface PmsLogMapper {
	void saveLog(PmsLog log);

	/**
	 * 获取pms登录日志
	 * @param paramMap
	 * @param bounds
	 * @return
	 */
	List<PmsLog> getPmsLog(Map<String, Object> paramMap, RowBounds bounds);

	/**
	 * 增加操作日志
	 * @param operateLog
	 */
    void addOperateLog(OperateLog operateLog);

	/**
	 * 获取除登录以外的日志
	 * @param paramMap
	 * @param bounds
	 * @return
	 */
	List<PmsLog> getAllPmsLog(Map<String, Object> paramMap, RowBounds bounds);
}
