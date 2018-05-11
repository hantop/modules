package com.fenlibao.service.pms.da.platformstatistics.impl;

import com.fenlibao.dao.pms.da.platformstatistics.PlatformstatisticsMapper;
import com.fenlibao.model.pms.da.platformstatistics.Platformstatistics;
import com.fenlibao.model.pms.da.platformstatistics.PlatformstatisticsTotal;
import com.fenlibao.service.pms.da.platformstatistics.PlatformStatisticsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 平台每日统计数据
 * Created by chenzhixuan on 2015/12/1.
 */
@Service
public class PlatformStatisticsServiceImpl implements PlatformStatisticsService {
    @Resource
    private PlatformstatisticsMapper platformstatisticsMapper;

    @Override
    public Platformstatistics getPlatformstatistics(Date startTime, Date endTime) {
        return platformstatisticsMapper.getPlatformstatistics(startTime, endTime);
    }

    @Override
    public PlatformstatisticsTotal getPlatformstatisticsTotal() {
        return platformstatisticsMapper.getPlatformstatisticsTotal();
    }
}
