package com.fenlibao.p2p.service.dm.hx.busi.impl;

import com.fenlibao.p2p.dao.dm.hx.HuaXingDao;
import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.trade.coupon.CouponManageDao;
import com.fenlibao.p2p.dao.trade.order.FlowBidOrderDao;
import com.fenlibao.p2p.dao.trade.order.OrderManageDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.APPType;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import com.fenlibao.p2p.model.dm.message.ResponseDocument;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.enums.T6230_F20;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.service.dm.hx.HuaXingService;
import com.fenlibao.p2p.service.dm.hx.busi.FlowBidService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.service.trade.order.FlowBidOrderService;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import com.fenlibao.p2p.util.dm.http.HXHttpUtil;
import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class FlowBidServiceImpl extends HXOrderProcessImpl implements FlowBidService{

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Resource
	BidManageDao bidManageDao;
	
    @Resource
    private HuaXingService huaXingService;
    
    @Resource
    private FlowBidOrderService flowBidOrderService;
    
    @Resource
    HuaXingDao huaXingDao;
    
    @Resource
    BidManageService bidManageService;
    
	@Resource
	TradeCommonDao tradeCommonDao;
	
	@Resource
	UserDao userDao;
	
	@Resource 
	OrderManageDao orderManageDao;
	
	@Resource
	FlowBidOrderDao flowBidOrderDao;
	
	@Resource
	CouponManageDao couponManageDao;
	
    /**
     * 这只是流标申请，结果和对结果的处理还要在定时器查完结果后进行
     */
	@Override
	public Map<String,String> flowBidApply(int loanId, String reason,APPType clientType,Integer pmsUserId) throws Exception {
        //防止重复操作
        Map<String,Object> params=new HashMap<>(3);
        params.put("businessId", loanId);
        params.put("status", OrderStatus.DQR.getCode());
        params.put("typeCode", HXTradeType.LB.getBusiCode());
        HXOrder oldOrder= huaXingDao.getOrder(params);
        if(oldOrder!=null){
        	throw new TradeException(TradeResponseCode.TRADE_OPERATION_REPEAT);
        }
        
		if(loanId <= 0){
			throw new TradeException(TradeResponseCode.COMMON_PARAM_WRONG);
		}
		T6230 t6230=bidManageDao.getBidById(loanId);
		if(t6230==null){
			throw new TradeException(TradeResponseCode.BID_NOT_EXIST);
		}
		if(t6230.F38!=2){
			throw new TradeException(TradeResponseCode.BID_TYPE_NOT_DM);
		}
        if (t6230.F20 != T6230_F20.DFK && t6230.F20 != T6230_F20.TBZ)
        {
        	throw new TradeException(TradeResponseCode.TRADE_FLOW_CONDITIONS_NOT_SATISFIED);
        }

		// 创建订单
		HXOrder order = new HXOrder().fromPms(pmsUserId, HXTradeType.LB.getBusiCode(), loanId);
		int clientCode=clientType.getCode();
		huaXingService.createOrder(clientCode, order);
		//获取华兴流标操作编码
		String tradeCode= HXTradeType.getTradeCode(HXTradeType.LB, clientCode);
		//用6501的流水号标识同一次操作，先提交迪蒙订单，再提交华兴订单
		//生成一个流水号
		String channelFlow=HXUtil.getChannelFlow(tradeCode,order.getId());
		flowBidOrderService.createOrder(loanId,channelFlow, pmsUserId);
        //流标申请
        String result=sendApply(t6230,reason,clientCode,channelFlow);
        ResponseDocument resp= MessageUtil.getXMLDocument(ResponseDocument.class,result);
        //保存返回的报文
        huaXingService.saveMessage(resp.getBody().getTRANSCODE(), channelFlow, resp.getHeader().getChannelFlow(), result);
        //完成华兴订单
    	String errorCode=resp.getHeader().getErrorCode();
    	boolean hxOrderSuccess="0".equals(errorCode);
    	Map<String,String> returnMap=new HashMap<>();
        if(hxOrderSuccess){
            submit(order.getId(),channelFlow, null);
            flowBidOrderService.updateOrder(T6501_F03.DQR,channelFlow);
        	logger.info("订单"+order.getId()+"流标申请成功受理");
        	returnMap.put("code", "CG");
        	returnMap.put("msg", "流标申请成功受理");
        }
        else{
        	order.setStatus(OrderStatus.SB.getCode());
        	huaXingDao.completeOrder(order);
        	//如果流标申请没被受理，直接把t6501的订单改为失败
        	flowBidOrderService.updateOrder(T6501_F03.SB, channelFlow);
        	logger.info("标"+loanId+"流标申请失败："+resp.getHeader().getErrorMsg());
        	returnMap.put("code", "SB");
        	returnMap.put("msg", "流标申请失败："+resp.getHeader().getErrorMsg());
        }
        return returnMap;
	}
    
    private String sendApply(T6230 t6230,String reason,Integer clientType,String channelFlow) throws Exception{
    	HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
    	ReqBusinessParams businessParams = new ReqBusinessParams();
    	businessParams.setLOANNO(t6230.F25);
    	businessParams.setCANCELREASON(reason==null?"":reason);
    	String message = MessageUtil.getMessageByBusi(businessParams, HXTradeType.getTradeCode(HXTradeType.LB, clientType), channelFlow, clientType);
    	String result = HXHttpUtil.doPost(config.formSubmitUrl(), message);
    	return result;
    }

    /**
     * 供定时器调用的流标结果查询和处理
     */
	@Override
	public RespBusinessParams queryOrder(HXOrder hxOrder) throws Exception {
		if(hxOrder.getTypeCode()!=HXTradeType.LB.getBusiCode()){
			throw new TradeException(TradeResponseCode.ORDER_TRADE_TYPE_WRONG);
		}
		RespBusinessParams respParam=null;
		Integer clientType=huaXingDao.getClientType(hxOrder.getId());
		String result=queryResult(hxOrder.getFlowNum(),clientType);
        ResponseDocument resp= MessageUtil.getXMLDocument(ResponseDocument.class,result);
        //保存返回的报文
        huaXingService.saveMessage(resp.getBody().getTRANSCODE(), hxOrder.getFlowNum(), resp.getHeader().getChannelFlow(), result);
        String errorCode=resp.getHeader().getErrorCode();
        if("0".equals(errorCode)){
            //对返回报文进行处理
            String enXmlParam = resp.getBody().getXMLPARA();
            respParam = MessageUtil.getXmlParam(enXmlParam, RespBusinessParams.class);
            String returnStatus=respParam.getRETURN_STATUS();
            if("S".equals(returnStatus)){
            	//结束华兴订单
            	process(respParam,hxOrder.getThirdPartyFlowNum());
            	//结束平台订单
            	flowBidOrderService.updateOrder(T6501_F03.CG, hxOrder.getFlowNum());
            	logger.info("标"+hxOrder.getBusinessId()+"流标成功");
            }
            else if("F".equals(returnStatus)){
            	//结束华兴订单
            	hxOrder.setStatus(OrderStatus.SB.getCode());
            	huaXingDao.completeOrder(hxOrder);
            	//结束平台订单
            	flowBidOrderService.updateOrder(T6501_F03.SB, hxOrder.getFlowNum());
            	logger.info("标"+hxOrder.getBusinessId()+"流标失败");
            }
        }
        else{
        	logger.info("标"+hxOrder.getBusinessId()+"流标申请失败："+resp.getHeader().getErrorMsg());
        }
        return null;
	}
	
	private String queryResult(String channelFlow,Integer clientType) throws Exception{
    	HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
    	ReqBusinessParams businessParams = new ReqBusinessParams();
    	businessParams.setOLDREQSEQNO(channelFlow);
    	businessParams.setOLDTTJNL("");
    	String message = MessageUtil.getMessageForQuery(businessParams, HXTradeType.LB.getQueryCode());
    	String result = HXHttpUtil.doPost(config.formSubmitUrl(), message);
    	return result;
	}
	
    @Override
    protected void doConfirm(RespBusinessParams busiParams, HXOrder order) throws Exception {
    	bidManageService.flowBid(order.getBusinessId(), order.getFlowNum());
    }
}
