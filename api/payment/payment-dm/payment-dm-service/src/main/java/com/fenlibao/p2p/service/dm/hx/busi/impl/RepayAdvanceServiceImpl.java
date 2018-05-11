package com.fenlibao.p2p.service.dm.hx.busi.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fenlibao.p2p.dao.dm.hx.HXUserDao;
import com.fenlibao.p2p.dao.dm.hx.HuaXingDao;
import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
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
import com.fenlibao.p2p.model.trade.entity.T6252;
import com.fenlibao.p2p.model.trade.entity.TradeFeeCode;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.service.dm.hx.HuaXingService;
import com.fenlibao.p2p.service.dm.hx.busi.RepayAdvanceService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.util.dm.HXUtil;
import com.fenlibao.p2p.util.dm.MessageUtil;
import com.fenlibao.p2p.util.dm.http.HXHttpUtil;

@Service
public class RepayAdvanceServiceImpl extends HXOrderProcessImpl implements RepayAdvanceService{

	protected final Logger logger = LogManager.getLogger(this.getClass());
	
	@Resource
	HXUserDao hxUserDao;
	
	@Resource
	BidManageDao bidManageDao;
	
    @Resource
    private HuaXingService huaXingService;
    
    @Resource
    private HuaXingDao hxDao;
	
	/**
	 * 还款垫付
	 * @param loanId 标id
	 * @param feeAmt 扣借款人的平台手续费
	 * @param remark 备注
	 * @param pmsUserId 后台用户id
	 */
	@Override
	public Map<String,String> repayAdvance(int loanId, BigDecimal feeAmt, String remark,Integer pmsUserId) throws Exception{
        //防止重复操作
        Map<String,Object> params=new HashMap<>(3);
        params.put("businessId", loanId);
        params.put("status", OrderStatus.DQR.getCode());
        params.put("typeCode", HXTradeType.HKDF.getBusiCode());
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
		// 创建订单
		HXOrder order = new HXOrder().fromPms(pmsUserId, HXTradeType.HKDF.getBusiCode(), t6230.F01);
		order.setUserId(t6230.F02);
		int clientType=APPType.PC.getCode();
		huaXingService.createOrder(clientType, order);
		String tradeCode= HXTradeType.getTradeCode(HXTradeType.HKDF, clientType);
		//生成一个流水号
		String channelFlow=HXUtil.getChannelFlow(tradeCode,order.getId());
		//提交订单
        submit(order.getId(),channelFlow, null);
        String result=sendRepayAdvanceApply(t6230,clientType,channelFlow,feeAmt,remark);
        ResponseDocument resp= MessageUtil.getXMLDocument(ResponseDocument.class,result);
        //保存返回的报文
        huaXingService.saveMessage(resp.getBody().getTRANSCODE(), channelFlow, resp.getHeader().getChannelFlow(), result);
        //完成华兴订单
    	String errorCode=resp.getHeader().getErrorCode();
    	Map<String,String> returnMap=new HashMap<>();
        if("0".equals(errorCode)){
			// 对返回报文进行处理
			String enXmlParam = resp.getBody().getXMLPARA();
			RespBusinessParams busiParams = MessageUtil.getXmlParam(enXmlParam, RespBusinessParams.class);
			busiParams.setOLDREQSEQNO(channelFlow);// 因为没有返回请求中的流水号，所以设置
			process(busiParams, busiParams.getRESJNLNO());
			logger.info("标"+loanId+"垫付设置成功");
        	returnMap.put("code", "CG");
        	returnMap.put("msg", "垫付设置成功");
        }
        else{
        	order.setStatus(OrderStatus.SB.getCode());
        	hxDao.completeOrder(order);
        	logger.info("标"+loanId+"垫付申请失败："+resp.getHeader().getErrorMsg());
        	returnMap.put("code", "SB");
        	returnMap.put("msg", "垫付申请失败："+resp.getHeader().getErrorMsg());
        }
		return returnMap;
	}
	
    private String sendRepayAdvanceApply(T6230 t6230,int clientType,String channelFlow,BigDecimal feeAmt, String remark) throws Exception{
    	HXAccountInfo hxAccountInfo = hxUserDao.getAccountInfo(t6230.F02);
    	if(hxAccountInfo==null){
    		throw new TradeException(TradeResponseCode.FUND_ACCOUNT_NOT_EXIST);
    	}
    	HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
    	ReqBusinessParams businessParams = new ReqBusinessParams();
    	businessParams.setLOANNO(t6230.F25);  //借款编号
    	businessParams.setBWACNAME(hxAccountInfo.getAcName());
    	businessParams.setACCNO(hxAccountInfo.getAcNo());
    	//TODO 这个amount不知道是怎么定义的
    	businessParams.setAMOUNT(getRepayAmount(t6230.F01));
    	businessParams.setREMARK(remark==null?"":remark);
    	businessParams.setFEEAMT(feeAmt.setScale(2,RoundingMode.HALF_UP).toString());
    	String message = MessageUtil.getMessageByBusi(businessParams, HXTradeType.getTradeCode(HXTradeType.HKDF, clientType), channelFlow, clientType);
        String result = HXHttpUtil.doPost(config.formSubmitUrl(), message );
        return result;
    }
    
	public String getRepayAmount(int loanId){
		Map<String,Object> p1=new HashMap<>(3);
		p1.put("F02", loanId);
		List<Integer> F05List=new ArrayList<>();
		F05List.add(TradeFeeCode.TZ_BJ);
		F05List.add(TradeFeeCode.TZ_LX);
		//F05List.add(TradeFeeCode.TZ_FX);
		//F05List.add(TradeFeeCode.TZ_YQ_SXF);
		p1.put("F05List", F05List);
		p1.put("F09", "WH");
		List<T6252> t6252List=bidManageDao.getRepayPlan(p1);
		BigDecimal amount=BigDecimal.ZERO;
		for(T6252 e:t6252List){
			amount.add(e.F07);
		}
		return amount.setScale(2, RoundingMode.HALF_UP).toString();
	}

	@Override
	public void reProcess(HXOrder order) throws Exception {
		if(order.getTypeCode()!=HXTradeType.HKDF.getBusiCode()){
			throw new TradeException(TradeResponseCode.ORDER_TRADE_TYPE_WRONG);
		}
		RespBusinessParams busiParams = new RespBusinessParams();
		busiParams.setOLDREQSEQNO(order.getFlowNum());
		process(busiParams, order.getThirdPartyFlowNum());
	}

}
