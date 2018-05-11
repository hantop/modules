package com.fenlibao.service.pms.da.riskcontrol;

import com.fenlibao.model.pms.da.riskcontrol.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2017/11/20.
 */
public interface RiskcontrolService {


    /**
     * 查询消费信贷标列表
     *
     * @param bounds
     * @param consumerBid
     * @return
     */
    List<ConsumerBid> getConsumerBidList(RowBounds bounds, ConsumerBid consumerBid);
    RiskBaseInfo getRiskBaseInfo( ConsumerBid consumerBid);
    RiskWorkInfo getRiskWorkInfo(ConsumerBid consumerBid);
    RiskAntiFraud getRiskAntiFraud(ConsumerBid consumerBid);
    RiskAuditInfo getRiskAuditInfo(ConsumerBid consumerBid);
    RiskMutiBorrow getRiskMutiBorrow(ConsumerBid consumerBid);
}
