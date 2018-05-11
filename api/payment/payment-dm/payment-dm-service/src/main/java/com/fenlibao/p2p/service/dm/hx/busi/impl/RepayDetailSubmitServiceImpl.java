package com.fenlibao.p2p.service.dm.hx.busi.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.aeonbits.owner.ConfigCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fenlibao.p2p.dao.dm.hx.HXOrderDao;
import com.fenlibao.p2p.dao.dm.hx.HXUserDao;
import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.entity.HXRepayOrder;
import com.fenlibao.p2p.model.dm.enums.APPType;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import com.fenlibao.p2p.model.dm.message.ResponseDocument;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.model.dm.xmlpara.request.Repay;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.entity.T6252;
import com.fenlibao.p2p.model.trade.entity.TradeFeeCode;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.service.dm.hx.HuaXingService;
import com.fenlibao.p2p.service.dm.hx.busi.RepayDetailSubmitService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.service.trade.bid.OtherTaskOfPrepay;
import com.fenlibao.p2p.service.trade.bid.OtherTaskOfRepay;
import com.fenlibao.p2p.service.trade.order.PrepayOrderService;
import com.fenlibao.p2p.service.trade.order.RepayOrderService;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import com.fenlibao.p2p.util.dm.http.HXHttpUtil;

@Service
public class RepayDetailSubmitServiceImpl extends HXOrderProcessImpl implements RepayDetailSubmitService{

	@Resource
	BidManageDao bidManageDao;
	
    @Resource
    BidManageService bidManageService;
    
    @Resource
    RepayOrderService repayOrderService;
    
    @Resource
    PrepayOrderService prepayOrderService;
    
	@Resource
	HXUserDao hxUserDao;
	
	@Resource
	OtherTaskOfRepay otherTaskOfRepay;
	
	@Resource
	OtherTaskOfPrepay otherTaskOfPrepay;
	
	@Resource
	HuaXingService huaXingService;
	
	@Transactional
	@Override
	public void repayDetailSubmit(HXOrder hxOrder) throws Exception {
		int clientType=APPType.PC.getCode();
		int loanId=hxOrder.getBusinessId();
		T6230 t6230=bidManageDao.getBidById(loanId);
		
		// 创建订单
		HXOrder mxOrder = new HXOrder().fromPms(null, HXTradeType.HKMX.getBusiCode(), loanId);
		mxOrder.setParentFlowNum(hxOrder.getFlowNum());
		huaXingService.createOrder(clientType, mxOrder);
		//获取华兴流标操作编码
		String tradeCode= HXTradeType.getTradeCode(HXTradeType.HKMX, clientType);
		//生成一个流水号
		String channelFlow=HXUtil.getChannelFlow(tradeCode,mxOrder.getId());
		//借款人账户
		HXAccountInfo accountInfo=hxUserDao.getAccountInfo(t6230.F02);
		List<T6252> listToRepay=null;
		//平台订单
		HXRepayOrder repayOrder=huaXingDao.getRepayOrderById(hxOrder.getId());
		int term=repayOrder.getTerm();
		int dfflag=repayOrder.getDfflag();
		if(HXTradeType.HK.getBusiCode()==hxOrder.getTypeCode()){
			repayOrderService.updateOrder(T6501_F03.DQR,hxOrder.getFlowNum());
			listToRepay=bidManageService.getRepayList(loanId, term);
		}
		else if(HXTradeType.TQHK.getBusiCode()==hxOrder.getTypeCode()){
			prepayOrderService.updateOrder(T6501_F03.DQR,hxOrder.getFlowNum());
	        Map<String,Object> params=new HashMap<>(3);
	        params.put("businessId", loanId);
	        params.put("status", OrderStatus.SB.getCode());
	        params.put("typeCode", HXTradeType.HKMX.getBusiCode());
	        HXOrder sbOrder= huaXingDao.getOrder(params);
	        if(sbOrder!=null){
	        	listToRepay=bidManageService.getRepayList(loanId, term);
	        }
	        else{
	        	listToRepay=bidManageService.getPrepayList(loanId, term);
	        }
		}

		//提交明细
		String result=submitDetail(dfflag,hxOrder.getFlowNum(),t6230,term,accountInfo,clientType,channelFlow,listToRepay);
        ResponseDocument resp= MessageUtil.getXMLDocument(ResponseDocument.class,result);
        //保存返回的报文
        huaXingService.saveMessage(resp.getBody().getTRANSCODE(), channelFlow, resp.getHeader().getChannelFlow(), result);
        //完成华兴订单
    	String errorCode=resp.getHeader().getErrorCode();
    	boolean hxOrderSuccess="0".equals(errorCode);
//        if(hxOrderSuccess){
//    		//提交华兴订单
//    		submit(mxOrder.getId(),channelFlow, null);
//        	logger.info("提交还款明细成功受理");
//        }
//        else{
//        	mxOrder.setStatus(OrderStatus.SB.getCode());
//        	huaXingDao.completeOrder(mxOrder);
//        	logger.info("提交还款明细失败："+resp.getHeader().getErrorMsg());
//        }
    	
    	//模拟代码开始
		submit(mxOrder.getId(),channelFlow, null);
    	logger.info("提交还款明细成功受理");
	}

