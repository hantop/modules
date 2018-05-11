package com.fenlibao.p2p.service.dm.hx.busi.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.aeonbits.owner.ConfigCache;
import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.dm.hx.HXUserDao;
import com.fenlibao.p2p.dao.dm.hx.HuaXingDao;
import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.trade.order.PrepayOrderDao;
import com.fenlibao.p2p.dao.trade.order.RepayOrderDao;
import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.entity.HXRepayOrder;
import com.fenlibao.p2p.model.dm.enums.APPType;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import com.fenlibao.p2p.model.dm.message.ResponseDocument;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.entity.T6252;
import com.fenlibao.p2p.model.trade.enums.T6230_F20;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.service.dm.hx.HuaXingService;
import com.fenlibao.p2p.service.dm.hx.busi.HXRepayService;
import com.fenlibao.p2p.service.dm.hx.busi.RepayDetailSubmitService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.service.trade.order.PrepayOrderService;
import com.fenlibao.p2p.service.trade.order.RepayOrderService;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import com.fenlibao.p2p.util.dm.http.HXHttpUtil;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class HXRepayServiceImpl extends HXOrderProcessImpl implements HXRepayService{

	@Resource
	BidManageDao bidManageDao;
	
	@Resource
	HXUserDao hxUserDao;
	
    @Resource
    HuaXingService huaXingService;
    
    @Resource
    RepayOrderService repayOrderService;
    
    @Resource
    HuaXingDao huaXingDao;
    
    @Resource
    BidManageService bidManageService;
    
    @Resource
    PrepayOrderService prepayOrderService;
    
    @Resource
    TradeCommonDao tradeCommonDao;
    
    @Resource
    RepayOrderDao repayOrderDao; 
    
    @Resource
    PrepayOrderDao prepayOrderDao; 
    
    @Resource
    RepayDetailSubmitService repayDetailSubmitService;
    
	@Override
	public String repayApply(int loanId, int term, String remark,
			String returnURI, APPType clientType,HXTradeType hxTradeType,int userId) throws Exception {
		if(hxTradeType!=HXTradeType.HK&&hxTradeType!=HXTradeType.TQHK){
			throw new TradeException(TradeResponseCode.COMMON_PARAM_WRONG);
		}
		//防止重复操作
        Map<String,Object> params=new HashMap<>(3);
        params.put("businessId", loanId);
        params.put("status", OrderStatus.DQR.getCode());
        params.put("typeCode", HXTradeType.HK.getBusiCode());
        HXOrder oldOrder= huaXingDao.getOrder(params);
        if(oldOrder!=null){
        	throw new TradeException(TradeResponseCode.TRADE_OPERATION_REPEAT);
        }
        params.put("typeCode", HXTradeType.TQHK.getBusiCode());
        oldOrder= huaXingDao.getOrder(params);
        if(oldOrder!=null){
        	throw new TradeException(TradeResponseCode.TRADE_OPERATION_REPEAT);
        }
		
		T6230 t6230=bidManageDao.getBidById(loanId);
		if (t6230 == null) {
			throw new TradeException(TradeResponseCode.BID_NOT_EXIST);
		}
		if(t6230.F38!=2){
			throw new TradeException(TradeResponseCode.BID_TYPE_NOT_DM);
		}
		if (t6230.F20 != T6230_F20.HKZ && t6230.F20 != T6230_F20.YDF) {
			throw new TradeException(TradeResponseCode.TRADE_REPAY_CONDITIONS_NOT_SATISFIED);
		}
		if(t6230.F02!=userId){
			throw new TradeException(TradeResponseCode.TRADE_REPAY_BORROWER_WRONG);
		}
		//是否有设置过垫付
		//dfflag 还款类型 1=正常还款    2=垫付后，借款人还款
		//oldreqseqno 原垫付请求流水号
		int dfflag=1;
		String oldreqseqno="";
		Map<String,Object> param =new HashMap<>(3);
		param.put("businessId", loanId);
		param.put("status", OrderStatus.CG.getCode());
		param.put("typeCode",HXTradeType.HKDF.getBusiCode());
		HXOrder dfOrder= huaXingDao.getOrder(param);
		if(dfOrder!=null){
			dfflag=2;
			oldreqseqno=dfOrder.getFlowNum();
		}
		//借款人账户
		HXAccountInfo accountInfo=hxUserDao.getAccountInfo(t6230.F02);
		if(accountInfo==null){
			throw new TradeException(TradeResponseCode.FUND_ACCOUNT_NOT_EXIST);
		}
		// 创建订单
		HXOrder order = new HXOrder().fromUser(t6230.F02, hxTradeType.getBusiCode(), loanId);
		int clientCode=clientType.getCode();
		huaXingService.createOrder(clientCode, order);
		HXRepayOrder repayOrder=new HXRepayOrder(order.getId(),term,dfflag);
		huaXingDao.createRepayOrder(repayOrder);
		//获取华兴还款操作编码
		String tradeCode= HXTradeType.getTradeCode(HXTradeType.HK, clientCode);
		//生成一个流水号
		String channelFlow=HXUtil.getChannelFlow(tradeCode,order.getId());
		//平台订单
		List<T6252> listToRepay=null;
		if(HXTradeType.HK.getBusiCode()==hxTradeType.getBusiCode()){
			listToRepay=repayOrderService.createOrder(loanId, term, channelFlow, null);
		}
		else if(HXTradeType.TQHK.getBusiCode()==hxTradeType.getBusiCode()){
			listToRepay=prepayOrderService.createOrder(loanId, term, channelFlow, null);
		}
        BigDecimal amount=BigDecimal.ZERO;
        //算总数
        amount=getTotalRepayAmount(listToRepay);
		submit(order.getId(),channelFlow, null);
		//构建请求报文
		String requestMsg=buildRequestMessage(dfflag,oldreqseqno,t6230.F25,accountInfo,amount.setScale(2, RoundingMode.HALF_UP).toString(),remark,returnURI,clientCode,channelFlow);
		return requestMsg;
	}
	
    private String buildRequestMessage(int dfflag, String oldreqseqno,String loanCode,HXAccountInfo accountInfo,String amount,String remark,String returnURI,int clientType,String channelFlow) throws Exception{
    	HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
    	ReqBusinessParams businessParams = new ReqBusinessParams();
    	businessParams.setTTRANS(String.valueOf(HXTradeType.HK.getCode()));
    	businessParams.setDFFLAG(""+dfflag);
    	businessParams.setOLDREQSEQNO(oldreqseqno);
    	businessParams.setLOANNO(loanCode);
    	businessParams.setBWACNAME(accountInfo.getAcName());
    	businessParams.setBWACNO(StringHelper.decode(accountInfo.getAcNo()));
    	businessParams.setAMOUNT(amount);
    	businessParams.setREMARK(remark);
    	businessParams.setRETURNURL(config.serverDomain()+returnURI);
    	String message = MessageUtil.getMessageByBusi(businessParams, HXTradeType.getTradeCode(HXTradeType.HK, clientType), channelFlow, clientType);
    	return message;
    }
    
    @Override
    protected void doConfirm(RespBusinessParams busiParams, HXOrder order) throws Exception {
    	repayDetailSubmitService.repayDetailSubmit(order);
    }

	@Override
	public RespBusinessParams queryOrder(HXOrder hxOrder) throws Exception {
		if(hxOrder.getTypeCode()!=HXTradeType.HK.getBusiCode()&&hxOrder.getTypeCode()!=HXTradeType.TQHK.getBusiCode()){
			throw new TradeException(TradeResponseCode.ORDER_TRADE_TYPE_WRONG);
		}
		//因为在查询明细提交结果后才能结束还款订单，所以如果已经创建了提交明细订单，就不用再运行提交明细了
        Map<String,Object> params=new HashMap<>(3);
        params.put("businessId", hxOrder.getBusinessId());
        params.put("status", OrderStatus.DQR.getCode());
        params.put("typeCode", HXTradeType.HKMX.getBusiCode());
        HXOrder mxOrder= huaXingDao.getOrder(params);
        if(mxOrder!=null){
        	return null;
        }
		
		Integer clientType=huaXingDao.getClientType(hxOrder.getId());
		String result=queryApplyResult(hxOrder.getFlowNum(),clientType,hxOrder.getBusinessId());
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
//            	logger.info("标"+hxOrder.getBusinessId()+"还款申请成功");
//            	//提交明细
//            	process(respParam,hxOrder.getThirdPartyFlowNum());
//            }
//            else if("F".equals(returnStatus)){
//            	//结束华兴订单
//            	hxOrder.setStatus(OrderStatus.SB.getCode());
//            	huaXingDao.completeOrder(hxOrder);
//            	logger.info("标"+hxOrder.getBusinessId()+"还款申请失败");
//            }
//            else{
//            	logger.info("标"+hxOrder.getBusinessId()+"还款申请处理中");
//            }
//        }
//        else{
//        	logger.info("标"+hxOrder.getBusinessId()+"查询还款结果失败："+resp.getHeader().getErrorMsg());
//        }
        
        //模拟代码开始
    	logger.info("标"+hxOrder.getBusinessId()+"还款申请成功");
    	respParam=new RespBusinessParams();
    	respParam.setOLDREQSEQNO(hxOrder.getFlowNum());
    	//提交明细
    	process(respParam,hxOrder.getThirdPartyFlowNum());  
    	//模拟代码结束
    	
        return null;
	}
	
	private String queryApplyResult(String channelFlow,Integer clientType,Integer loanId) throws Exception{
    	HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
    	ReqBusinessParams businessParams = new ReqBusinessParams();
    	businessParams.setOLDREQSEQNO(channelFlow);
    	String message = MessageUtil.getMessageForQuery(businessParams, HXTradeType.HK.getQueryCode());
    	String result = HXHttpUtil.doPost(config.formSubmitUrl(), message);
    	return result;
	}

	private BigDecimal getTotalRepayAmount(List<T6252> list)throws Exception{
		BigDecimal totalAmount=BigDecimal.ZERO;
		for(T6252 e:list){
			totalAmount=totalAmount.add(e.F07);
		}
		return totalAmount;
	}

}
