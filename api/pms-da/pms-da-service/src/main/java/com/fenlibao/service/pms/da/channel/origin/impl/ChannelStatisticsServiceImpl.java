package com.fenlibao.service.pms.da.channel.origin.impl;

import com.fenlibao.dao.pms.da.channel.origin.ChannelStatisticsMapper;
import com.fenlibao.model.pms.da.channel.*;
import com.fenlibao.model.pms.da.channel.vo.ChannelStatisticsVO;
import com.fenlibao.service.pms.da.channel.channel.ChannelService;
import com.fenlibao.service.pms.da.channel.origin.ChannelStatisticsService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChannelStatisticsServiceImpl implements ChannelStatisticsService {
    @Resource
    private ChannelStatisticsMapper channelStatisticsMapper;

    @Resource
    private ChannelService channelService;


    @Override
    public List<ChannelStatisticsVO> getSortChannelStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, String channelName, String channelCode, RowBounds bounds) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        paramMap.put("channelName", channelName);
        paramMap.put("channelCode", channelCode);
        return channelStatisticsMapper.getSortChannelStatisticses(paramMap, bounds);
    }

    @Override
    public ChannelUserStatistics getChannelUserTotal(BigDecimal minimumMoney, BigDecimal maximumMoney, String channelId, Date startDate, Date endDate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("minimumMoney", minimumMoney);
        paramMap.put("maximumMoney", maximumMoney);
        paramMap.put("secondChannelId", channelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        return channelStatisticsMapper.getChannelUserTotal(paramMap);
    }

    @Override
    public List<ChannelUserStatistics> getChannelUserStatisticses(BigDecimal minimumMoney, BigDecimal maximumMoney, String channelId, Date startDate, Date endDate, RowBounds bounds) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("minimumMoney", minimumMoney);
        paramMap.put("maximumMoney", maximumMoney);
        paramMap.put("secondChannelId", channelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        return channelStatisticsMapper.getChannelUserStatisticses(paramMap, bounds);
    }

    @Override
    public BigDecimal getRegisterTotal(String firstChannelId, String secondChannelId, String channelName, String channelCode, Date startDate, Date endDate) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        paramMap.put("channelName", channelName);
        paramMap.put("channelCode", channelCode);
        return channelStatisticsMapper.getRegisterTotal(paramMap);
    }

    @Override
    public BigDecimal getAuthTotal(String firstChannelId, String secondChannelId, Date startDate, Date endDate, String channelName, String channelCode) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        paramMap.put("channelName", channelName);
        paramMap.put("channelCode", channelCode);
        return channelStatisticsMapper.getAuthTotal(paramMap);
    }

    @Override
    public InvestStatistics getInvestTotal(String firstChannelId, String secondChannelId, Date startDate, Date endDate, String channelName, String channelCode) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        paramMap.put("channelName", channelName);
        paramMap.put("channelCode", channelCode);
        return channelStatisticsMapper.getInvestTotal(paramMap);
    }

    @Override
    public RechargeStatistics getRechargeTotal(String firstChannelId, String secondChannelId, Date startDate, Date endDate, String channelName, String channelCode) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        paramMap.put("channelName", channelName);
        paramMap.put("channelCode", channelCode);
        return channelStatisticsMapper.getRechargeTotal(paramMap);
    }

    @Override
    public RedpacketStatistics getActiveRedpacketTotal(String firstChannelId, String secondChannelId, Date startDate, Date endDate, String channelName, String channelCode) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        paramMap.put("channelName", channelName);
        paramMap.put("channelCode", channelCode);
        return channelStatisticsMapper.getActiveRedpacketTotal(paramMap);
    }

    @Override
    public List<RegisterStatistics> getRegisterStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, RowBounds bounds) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        return channelStatisticsMapper.getRegisterStatisticses(paramMap, bounds);
    }

    @Override
    public List<AuthStatistics> getAuthStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, RowBounds bounds) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        return channelStatisticsMapper.getAuthStatisticses(paramMap, bounds);
    }

    @Override
    public List<InvestStatistics> getInvestStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, RowBounds bounds) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        return channelStatisticsMapper.getInvestStatisticses(paramMap, bounds);
    }

    @Override
    public List<RechargeStatistics> getRechargeStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, RowBounds bounds) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        return channelStatisticsMapper.getRechargeStatisticses(paramMap, bounds);
    }

    @Override
    public List<RedpacketStatistics> getRedpacketStatisticses(String firstChannelId, String secondChannelId, Date startDate, Date endDate, RowBounds bounds) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstChannelId", firstChannelId);
        paramMap.put("secondChannelId", secondChannelId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        return channelStatisticsMapper.getActiveRedpacketStatisticses(paramMap, bounds);
    }

    /*@Override
    public List<ChannelStatistics> getChannelStatisticses(ChannelStatisticsForm channelStatisticsForm, RowBounds bounds) {
        List<ChannelStatistics> channelStatisticses = new ArrayList<>();
        int firstChannelId = channelStatisticsForm.getFirstChannelId();
        int secondChannelId = channelStatisticsForm.getSecondChannelId();
        Date startDate = channelStatisticsForm.getStartDate();
        Date endDate = channelStatisticsForm.getEndDate();

        // 获取渠道
        List<ChannelVO> channels = channelService.getChannels(firstChannelId, secondChannelId, bounds);
        if(channels != null && channels.size() > 0) {
            ChannelStatistics statistics;
            RegisterStatistics registerStatistics;
            AuthStatistics authStatistics;
            InvestStatistics investStatistics;
            RechargeStatistics rechargeStatistics;
            RedpacketStatistics redpacketStatistics;
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("firstChannelId", firstChannelId);
            paramMap.put("secondChannelId", secondChannelId);
            paramMap.put("startDate", startDate);
            paramMap.put("endDate", endDate);
            // 注册人数
            List<RegisterStatistics> registerStatisticsList = channelStatisticsMapper.getRegisterStatisticses(paramMap, bounds);
            // 实名认证人数
            List<AuthStatistics> authStatisticsList = channelStatisticsMapper.getAuthStatisticses(paramMap, bounds);
            // 投资统计
            List<InvestStatistics> investStatisticsList = channelStatisticsMapper.getInvestStatisticses(paramMap, bounds);
            // 充值统计
            List<RechargeStatistics> rechargeStatisticsList = channelStatisticsMapper.getRechargeStatisticses(paramMap, bounds);
            // 激活红包统计
            List<RedpacketStatistics> redpacketStatisticsList = channelStatisticsMapper.getActiveRedpacketStatisticses(paramMap, bounds);
            for (int i = 0; i < channels.size(); i++) {
                statistics = new ChannelStatistics();
                ChannelVO channelVO = channels.get(i);
                registerStatistics = registerStatisticsList.get(i);
                authStatistics = authStatisticsList.get(i);
                investStatistics = investStatisticsList.get(i);
                rechargeStatistics = rechargeStatisticsList.get(i);
                redpacketStatistics = redpacketStatisticsList.get(i);
                // 设置渠道ID
                statistics.setChannelId(channelVO.getId());
                // 设置渠道名称
                statistics.setChannelName(channelVO.getName());
                // 注册人数
                statistics.setRegisterCount(registerStatistics.getRegisterCount());
                // 实名认证人数
                statistics.setAuthCount(authStatistics.getAuthCount());
                // 投资人数
                statistics.setInvestCount(investStatistics.getInvestCount());
                // 投资总额
                statistics.setInvestSum(investStatistics.getInvestSum());
                // 充值人数
                statistics.setRechargeCount(rechargeStatistics.getRechargeCount());
                // 充值总额
                statistics.setRechargeSum(rechargeStatistics.getRechargeSum());
                // 激活红包人数
                statistics.setActiveRedpacketCount(redpacketStatistics.getActiveRedpacketCount());
                // 激活红包总额
                statistics.setActiveRedpacketSum(redpacketStatistics.getActiveRedpacketSum());
                channelStatisticses.add(statistics);
            }
        }
        return channelStatisticses;
    }*/
}
