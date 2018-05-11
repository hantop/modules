package com.fenlibao.dao.pms.da.platformstatistics;

import com.fenlibao.model.pms.da.platformstatistics.Platformstatistics;
import com.fenlibao.model.pms.da.platformstatistics.PlatformstatisticsTotal;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 平台每日统计数据
 * Created by chenzhixuan on 2015/12/1.
 */
public interface PlatformstatisticsMapper {
    /**
     * 根据历史类型获取统计数据
     * @param startTime
     * @param endTime
     * @return
     */
    Platformstatistics getPlatformstatistics(
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    /**
     * 平台累计数据
     * @return
     */
    PlatformstatisticsTotal getPlatformstatisticsTotal();
}
