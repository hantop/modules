package com.fenlibao.service.pms.da.riskcontrol.impl;

import com.fenlibao.dao.pms.da.riskcontrol.RiskControlMapper;
import com.fenlibao.model.pms.da.riskcontrol.*;
import com.fenlibao.service.pms.da.riskcontrol.RiskcontrolService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */
@Service
public class RiskcontrolServiceImpl implements RiskcontrolService {


    @Resource
    RiskControlMapper riskControlMapper;
    @Override
    public List<ConsumerBid> getConsumerBidList(RowBounds bounds, ConsumerBid consumerBid) {


        return riskControlMapper.getConsumerBidList(bounds,consumerBid);
    }

    @Override
    public RiskBaseInfo getRiskBaseInfo(ConsumerBid consumerBid) {
        return riskControlMapper.getRiskBaseInfo(consumerBid);
    }

    @Override
    public RiskWorkInfo getRiskWorkInfo(ConsumerBid consumerBid) {
        return riskControlMapper.getRiskWorkInfo(consumerBid);
    }

    @Override
    public RiskAntiFraud getRiskAntiFraud(ConsumerBid consumerBid) {
        return riskControlMapper.getRiskAntiFraud(consumerBid);
    }

    @Override
    public RiskAuditInfo getRiskAuditInfo(ConsumerBid consumerBid) {
        return riskControlMapper.getRiskAuditInfo(consumerBid);
    }

    @Override
    public RiskMutiBorrow getRiskMutiBorrow(ConsumerBid consumerBid) {
        return riskControlMapper.getRiskMutiBorrow(consumerBid);
    }
}
