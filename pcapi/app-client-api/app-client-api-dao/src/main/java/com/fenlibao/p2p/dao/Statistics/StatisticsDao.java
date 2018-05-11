package com.fenlibao.p2p.dao.Statistics;

import com.fenlibao.p2p.model.entity.Statistic.ReportDeatil;
import com.fenlibao.p2p.model.entity.Statistic.Statistics;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */
public interface StatisticsDao {

    //平台数据统计
    Statistics getStatisticLits();

    /**
     * 获取财务审计报告列表
     * @param type
     * @return
     */
    List<ReportDeatil> getReportList(String type, PageBounds pageBounds);
}
