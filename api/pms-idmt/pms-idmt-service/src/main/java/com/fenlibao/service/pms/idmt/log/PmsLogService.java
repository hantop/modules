package com.fenlibao.service.pms.idmt.log;

import com.fenlibao.model.pms.common.global.OperateLog;
import com.fenlibao.model.pms.idmt.log.PmsLog;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PmsLogService {
   void saveLog(PmsLog log);
   /**
    * 获取pms登录日志
    * @param bounds
    * @return
    */
   List<PmsLog> getPmsLog(String user, String name,String status,Date startDate, Date endDate, RowBounds bounds);

   /**
    * 增加操作日志
    * @param operateLog
    */
   void addOperateLog(OperateLog operateLog);
}
