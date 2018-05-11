package com.fenlibao.service.pms.da.statistics.invest;

import com.fenlibao.model.pms.da.statistics.invest.ClientTypeInfo;
import com.fenlibao.model.pms.da.statistics.invest.InvestForm;
import com.fenlibao.model.pms.da.statistics.invest.InvestInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2015/11/6.
 */

public interface InvestService {

    List<InvestInfo> getInvestStatistics(InvestForm investForm, RowBounds bounds);

    List<ClientTypeInfo> getClientTypeList(Integer typeId);

    /**
     * 查询用户在投金额
     * @param userId
     * @return
     */
    InvestInfo getUserInvestDetails(String userId);

    /**
     * 查询被推广用户的相关信息
     * @param userId
     * @return
     */
    List<InvestInfo> getUserRankingDetails(String userId, RowBounds bounds);

    /**
     * 查询用户再投金额的总计
     * @param investForm
     * @return
     */
    InvestInfo getInvestTotal(InvestForm investForm);

    /**
     * 获取计划类型名称
     * @return
     */
    List<Map<String, String>> getProductTypeExtend();
}
