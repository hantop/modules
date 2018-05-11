package com.fenlibao.p2p.service.dm.hx.busi;

import java.util.Map;

import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.APPType;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;

public interface FlowBidService {
    /**
     * 流标申请
     * @param loanId 标id
     * @param reason 流标原因
     * @param clientType 客户端类型
     * @param pmsUserId 后台用户
     * @throws Exception
     * @throws TradeException
     * @return Map code CG成功/SB失败, msg 文字信息
     */
    Map<String,String> flowBidApply(int loanId, String reason,APPType clientType,Integer pmsUserId) throws Exception;
    /**
     * 定时器调用，流标结果查询和处理
     * @param hxOrder
     * @throws Exception
     */
    public RespBusinessParams queryOrder(HXOrder order) throws Exception;
}
