package com.fenlibao.dao.pms.da.riskcontrol;

import com.fenlibao.model.pms.da.riskcontrol.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */
public interface RiskControlMapper {


    List<ConsumerBid> getConsumerBidList(RowBounds bounds, @Param("consumerBid") ConsumerBid consumerBid);



    RiskBaseInfo getRiskBaseInfo(@Param("consumerBid") ConsumerBid consumerBid);
    RiskWorkInfo getRiskWorkInfo(@Param("consumerBid") ConsumerBid consumerBid);
    RiskAntiFraud getRiskAntiFraud(@Param("consumerBid") ConsumerBid consumerBid);
    RiskAuditInfo getRiskAuditInfo(@Param("consumerBid") ConsumerBid consumerBid);
    RiskMutiBorrow getRiskMutiBorrow(@Param("consumerBid") ConsumerBid consumerBid);




}
