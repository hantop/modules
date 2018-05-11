package com.fenlibao.p2p.service.bid.impl;

import com.dimeng.util.parser.IntegerParser;
import com.fenlibao.p2p.dao.user.AutoMatchBidDao;
import com.fenlibao.p2p.model.entity.bid.InverstBidTradeInfo;
import com.fenlibao.p2p.model.entity.bid.UserCreditInfoForPlan;
import com.fenlibao.p2p.model.entity.bid.UserPlanInfo;
import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.enums.bid.OperationTypeEnum;
import com.fenlibao.p2p.model.vo.bid.PlanBidVO;
import com.fenlibao.p2p.service.bid.AutoMatchBidExtService;
import com.fenlibao.p2p.service.bid.IBidDmService;
import com.fenlibao.p2p.service.bid.migrate.impl.TenderOrderExecutor;
import com.fenlibao.p2p.service.creditassignment.TransferInService;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 计划投资辅助类
 * Created by LouisWang on 2015/8/14.
 */
@Service
public class AutoMatchBidExtServiceImpl implements AutoMatchBidExtService {
    private static final Logger logger = LogManager.getLogger(AutoMatchBidExtServiceImpl.class);

    @Resource
    private TransferInService transferInService;
    @Resource
    private IBidDmService bidDmService;
    @Resource
    private TenderOrderExecutor tenderOrderExecutor;
    @Resource
    private AutoMatchBidDao autoMatchBidDao;
    @Resource
    private SqlSession sqlSession;

    /**
     * 投资单个标
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public int doBidForPlan(PlanBidVO planBidVO, UserPlanInfo userPlanInfo) throws Throwable {
        Connection connection = sqlSession.getConnection();
        Map<String, String> rtnMap = bidDmService.doBid(connection, planBidVO.getBidId(), planBidVO.getPurchaseAmount(), userPlanInfo.getUserId(), null, null, true, OperationTypeEnum.PLAN);
        //确认订单
        Throwable throwable2 = null;
        if (VersionTypeEnum.PT.getIndex() == userPlanInfo.getCgMode()) {
            //余额投标订单
            Throwable throwable1 = tenderOrderExecutor.submitKernel(connection, IntegerParser.parse(rtnMap.get("orderId")), rtnMap);
            if (throwable1 != null) {
                tenderOrderExecutor.log(connection, IntegerParser.parse(rtnMap.get("orderId")), throwable1);
            }
            //确认订单
            throwable2 = tenderOrderExecutor.confirmKernel(connection, IntegerParser.parse(rtnMap.get("orderId")), rtnMap, false, false, false, OperationTypeEnum.PLAN);

        } else if (VersionTypeEnum.CG.getIndex() == userPlanInfo.getCgMode()) {
            try {
                //业务修改，没有再维护这个类
                //xwBidService.doBidForPlan(IntegerParser.parse(rtnMap.get("orderId")), userPlanInfo.getUserId());
            } catch (Throwable th) {
                throwable2 = th;
            }
        }
        if (throwable2 != null) {
            tenderOrderExecutor.log(connection, IntegerParser.parse(rtnMap.get("orderId")), throwable2);
            return 0;
        }else{
            // 订单详情
            InverstBidTradeInfo inverstBidTradeInfo = tenderOrderExecutor.getBidOrderDetail(connection, IntegerParser.parse(rtnMap.get("orderId")));

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", userPlanInfo.getUserId());
            map.put("userPlanId", userPlanInfo.getId());
            map.put("productType", 1);
            map.put("productId", 0);
            map.put("amount", planBidVO.getPurchaseAmount());
            map.put("tenderId", inverstBidTradeInfo.getBidRecordId());
            //插入投资/复投记录
            autoMatchBidDao.insertUserPlanProduct(map);
            userPlanInfo.setBeinvestAmount(userPlanInfo.getBeinvestAmount().subtract(planBidVO.getPurchaseAmount()));
            return inverstBidTradeInfo.getBidRecordId();
        }
    }

    /**
     * 投资单个债权
     *
     * @param userCreditInfoForPlan
     * @param userPlanInfo
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void doCreditForPlan(UserCreditInfoForPlan userCreditInfoForPlan, UserPlanInfo userPlanInfo) throws Throwable {
        Connection connection = sqlSession.getConnection();
        transferInService.purchase(connection, userCreditInfoForPlan.getApplyforId(), userPlanInfo.getUserId(), OperationTypeEnum.PLAN, userPlanInfo.getId());
        BigDecimal outPrice = userCreditInfoForPlan.getTransferOutPrice();
        //UserNewCreditInfoForPlan userNewCreditInfoForPlan = autoMatchBidService.getNewCreditInfo(userCreditInfoForPlan.getApplyforId());
        userPlanInfo.setBeinvestAmount(userPlanInfo.getBeinvestAmount().subtract(outPrice));
    }
}