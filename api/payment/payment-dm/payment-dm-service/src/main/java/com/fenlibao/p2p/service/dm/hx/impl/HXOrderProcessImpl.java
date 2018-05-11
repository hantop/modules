package com.fenlibao.p2p.service.dm.hx.impl;

import com.fenlibao.p2p.dao.dm.hx.HuaXingDao;
import com.fenlibao.p2p.model.dm.consts.HXConst;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import com.fenlibao.p2p.model.dm.message.ResponseDocument;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.service.dm.hx.HXOrderProcess;
import com.fenlibao.p2p.service.dm.hx.HXUserService;
import com.fenlibao.p2p.service.dm.hx.HuaXingService;
import com.fenlibao.p2p.service.trade.common.TradeCommonService;
import com.fenlibao.p2p.service.trade.order.OrderManageService;
import com.fenlibao.p2p.service.user.UserService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import com.fenlibao.p2p.util.dm.http.HXHttpUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 对订单的统一处理
 * Created by zcai on 2016/10/12.
 */
@Service
public class HXOrderProcessImpl implements HXOrderProcess {

    protected Logger logger = LogManager.getLogger(this.getClass());

    @Resource
    protected HuaXingDao huaXingDao;
    @Resource
    protected HXUserService hxUserService;
    @Resource
    protected HuaXingService huaXingService;
    @Resource
    protected OrderManageService orderManageService;
    @Resource
    protected TradeCommonService tradeCommonService;
    @Resource
    protected UserService userService;

    @Transactional
    @Override
    public void process(RespBusinessParams busiParams, String thirdPartyFlowNum) throws Exception {
        HXOrder order = huaXingDao.getOrderByFlowNum(busiParams.getOLDREQSEQNO());//锁定订单
        if (order != null && OrderStatus.DQR.getCode() == order.getStatus()) { //华兴的异步回调只有在成功的时候回调,所以这里只处理成功的
            doConfirm(busiParams, order);
            if (OrderStatus.N.getCode() != order.getStatus()) {//有些业务还不是最终状态所以不需要处理，交由定时器再处理
                //默认为成功，如果需要将订单置成失败，在具体业务给HXOrder状态置成失败即可
                if (OrderStatus.DQR.getCode() == order.getStatus()) {
                    order.setStatus(OrderStatus.CG.getCode());
                }
                order.setThirdPartyFlowNum(thirdPartyFlowNum);
                huaXingDao.completeOrder(order);
            }
        }
    }

    @Transactional
    @Override
    public void submit(int orderId, String flowNum, Map<String, Object> params) throws Exception {
        HXOrder order = huaXingDao.getOrderById(orderId);//锁定订单
        if (order != null && OrderStatus.DTJ.getCode() == order.getStatus()) {
            doSubmit(order, params);
            order.setFlowNum(flowNum);
            order.setStatus(OrderStatus.DQR.getCode());
            huaXingDao.submitOrder(order);
        }
    }

    @Override
    public RespBusinessParams queryOrder(HXOrder order) throws Exception {
        RespBusinessParams respBusinessParams;
        ReqBusinessParams reqParams = new ReqBusinessParams();
        doQuery(order, reqParams);//各业务封装各自独有的参数
        reqParams.setOLDREQSEQNO(order.getFlowNum());
        String message = MessageUtil.getMessageForQuery(reqParams, HXTradeType.getQueryCodeByBusiCode(order.getTypeCode()));
        String result = HXHttpUtil.doPost(message);
        ResponseDocument respdecument = MessageUtil.getXMLDocument(ResponseDocument.class, result);
        String errorCode = respdecument.getHeader().getErrorCode();
        if (!HXConst.ERRORCODE_SUCCESS.equals(errorCode)) {
            if (HXConst.ERRORCODE_FLOWNO_NOT_EXIST.equals(errorCode)) {
                respBusinessParams = new RespBusinessParams();
                respBusinessParams.setOLDREQSEQNO(order.getFlowNum());
                respBusinessParams.setRETURN_STATUS(HXConst.ERRORCODE_FLOWNO_NOT_EXIST);
                return respBusinessParams;
            }
            logger.error("[{}]华兴订单查询异常....flowNum=[{}],ErrorCode=[{}]", order.getTypeCode(), order.getFlowNum(), respdecument.getHeader().getErrorCode());
            if (HXConst.ERRORCODE_REQUEST_FREQUENTLY.equals(errorCode)) {
                return null;
            }
            return null;
        }
        String enXmlParam = respdecument.getBody().getXMLPARA();
        respBusinessParams = MessageUtil.getXmlParam(enXmlParam, RespBusinessParams.class);
        boolean isTimeout = order.getCreateTime().before(DateUtil.timeAddOrSub(new Date(), Calendar.MINUTE, -30));//华兴页面超时时间为25min
        String returnStatus = respBusinessParams.getRETURN_STATUS();
        if (!HXConst.RETURN_STATUS_F.equals(returnStatus) && !HXConst.RETURN_STATUS_S.equals(returnStatus)) {
            if (HXConst.RETURN_STATUS_R.equals(returnStatus) && isTimeout) {
                //页面处理中（客户仍停留在页面操作，25分钟后仍收到此状态可置交易为失败）
                //华兴R状态 如果用户没有继续操作目前会一直不变 所以需要判断是否超时进行结束订单
            respBusinessParams.setRETURN_STATUS(HXConst.ORDER_STATE_TIMEOUT);
//                respBusinessParams = null; //如果华兴的R状态会改变 那么可以加多一个处理
            } else {
                logger.info("[{}]华兴订单处理中....flowNum=[{}],returnStatus=[{}]", order.getTypeCode(), order.getFlowNum(), returnStatus);
                respBusinessParams = null;
            }
        }
        return respBusinessParams;
    }

    /**
     * 在提交订单之前要做的事
     * @param order
     * @param params
     * @throws Exception
     */
    protected void doSubmit(HXOrder order, Map<String, Object> params) throws Exception {

    }

    protected void doConfirm(RespBusinessParams busiParams, HXOrder order) throws Exception {

    }

    /**
     * 统一查询，具体逻辑各子业务自己实现
     * @param order
     * @throws Exception
     */
    protected void doQuery(HXOrder order, ReqBusinessParams reqParams) throws Exception {

    }

}
