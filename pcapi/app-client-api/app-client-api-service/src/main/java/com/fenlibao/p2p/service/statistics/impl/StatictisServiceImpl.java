package com.fenlibao.p2p.service.statistics.impl;

import com.fenlibao.p2p.dao.Statistics.StatisticsDao;
import com.fenlibao.p2p.model.entity.Statistic.ReportDeatil;
import com.fenlibao.p2p.model.entity.Statistic.Statistics;
import com.fenlibao.p2p.service.statistics.StatisticService;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */
@Service
public class StatictisServiceImpl implements StatisticService {

    @Resource
    StatisticsDao statisticsDao;

    /**
     * 获取平台统计数据
     * @return
     */
    @Override
    public Statistics getStatisticList() {
        return statisticsDao.getStatisticLits();
    }

    @Override
    public List<ReportDeatil> getReportList(String type,PageBounds pageBounds) {
        return statisticsDao.getReportList(type,pageBounds);
    }
}
