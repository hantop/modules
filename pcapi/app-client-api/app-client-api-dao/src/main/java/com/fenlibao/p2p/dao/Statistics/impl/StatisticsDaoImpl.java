package com.fenlibao.p2p.dao.Statistics.impl;

import com.fenlibao.p2p.dao.Statistics.StatisticsDao;
import com.fenlibao.p2p.model.entity.Statistic.ReportDeatil;
import com.fenlibao.p2p.model.entity.Statistic.Statistics;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/21.
 */
@Repository
public class StatisticsDaoImpl implements StatisticsDao{

    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "StatisticsMapper.";


    @Override
    public Statistics getStatisticLits() {
        return sqlSession.selectOne(MAPPER+"getStatisticsList");
    }

    @Override
    public List<ReportDeatil> getReportList(String type, PageBounds pageBounds) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        return sqlSession.selectList(MAPPER+"getReportList",params,pageBounds);
    }
}
