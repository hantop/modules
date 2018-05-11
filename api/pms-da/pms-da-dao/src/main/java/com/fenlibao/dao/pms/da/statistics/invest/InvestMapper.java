package com.fenlibao.dao.pms.da.statistics.invest;

import com.fenlibao.model.pms.da.statistics.invest.ClientTypeInfo;
import com.fenlibao.model.pms.da.statistics.invest.InvestForm;
import com.fenlibao.model.pms.da.statistics.invest.InvestInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 用户个人资产投资统计
 * Created by Louis Wang on 2015/11/6.
 */
public interface InvestMapper {

    List<InvestInfo> getInvestStatistics(Map<String, Object> paramMap, RowBounds bounds);

    List<ClientTypeInfo> getClientTypeList(Map param);

    InvestInfo getUserInvestDetails(Integer userId);

    List<InvestInfo> getUserRankingDetails(Integer userId, RowBounds bounds);

    List<InvestInfo> getInvestDetail(InvestForm investForm, RowBounds bounds);

    InvestInfo getInvestTotal(InvestForm paramMap);

    /**
     * 累计投资次数,累计投资金额
     * @param userIds
     * @return
     */
    List<Map<String, Object>> getInvestMoneyAndTimesTotal(
            @Param("userIds") List<Integer> userIds
    );

    /**
     * 基础信息(姓名,账户余额)
     * @param userIds
     * @return
     */
    List<Map<String, Object>> getUserBaseInfo(
            @Param("userIds") List<Integer> userIds
    );

    List<Map<String, String>> getProductTypeExtend();
}