	@Override
	public RespBusinessParams queryOrder(HXOrder mxOrder) throws Exception {
		if(mxOrder.getTypeCode()!=HXTradeType.HKMX.getBusiCode()){
			throw new TradeException(TradeResponseCode.ORDER_TRADE_TYPE_WRONG);
		}
		T6230 t6230=bidManageDao.getBidById(mxOrder.getBusinessId());
		String result=querySubmitResult(mxOrder.getFlowNum(),t6230.F25);
		ResponseDocument resp= MessageUtil.getXMLDocument(ResponseDocument.class,result);
        //保存返回的报文
        huaXingService.saveMessage(resp.getBody().getTRANSCODE(), mxOrder.getFlowNum(), resp.getHeader().getChannelFlow(), result);
        String errorCode=resp.getHeader().getErrorCode();
        RespBusinessParams respParam=null;
//        if("0".equals(errorCode)){
//            //对返回报文进行处理
//            String enXmlParam = resp.getBody().getXMLPARA();
//            respParam = MessageUtil.getXmlParam(enXmlParam, RespBusinessParams.class);
//            String returnStatus=respParam.getRETURN_STATUS();
//            HXOrder parentOrder=huaXingService.getOrder(mxOrder.getParentFlowNum());   
//            if("S".equals(returnStatus)){
//            	//执行还款
//        		HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
//        		if(HXTradeType.HK.getBusiCode()==parentOrder.getTypeCode()){
//        			bidManageService.repay(mxOrder.getBusinessId(), mxOrder.getParentFlowNum(),config.platformHxWlzh());
//        			//TODO 加息发送完再发信息
//        			otherTaskOfRepay.sendLetterAndMsg(mxOrder.getBusinessId(),mxOrder.getParentFlowNum()); 
//        		}
//        		else if(HXTradeType.TQHK.getBusiCode()==parentOrder.getTypeCode()){
//        			bidManageService.prepay(mxOrder.getBusinessId(), mxOrder.getParentFlowNum(),config.platformHxWlzh());
//        			//TODO 加息发送完再发信息
//        			otherTaskOfPrepay.sendLetterAndMsg(mxOrder.getBusinessId(),mxOrder.getParentFlowNum()); 
//        		}
//            	//结束华兴订单
//            	mxOrder.setStatus(OrderStatus.CG.getCode());
//            	huaXingDao.completeOrder(mxOrder);
//            	parentOrder.setStatus(OrderStatus.CG.getCode());
//            	huaXingDao.completeOrder(parentOrder);
//            	//结束平台订单
//            	if(HXTradeType.HK.getBusiCode()==parentOrder.getTypeCode()){
//            	    repayOrderService.updateOrder(T6501_F03.CG, mxOrder.getParentFlowNum());
//            	}
//            	else if(HXTradeType.TQHK.getBusiCode()==parentOrder.getTypeCode()){
//            		prepayOrderService.updateOrder(T6501_F03.CG, mxOrder.getParentFlowNum());
//            	}
//                logger.info("标"+mxOrder.getBusinessId()+"还款明细提交成功");
//            }
//            else if("F".equals(returnStatus)){
//            	//结束华兴订单
//            	mxOrder.setStatus(OrderStatus.SB.getCode());
//            	huaXingDao.completeOrder(mxOrder);
//            	//将还款计划还原，或者查找是有明细失败订单后跳过对还款计划的修改，就可以重新提交明细。
//            	logger.info("标"+mxOrder.getBusinessId()+"还款明细提交失败");
//            }
//        }
//        else{
//        	logger.info("标"+mxOrder.getBusinessId()+"查询还款明细提交结果失败："+resp.getHeader().getErrorMsg());
//        }
        
        //模拟代码开始
    	//执行还款
        HXOrder parentOrder=huaXingService.getOrder(mxOrder.getParentFlowNum()); 
		HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
		if(HXTradeType.HK.getBusiCode()==parentOrder.getTypeCode()){
			bidManageService.repay(mxOrder.getBusinessId(), mxOrder.getParentFlowNum(),config.platformHxWlzh());
			//TODO 加息发送完再发信息
			otherTaskOfRepay.sendLetterAndMsg(mxOrder.getBusinessId(),mxOrder.getParentFlowNum()); 
		}
		else if(HXTradeType.TQHK.getBusiCode()==parentOrder.getTypeCode()){
			bidManageService.prepay(mxOrder.getBusinessId(), mxOrder.getParentFlowNum(),config.platformHxWlzh());
			//TODO 加息发送完再发信息
			otherTaskOfPrepay.sendLetterAndMsg(mxOrder.getBusinessId(),mxOrder.getParentFlowNum()); 
		}
    	//结束华兴订单
    	mxOrder.setStatus(OrderStatus.CG.getCode());
    	huaXingDao.completeOrder(mxOrder);
    	parentOrder.setStatus(OrderStatus.CG.getCode());
    	huaXingDao.completeOrder(parentOrder);
    	//结束平台订单
    	if(HXTradeType.HK.getBusiCode()==parentOrder.getTypeCode()){
    	    repayOrderService.updateOrder(T6501_F03.CG, mxOrder.getParentFlowNum());
    	}
    	else if(HXTradeType.TQHK.getBusiCode()==parentOrder.getTypeCode()){
    		prepayOrderService.updateOrder(T6501_F03.CG, mxOrder.getParentFlowNum());
    	}
        logger.info("标"+mxOrder.getBusinessId()+"还款明细提交成功");
        //模拟代码结束
        
		return null;
	}
	
