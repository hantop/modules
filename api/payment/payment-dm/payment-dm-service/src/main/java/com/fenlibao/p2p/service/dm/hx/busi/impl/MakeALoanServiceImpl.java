package com.fenlibao.p2p.service.dm.hx.busi.impl;

import com.fenlibao.p2p.dao.dm.hx.HXUserDao;
import com.fenlibao.p2p.dao.dm.hx.HuaXingDao;
import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.APPType;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import com.fenlibao.p2p.model.dm.message.ResponseDocument;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.entity.T6238;
import com.fenlibao.p2p.model.trade.enums.T6230_F20;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.service.dm.hx.HuaXingService;
import com.fenlibao.p2p.service.dm.hx.busi.HXCommonService;
import com.fenlibao.p2p.service.dm.hx.busi.MakeALoanService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.service.trade.bid.OtherTaskOfMakeALoan;
import com.fenlibao.p2p.service.trade.order.MakeALoanOrderService;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import com.fenlibao.p2p.util.dm.http.HXHttpUtil;
import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class MakeALoanServiceImpl extends HXOrderProcessImpl implements MakeALoanService{

	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Resource
	BidManageDao bidManageDao;
	
    @Resource
    HuaXingService huaXingService;
    
    @Resource
    MakeALoanOrderService makeALoanOrderService;
    
    @Resource
    HXUserDao hxUserDao;
    
    @Resource
    UserDao userDao;
    
    @Resource
    private HuaXingDao huaXingDao;
    
    @Resource
    private BidManageService bidManageService;
    
    @Resource
    HXCommonService hxCommonService;
    
    @Resource
    OtherTaskOfMakeALoan otherTaskOfMakeALoan;
	
	/**
	 * 此接口只放款，如果没有人投资，请调用流标接口
	 * @param loanId 标id
	 * @param clientType 客户端类型
	 * @param pmsUserId 后台用户id
	 * @throws Exception
	 */
	@Override
	public Map<String,String> makeALoanApply(int loanId, APPType clientType, Integer pmsUserId) throws Exception {
        //防止重复操作
        Map<String,Object> params=new HashMap<>(3);
        params.put("businessId", loanId);
        params.put("status", OrderStatus.DQR.getCode());
        params.put("typeCode", HXTradeType.FK.getBusiCode());
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
        if (t6230.F20 != T6230_F20.DFK)
        {
        	throw new TradeException(TradeResponseCode.TRADE_MAKE_A_LOAN_CONDITIONS_NOT_SATISFIED);
        }
		// 创建华兴订单
		HXOrder order = new HXOrder().fromPms(pmsUserId, HXTradeType.FK.getBusiCode(), loanId);
		order.setUserId(t6230.F02);
		int clientCode=clientType.getCode();
		huaXingService.createOrder(clientCode, order);
		//获取华兴放款操作编码
		String tradeCode= HXTradeType.getTradeCode(HXTradeType.FK, clientCode);
		//用6501的流水号标识同一次操作，先提交迪蒙订单，再提交华兴订单
		//生成一个流水号
		String channelFlow=HXUtil.getChannelFlow(tradeCode,order.getId());
		makeALoanOrderService.createOrder(loanId, channelFlow, pmsUserId);
        //放款申请
        String result=sendApply(t6230,clientCode,channelFlow);
        ResponseDocument resp= MessageUtil.getXMLDocument(ResponseDocument.class,result);
        //保存返回的报文
        huaXingService.saveMessage(resp.getBody().getTRANSCODE(), channelFlow, resp.getHeader().getChannelFlow(), result);
        //完成华兴订单
    	String errorCode=resp.getHeader().getErrorCode();
    	boolean hxOrderSuccess="0".equals(errorCode);
    	Map<String,String> returnMap=new HashMap<>();
        if(hxOrderSuccess){
    		makeALoanOrderService.updateOrder(T6501_F03.DQR,channelFlow);
    		submit(order.getId(),channelFlow, null);
    		logger.info("订单"+order.getId()+"放款申请成功受理");
        	returnMap.put("code", "CG");
        	returnMap.put("msg", "放款申请成功受理");
        }
        else{
        	order.setStatus(OrderStatus.SB.getCode());
        	huaXingDao.completeOrder(order);
        	//如果放款申请没被受理，直接把t6501的订单改为失败
        	makeALoanOrderService.updateOrder(T6501_F03.SB, channelFlow);
        	logger.info("订单"+order.getId()+"放款申请失败");
        	returnMap.put("code", "SB");
        	returnMap.put("msg", "放款申请失败："+resp.getHeader().getErrorMsg());
        }
        return returnMap;
	}
    
    private String sendApply(T6230 t6230,int clientType,String channelFlow) throws Exception{
    	HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
    	HXAccountInfo hxAccountInfo =hxUserDao.getAccountInfo(t6230.F02);
    	if(hxAccountInfo==null){
    		throw new TradeException(TradeResponseCode.FUND_ACCOUNT_NOT_EXIST);
    	}
    	ReqBusinessParams businessParams = new ReqBusinessParams();
    	businessParams.setLOANNO(t6230.F25);
    	businessParams.setBWACNAME(userDao.get(t6230.F02, null).getFullName());
    	businessParams.setBWACNO(StringHelper.decode(hxAccountInfo.getAcNo()));
    	T6238 t6238=bidManageDao.getBidRateById(t6230.F01);
    	//成交服务费
    	BigDecimal transactionFee=t6238.F02.multiply(t6230.F05.subtract(t6230.F07)).setScale(2, RoundingMode.HALF_UP);
    	//代收手续费
    	BigDecimal dssxf=t6238.F08;
    	businessParams.setACMNGAMT((transactionFee.add(dssxf)).setScale(2, RoundingMode.HALF_UP).toString());
    	businessParams.setGUARANTAMT("");
    	businessParams.setREMARK("");
    	String message = MessageUtil.getMessageByBusi(businessParams, HXTradeType.getTradeCode(HXTradeType.FK, clientType), channelFlow, clientType);
    	String result = HXHttpUtil.doPost(config.formSubmitUrl(), message);
    	return result;
    }

	@Override
	public RespBusinessParams queryOrder(HXOrder hxOrder) throws Exception {
		if(hxOrder.getTypeCode()!=HXTradeType.FK.getBusiCode()){
			throw new TradeException(TradeResponseCode.ORDER_TRADE_TYPE_WRONG);
		}
		Integer clientType=huaXingDao.getClientType(hxOrder.getId());
		String result=queryResult(hxOrder.getFlowNum(),clientType,hxOrder.getBusinessId());
		ResponseDocument resp= MessageUtil.getXMLDocument(ResponseDocument.class,result);
        //保存返回的报文
        huaXingService.saveMessage(resp.getBody().getTRANSCODE(), hxOrder.getFlowNum(), resp.getHeader().getChannelFlow(), result);
        String errorCode=resp.getHeader().getErrorCode();
        RespBusinessParams respParam=null;
//        if("0".equals(errorCode)){
//            //对返回报文进行处理
//            String enXmlParam = resp.getBody().getXMLPARA();
//            respParam = MessageUtil.getXmlParam(enXmlParam, RespBusinessParams.class);
//            String returnStatus=respParam.getRETURN_STATUS();
//            if("S".equals(returnStatus)){
//            	//结束华兴订单
//            	process(respParam,hxOrder.getThirdPartyFlowNum());
//            	//结束平台订单
//            	makeALoanOrderService.updateOrder(T6501_F03.CG, hxOrder.getFlowNum());
//            	logger.info("标"+hxOrder.getBusinessId()+"放款成功");
//                //发信息
//            	BigDecimal balance=hxCommonService.getBalance(hxOrder.getUserId()).getBalance();
//            	otherTaskOfMakeALoan.sendLetterAndMsg(hxOrder.getBusinessId(),hxOrder.getFlowNum(),balance);
//            }
//            else if("F".equals(returnStatus)){
//            	//结束华兴订单
//            	hxOrder.setStatus(OrderStatus.SB.getCode());
//            	huaXingDao.completeOrder(hxOrder);
//            	//结束平台订单
//            	makeALoanOrderService.updateOrder(T6501_F03.SB, hxOrder.getFlowNum());
//            	logger.info("标"+hxOrder.getBusinessId()+"放款失败");
//            	throw new RuntimeException("放款失败");
//            }
//            else if("L".equals(returnStatus)){
//            	logger.info("标"+hxOrder.getBusinessId()+"放款处理中");
//            	throw new RuntimeException("放款处理中");
//            }
//        }
//        else{
//        	logger.info("标"+hxOrder.getBusinessId()+"查询放款结果失败："+resp.getHeader().getErrorMsg());
//        	throw new RuntimeException("查询放款结果失败"+resp.getHeader().getErrorMsg());
//        }
        
        //模拟代码开始
    	//结束华兴订单
        respParam=new RespBusinessParams();
        respParam.setOLDREQSEQNO(hxOrder.getFlowNum());
    	process(respParam,hxOrder.getThirdPartyFlowNum());
    	//结束平台订单
    	makeALoanOrderService.updateOrder(T6501_F03.CG, hxOrder.getFlowNum());
    	logger.info("标"+hxOrder.getBusinessId()+"放款成功");
        //发信息
    	BigDecimal balance=hxCommonService.getBalance(hxOrder.getUserId()).getBalance();
    	otherTaskOfMakeALoan.sendLetterAndMsg(hxOrder.getBusinessId(),hxOrder.getFlowNum(),balance);
    	//模拟代码结束
        
        return null;
	}
	
	private String queryResult(String channelFlow,Integer clientType,Integer loanId) throws Exception{
    	HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
    	T6230 t6230 = bidManageDao.getBidById(loanId);
    	ReqBusinessParams businessParams = new ReqBusinessParams();
    	businessParams.setOLDREQSEQNO(channelFlow);
    	businessParams.setLOANNO(t6230.F25);
    	businessParams.setOLDTTJNL("");
    	String message = MessageUtil.getMessageForQuery(businessParams, HXTradeType.FK.getQueryCode());
    	String result = HXHttpUtil.doPost(config.formSubmitUrl(), message);
    	return result;
	}
	
	@Override
	protected void doConfirm(RespBusinessParams busiParams, HXOrder order) throws Exception {
		HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
		bidManageService.makeALoan(order.getBusinessId(), order.getFlowNum(),config.platformHxWlzh());
	}

}
