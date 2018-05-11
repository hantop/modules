package com.fenlibao.service.pms.da.channel.origin;


import com.fenlibao.model.pms.da.channel.*;
import com.fenlibao.model.pms.da.channel.vo.ChannelStatisticsVO;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 渠道统计
 * Created by chenzhixuan on 2015/11/10.
 */
public interface ChannelStatisticsService {

    /**
     * 获取渠道统计数据并排序
     *
     * @param firstChannelId
     * @param secondChannelId
     * @param startDate
     * @param endDate
     * @param channelName
     * @param channelCode
     * @param bounds
     * @return
     */
    List<ChannelStatisticsVO> getSortChannelStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, String channelName, String channelCode, RowBounds bounds);

    /**
     * 获取渠道对应的用户总计
     *
     * @param minimumMoney
     * @param maximumMoney
     * @param channelId
     * @param startDate
     * @param endDate
     * @return
     */
    ChannelUserStatistics getChannelUserTotal(BigDecimal minimumMoney, BigDecimal maximumMoney, String channelId, Date startDate, Date endDate);

    /**
     * 获取渠道对应的用户统计
     *
     * @param minimumMoney
     * @param maximumMoney
     * @param channelId
     * @param startDate
     * @param endDate
     * @param bounds
     * @return
     */
    List<ChannelUserStatistics> getChannelUserStatisticses(BigDecimal minimumMoney, BigDecimal maximumMoney, String channelId, Date startDate, Date endDate, RowBounds bounds);

    /**
     * 获取注册总计
     *
     * @param channelName
     * @param channelCode
     * @param startDate
     * @param endDate
     * @return
     */
    BigDecimal getRegisterTotal(String firstChannelId, String secondChannelId, String channelName, String channelCode, Date startDate, Date endDate);

    /**
     * 获取实名认证总计
     *
     * @param startDate
     * @param endDate
     * @param channelName
     * @param channelCode
     * @return
     */
    BigDecimal getAuthTotal(String firstChannelId, String secondChannelId, Date startDate, Date endDate, String channelName, String channelCode);

    /**
     * 获取投资总计
     *
     * @param startDate
     * @param endDate
     * @param channelName
     * @param channelCode
     * @return
     */
    InvestStatistics getInvestTotal(String firstChannelId, String secondChannelId, Date startDate, Date endDate, String channelName, String channelCode);

    /**
     * 获取充值总计
     *
     * @param startDate
     * @param endDate
     * @param channelName
     * @param channelCode
     * @return
     */
    RechargeStatistics getRechargeTotal(String firstChannelId, String secondChannelId, Date startDate, Date endDate, String channelName, String channelCode);

    /**
     * 获取激活红包总计
     *
     * @param startDate
     * @param endDate
     * @param channelName
     * @param channelCode
     * @return
     */
    RedpacketStatistics getActiveRedpacketTotal(String firstChannelId, String secondChannelId, Date startDate, Date endDate, String channelName, String channelCode);

    /**
     * 获取注册统计数据
     *
     * @param firstChannelId
     * @param secondChannelId
     * @param startDate
     * @param endDate
     * @param bounds
     * @return
     */
    List<RegisterStatistics> getRegisterStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, RowBounds bounds);

    /**
     * 获取实名认证统计数据
     *
     * @param firstChannelId
     * @param secondChannelId
     * @param startDate
     * @param endDate
     * @param bounds
     * @return
     */
    List<AuthStatistics> getAuthStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, RowBounds bounds);

    /**
     * 获取投资统计统计数据
     *
     * @param firstChannelId
     * @param secondChannelId
     * @param startDate
     * @param endDate
     * @param bounds
     * @return
     */
    List<InvestStatistics> getInvestStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, RowBounds bounds);

    /**
     * 获取充值统计统计数据
     *
     * @param firstChannelId
     * @param secondChannelId
     * @param startDate
     * @param endDate
     * @param bounds
     * @return
     */
    List<RechargeStatistics> getRechargeStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, RowBounds bounds);

    /**
     * 获取激活红包统计数据
     *
     * @param firstChannelId
     * @param secondChannelId
     * @param startDate
     * @param endDate
     * @param bounds
     * @return
     */
    List<RedpacketStatistics> getRedpacketStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, RowBounds bounds);

//    /**
//     * 获取渠道统计数据
//     *
//     * @param channelStatisticsForm
//     * @param bounds
//     * @return
//     */
//    List<ChannelStatistics> getChannelStatisticses(ChannelStatisticsForm channelStatisticsForm, RowBounds bounds);

}