    private String submitDetail(int dfflag, String oldreqseqno,T6230 t6230,int term,HXAccountInfo accountInfo,int clientType,String channelFlow,List<T6252> listToRepay) throws Exception{
		List<T6252> t6252List=listToRepay;
		Map<Integer,T6252> debtBjMap=new HashMap<>();
		Map<Integer,T6252> debtLxMap=new HashMap<>();
		Map<Integer,T6252> debtFxMap=new HashMap<>();
		Map<Integer,T6252> debtSxfMap=new HashMap<>();
		Map<Integer,T6252> debtWyjMap=new HashMap<>();
		for(T6252 t6252:t6252List){
			if(t6252.F05==TradeFeeCode.TZ_BJ){
				T6252 temp=debtBjMap.get(t6252.F11);
				if(temp!=null){
					temp.F07=temp.F07.add(t6252.F07);
				}
				else{
					temp=t6252;
				}
				debtBjMap.put(t6252.F11, temp);
			}
			else if(t6252.F05==TradeFeeCode.TZ_LX){
				debtLxMap.put(t6252.F11, t6252);
			}
			else if(t6252.F05==TradeFeeCode.TZ_FX){
				debtFxMap.put(t6252.F11, t6252);
			}
			else if(t6252.F05==TradeFeeCode.TZ_YQ_SXF){
				debtSxfMap.put(t6252.F11, t6252);
			}
			else if(t6252.F05==TradeFeeCode.TZ_WYJ){
				debtWyjMap.put(t6252.F11, t6252);
			}
		}
		
		List<Repay> repayList=new ArrayList<>();
		
		int totalNum=debtLxMap.size();
		
		for (Integer key : debtLxMap.keySet()) {
			Repay repay=new Repay();
			repay.setSUBSEQNO(UUID.randomUUID().toString());
			int investorId=debtLxMap.get(key).F04;
			HXAccountInfo investorAccount= hxUserDao.getAccountInfo(investorId);
	    	if(investorAccount==null){
	    		throw new TradeException(TradeResponseCode.FUND_ACCOUNT_NOT_EXIST);
	    	}
			repay.setACNO(StringHelper.decode(investorAccount.getAcNo()));
			repay.setACNAME(investorAccount.getAcName());
			repay.setINCOMEDATE(HXUtil.getSimpleDate(debtLxMap.get(key).F08));
			BigDecimal bj=debtBjMap.get(key)!=null?debtBjMap.get(key).F07:BigDecimal.ZERO;
			bj.setScale(2, RoundingMode.HALF_UP);
			BigDecimal lx=debtLxMap.get(key)!=null?debtLxMap.get(key).F07:BigDecimal.ZERO;
			lx.setScale(2, RoundingMode.HALF_UP);
			BigDecimal fx=debtFxMap.get(key)!=null?debtFxMap.get(key).F07:BigDecimal.ZERO;
			fx.setScale(2, RoundingMode.HALF_UP);
			BigDecimal sxf=debtSxfMap.get(key)!=null?debtSxfMap.get(key).F07:BigDecimal.ZERO;
			sxf.setScale(2, RoundingMode.HALF_UP);
			BigDecimal wyj=debtWyjMap.get(key)!=null?debtWyjMap.get(key).F07:BigDecimal.ZERO;
			wyj.setScale(2, RoundingMode.HALF_UP);
			repay.setPRINCIPALAMT(bj.toString());
			repay.setINCOMEAMT(lx.add(fx).add(wyj).toString());
			repay.setFEEAMT(sxf.toString());
			repay.setAMOUNT(bj.add(lx).add(fx).add(sxf).add(wyj).toString());
			repayList.add(repay);
		}
    	
    	HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
    	ReqBusinessParams businessParams = new ReqBusinessParams();
    	businessParams.setDFFLAG(""+dfflag);
    	businessParams.setOLDREQSEQNO(oldreqseqno);
    	businessParams.setLOANNO(t6230.F25);
    	businessParams.setBWACNAME(accountInfo.getAcName());
    	businessParams.setBWACNO(StringHelper.decode(accountInfo.getAcNo()));
    	businessParams.setTOTALNUM(""+totalNum);
    	businessParams.setREPAYLIST(repayList);
    	String message = MessageUtil.getMessageByBusi(businessParams, HXTradeType.getTradeCode(HXTradeType.HKMX, clientType), channelFlow, clientType);
    	String result = HXHttpUtil.doPost(config.formSubmitUrl(), message);
    	return result;
    }
	
	private String querySubmitResult(String channelFlow,String loanCode) throws Exception{
    	HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
    	ReqBusinessParams businessParams = new ReqBusinessParams();
    	businessParams.setOLDREQSEQNO(channelFlow);
    	businessParams.setLOANNO(loanCode);
    	businessParams.setSUBSEQNO("");
    	String message = MessageUtil.getMessageForQuery(businessParams, HXTradeType.HKMX.getQueryCode());
    	String result = HXHttpUtil.doPost(config.formSubmitUrl(), message);
    	return result;
	}

}
