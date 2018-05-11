package com.fenlibao.service.pms.da.platformstatistics;

import com.fenlibao.model.pms.da.platformstatistics.Platformstatistics;
import com.fenlibao.model.pms.da.platformstatistics.PlatformstatisticsTotal;

import java.util.Date;

/**
 * 平台每日统计数据
 * Created by chenzhixuan on 2015/12/1.
 */
public interface PlatformStatisticsService {

    /**
     * 根据历史类型获取统计数据
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Platformstatistics getPlatformstatistics(Date startTime, Date endTime);

    /**
     * 平台累计数据
     * @return
     */
    PlatformstatisticsTotal getPlatformstatisticsTotal();
}
