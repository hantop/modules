package com.fenlibao.dao.pms.da.cs.logInfo;

import com.fenlibao.model.pms.da.cs.LogInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

public interface LogInfoMapper {

    /**
     *
     * @param userIds
     * @param startTime
     * @param endTime
     * @param bounds
     * @return
     */
    List<LogInfo> getLogInfoList(@Param(value = "types") List<Integer> types,
                                 @Param(value = "userIds") List<Integer> userIds,
                                 @Param(value = "startTime") Date startTime,
                                 @Param(value = "endTime") Date endTime,
                                 RowBounds bounds);

    /**
     * 保存用户操作日志
     * @return
     */
    void addUserLog(LogInfo logInfo);
}
