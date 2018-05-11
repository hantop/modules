package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.vo.bid.PlanBidVO;

import java.sql.Connection;
import java.util.List;

public interface PlanExtService {

    int doBidForPlan(Connection connection, PlanBidVO planBidVO, int accountId) throws Throwable;

    void sendSmsAndLetter(String phoneNum, String userId, String content, VersionTypeEnum versionTypeEnum);

    void useRedPackets(Integer orderId, List<UserRedPacketInfo> userRedPacketInfos, int planId, int planRecordId, UserInfo userInfo, String fxhbIds, String investType, VersionTypeEnum versionTypeEnum) throws Exception;

    void useCoupon(int planRecordId, UserInfo userInfo, String jxqId, String investType) throws Exception;

    void updateInvestPlanStatus(int planId) throws Exception;

    void sendMsg(String phoneNum, String content);
}

