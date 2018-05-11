package com.fenlibao.dao.pms.da.channel.origin;


import com.fenlibao.model.pms.da.channel.*;
import com.fenlibao.model.pms.da.channel.vo.ChannelStatisticsVO;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 渠道统计
 * Created by chenzhixuan on 2015/11/10.
 */
public interface ChannelStatisticsMapper {

    /**
     * 获取渠道统计数据并排序
     * @param paramMap
     * @param bounds
     * @return
     */
    List<ChannelStatisticsVO> getSortChannelStatisticses(Map<String, Object> paramMap, RowBounds bounds);

    /**
     * 获取渠道对应的用户总计
     * @param paramMap
     * @return
     */
    ChannelUserStatistics getChannelUserTotal(Map<String, Object> paramMap);

    /**
     * 获取渠道对应的用户统计
     * @param paramMap
     * @param bounds
     * @return
     */
    List<ChannelUserStatistics> getChannelUserStatisticses(Map<String, Object> paramMap, RowBounds bounds);

    /**
     * 获取激活红包总计
     * @param paramMap
     * @return
     */
    RedpacketStatistics getActiveRedpacketTotal(Map<String, Object> paramMap);

    /**
     * 获取投资总计
     * @param paramMap
     * @return
     */
    InvestStatistics getInvestTotal(Map<String, Object> paramMap);

    /**
     * 获取充值总计
     * @param paramMap
     * @return
     */
    RechargeStatistics getRechargeTotal(Map<String, Object> paramMap);

    /**
     * 获取注册总计
     * @param paramMap
     * @return
     */
    BigDecimal getRegisterTotal(Map<String, Object> paramMap);

    /**
     * 获取实名认证总计
     * @param paramMap
     * @return
     */
    BigDecimal getAuthTotal(Map<String, Object> paramMap);

    /**
     * 获取激活红包统计数据
     * @param paramMap
     * @param bounds
     * @return
     */
    List<RedpacketStatistics> getActiveRedpacketStatisticses(Map<String, Object> paramMap, RowBounds bounds);

    /**
     * 获取充值统计数据
     * @param paramMap
     * @param bounds
     * @return
     */
    List<RechargeStatistics> getRechargeStatisticses(Map<String, Object> paramMap, RowBounds bounds);

    /**
     * 获取注册统计数据
     *
     * @param paramMap
     * @param bounds
     * @return
     */
    List<RegisterStatistics> getRegisterStatisticses(Map<String, Object> paramMap, RowBounds bounds);

    /**
     * 获取实名认证统计数据
     *
     * @param paramMap
     * @param bounds
     * @return
     */
    List<AuthStatistics> getAuthStatisticses(Map<String, Object> paramMap, RowBounds bounds);

    /**
     * 获取投资统计数据
     *
     * @param paramMap
     * @param bounds
     * @return
     */
    List<InvestStatistics> getInvestStatisticses(Map<String, Object> paramMap, RowBounds bounds);

}