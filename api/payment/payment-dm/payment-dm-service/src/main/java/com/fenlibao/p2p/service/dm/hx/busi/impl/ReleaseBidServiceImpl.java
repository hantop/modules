package com.fenlibao.p2p.service.dm.hx.busi.impl;

import com.fenlibao.p2p.dao.dm.hx.HXUserDao;
import com.fenlibao.p2p.dao.dm.hx.HuaXingDao;
import com.fenlibao.p2p.dao.trade.bid.BidManageDao;
import com.fenlibao.p2p.dao.trade.common.TradeCommonDao;
import com.fenlibao.p2p.dao.user.UserDao;
import com.fenlibao.p2p.model.dm.config.HXConfig;
import com.fenlibao.p2p.model.dm.entity.HXOrder;
import com.fenlibao.p2p.model.dm.enums.APPType;
import com.fenlibao.p2p.model.dm.enums.HXTradeType;
import com.fenlibao.p2p.model.dm.enums.OrderStatus;
import com.fenlibao.p2p.model.dm.message.ResponseDocument;
import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.model.dm.xmlpara.request.Borrower;
import com.fenlibao.p2p.model.dm.xmlpara.request.ReqBusinessParams;
import com.fenlibao.p2p.model.dm.xmlpara.response.RespBusinessParams;
import com.fenlibao.p2p.model.trade.entity.T6230;
import com.fenlibao.p2p.model.trade.entity.T6231;
import com.fenlibao.p2p.model.trade.enums.T6230_F20;
import com.fenlibao.p2p.model.trade.enums.TradeResponseCode;
import com.fenlibao.p2p.model.trade.exception.TradeException;
import com.fenlibao.p2p.model.user.entity.UserInfoEntity;
import com.fenlibao.p2p.service.dm.hx.HuaXingService;
import com.fenlibao.p2p.service.dm.hx.busi.ReleaseBidService;
import com.fenlibao.p2p.service.dm.hx.impl.HXOrderProcessImpl;
import com.fenlibao.p2p.service.trade.bid.BidManageService;
import com.fenlibao.p2p.service.trade.common.TradeCommonService;
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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReleaseBidServiceImpl extends HXOrderProcessImpl implements ReleaseBidService{

	protected final Logger logger = LogManager.getLogger(this.getClass());
	
	@Resource
	BidManageDao bidManageDao;
	
    @Resource
    private HuaXingService huaXingService;
    
    @Resource
    private BidManageService bidManageService;
    
    @Resource
    private TradeCommonService tradeCommonService;
    
    @Resource
    private UserDao userDao;
    
    @Resource
    private TradeCommonDao tradeCommonDao;
    
    @Resource
    private HXUserDao hxUserDao;
    
    @Resource
    private HuaXingDao hxDao;
	
    /**
     * 发标通知
     * 没有报错算成功
     * @param loanId 借款id
     * @param pmsUserId 后台用户id
     * @param zrFlag 是否为债券转让标的    0 否，1 是
     * @param refLoanNo 债券转让原标ID 
     * @param OLDREQSEQ 债券转让原投标第三方交易流水号
     * @throws Exception
     * @throws TradeException
     * @return Map code CG成功/SB失败, msg 文字信息
     */
	@Override
	public Map<String,String> releaseBid(int loanId,Integer pmsUserId,int zrFlag,String refLoanNo,String OLDREQSEQ) throws Exception {
        //防止重复操作
        Map<String,Object> params=new HashMap<>(3);
        params.put("businessId", loanId);
        params.put("status", OrderStatus.DQR.getCode());
        params.put("typeCode", HXTradeType.FBTZ.getBusiCode());
        HXOrder oldOrder= huaXingDao.getOrder(params);
        if(oldOrder!=null){
        	throw new TradeException(TradeResponseCode.TRADE_OPERATION_REPEAT);
        }
		
		int clientType=APPType.PC.getCode();
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
		// 预发布后，如果标已经显示就不能再按发布了
		if (t6230.F35 != null) {
			if (new Date().getTime() - t6230.F35.getTime() >= 0) {
				throw new TradeException(TradeResponseCode.TRADE_DISPLAYED_BID_NOT_RELEASE);
			}
		}
		if(t6230.F20!=T6230_F20.DFB&&t6230.F20!=T6230_F20.YFB){
			throw new TradeException(TradeResponseCode.TRADE_RELEASE_BID_CONDITIONS_NOT_SATISFIED);
		}
		// 创建订单
		HXOrder order = new HXOrder().fromPms(pmsUserId, HXTradeType.FBTZ.getBusiCode(), t6230.F01);
		order.setUserId(t6230.F02);
		huaXingService.createOrder(clientType, order);
		String tradeCode= HXTradeType.getTradeCode(HXTradeType.FBTZ, clientType);
		//生成一个流水号
		String channelFlow=HXUtil.getChannelFlow(tradeCode,order.getId());
		//提交订单
        submit(order.getId(),channelFlow, null);
		//向华兴发发标通知
        String result=releaseBidNotice(t6230,clientType,channelFlow,zrFlag,refLoanNo,OLDREQSEQ);
        ResponseDocument resp= MessageUtil.getXMLDocument(ResponseDocument.class,result);
        //保存返回的报文
        huaXingService.saveMessage(resp.getBody().getTRANSCODE(), channelFlow, resp.getHeader().getChannelFlow(), result);
        //完成华兴订单
    	String errorCode=resp.getHeader().getErrorCode();
    	boolean hxOrderSuccess="0".equals(errorCode)||"EAS020420026".equals(errorCode);
    	Map<String,String> returnMap=new HashMap<>();
        if(hxOrderSuccess){
            if("0".equals(errorCode)){
                //对返回报文进行处理
                String enXmlParam = resp.getBody().getXMLPARA();
                RespBusinessParams busiParams = MessageUtil.getXmlParam(enXmlParam, RespBusinessParams.class);
                busiParams.setOLDREQSEQNO(channelFlow);//因为没有返回请求中的流水号，所以设置
                process(busiParams, busiParams.getRESJNLNO());
                logger.info("标"+loanId+"发标成功");
            	returnMap.put("code", "CG");
            	returnMap.put("msg", "发标成功");
            }
            else{
            	RespBusinessParams busiParams=new RespBusinessParams();
            	busiParams.setOLDREQSEQNO(channelFlow);//因为没有返回请求中的流水号，所以设置
            	process(busiParams, null);
            	logger.info("标"+loanId+"发标成功");
            	returnMap.put("code", "CG");
            	returnMap.put("msg", "发标成功");
            }
        }
        else{
        	order.setStatus(OrderStatus.SB.getCode());
        	hxDao.completeOrder(order);
        	logger.info("标"+loanId+"发标通知失败："+resp.getHeader().getErrorMsg());
        	returnMap.put("code", "SB");
        	returnMap.put("msg", "发标通知失败："+resp.getHeader().getErrorMsg());
        }
        return returnMap;
	}
    
    /**
     * 
     * @param t6230
     * @param clientType 客户端类型
     * @param channelFlow 流水号
     * @param zrFlag  是否为债券转让标的    0 否，1 是
     * @param refLoanNo 债券转让原标ID
     * @param OLDREQSEQ 债券转让原投标第三方交易流水号
     * @return
     * @throws Exception 
     */
    private String releaseBidNotice(T6230 t6230,int clientType,String channelFlow,int zrFlag,String refLoanNo,String OLDREQSEQ) throws Exception{
    	HXConfig config = ConfigCache.getOrCreate(HXConfig.class);
    	UserInfoEntity userInfoEntity=userDao.get(t6230.F02, null);
    	T6231 t6231=bidManageDao.getBidExInfoById(t6230.F01);
    	HXAccountInfo hxAccountInfo = hxUserDao.getAccountInfo(t6230.F02);
		if(hxAccountInfo==null){
			throw new TradeException(TradeResponseCode.FUND_ACCOUNT_NOT_EXIST);
		}
    	ReqBusinessParams businessParams = new ReqBusinessParams();
    	businessParams.setLOANNO(t6230.F25);  //借款编号
    	businessParams.setINVESTID(t6230.F25);  //标的编号
    	businessParams.setINVESTOBJNAME(t6230.F03);  //标名称
    	businessParams.setINVESTOBJINFO(t6231.F09);//标简介
    	businessParams.setMININVESTAMT("");  //最低投标金额
    	businessParams.setMAXINVESTAMT("");  //最高投资金额
    	businessParams.setINVESTOBJAMT(t6230.F05.toString()); //借款金额
    	businessParams.setINVESTBEGINDATE(HXUtil.getSimpleDate(new Date()));//发标日期
    	
        Timestamp currentTimestamp=tradeCommonDao.getCurrentTimestamp();
        Calendar raiseDeadline = Calendar.getInstance();
        raiseDeadline.setTime(currentTimestamp);
        raiseDeadline.add(Calendar.DAY_OF_YEAR,t6230.F08);
        raiseDeadline.set(Calendar.HOUR_OF_DAY, 24);
        raiseDeadline.set(Calendar.MINUTE, 0);
        raiseDeadline.set(Calendar.SECOND, 0);
    	
    	businessParams.setINVESTENDDATE(HXUtil.getSimpleDate(new Date(raiseDeadline.getTimeInMillis())));//筹款结束日期
    	businessParams.setYEARRATE(t6230.F06.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString());//年利率
    	int loanDays=0;
    	if(t6230.F09!=0){
    		loanDays=t6230.F09*30;//由于在放款后才知道开始计息日期，才能按自然月算出实际借款天数，所以这里统一乘以30天并不准确
    	}
    	else{
    		loanDays=t6230.F32;
    	}
    	businessParams.setINVESTRANGE(""+loanDays);//期限（天）
    	businessParams.setRATESTYPE("");//当日计息
    	businessParams.setREPAYSTYPE(t6230.F10.getChineseName()); //还款方式
    	businessParams.setINVESTOBJSTATE("0");
    	businessParams.setBWTOTALNUM("1");
    	businessParams.setREMARK("");//备注
    	businessParams.setZRFLAG(""+zrFlag);//是否为债券转让标的    0 否，1 是
    	businessParams.setREFLOANNO(refLoanNo==null?"":refLoanNo);//债券转让原标ID
    	businessParams.setOLDREQSEQ(OLDREQSEQ==null?"":OLDREQSEQ);//债券转让原投标第三方交易流水号
    	Borrower borrower=new Borrower();
    	borrower.setBWACNAME(userInfoEntity.getFullName()); //借款人姓名
    	borrower.setBWIDTYPE("1010");
    	borrower.setBWIDNO(StringHelper.decode(userInfoEntity.getIdCardEncrypt()));//借款人身份证
    	borrower.setBWACNO(StringHelper.decode(hxAccountInfo.getAcNo()));//借款人e账号
    	borrower.setBWACBANKID("");  //借款人账号所属行id
    	borrower.setBWACBANKNAME("");  //借款人账号所属行名称
    	borrower.setBWAMT(t6230.F05.toString()); //借款人借款金额
    	borrower.setMORTGAGEID(""); //借款人抵押品编号
    	borrower.setMORTGAGEINFO(""); //借款人抵押品简单描述
    	borrower.setCHECKDATE(""); //借款人审批通过日期
    	borrower.setREMARK("");//备注
    	businessParams.setBWLIST(borrower);
    	String message = MessageUtil.getMessageByBusi(businessParams, HXTradeType.getTradeCode(HXTradeType.FBTZ, clientType), channelFlow, clientType);
        String result = HXHttpUtil.doPost(config.formSubmitUrl(), message );
        return result;
    }
    
    @Override
    protected void doConfirm(RespBusinessParams busiParams, HXOrder order) throws Exception {
    	T6230 t6230=bidManageDao.getBidById(order.getBusinessId());
    	bidManageService.releaseBid(t6230.F01);
    	UserInfoEntity userInfoEntity=userDao.get(t6230.F02, null);
    	String template=tradeCommonDao.getSystemVariable("LETTER.LOAN_RELEASE_SUCCESS");
    	template=template.replace("${userName}", userInfoEntity.getUsername());
    	template=template.replace("${title}", t6230.F03);
    	tradeCommonService.sendLetter(t6230.F02, "借款已发布", template);
    }

	@Override
	public void preReleaseBid(int loanId, Integer pmsUserId, int zrFlag, String refLoanNo, String OLDREQSEQ,Timestamp displayTime,Timestamp releaseTime)
			throws Exception {
        //防止重复操作
        Map<String,Object> params=new HashMap<>(3);
        params.put("businessId", loanId);
        params.put("status", OrderStatus.DQR.getCode());
        params.put("typeCode", HXTradeType.YFB.getBusiCode());
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
		//修改标为预发布状态
		bidManageService.preReleaseBid(loanId, displayTime, releaseTime);
		//如果是第一次设置预发布，就创建订单
		if(t6230.F35==null){
			int clientType=APPType.PC.getCode();
			// 创建订单
			HXOrder order = new HXOrder().fromPms(pmsUserId, HXTradeType.YFB.getBusiCode(), t6230.F01);
			order.setUserId(t6230.F02);
			huaXingService.createOrder(clientType, order);
			String tradeCode= HXTradeType.getTradeCode(HXTradeType.YFB, clientType);
			//生成一个流水号
			String channelFlow=HXUtil.getChannelFlow(tradeCode,order.getId());
			//提交订单
	        submit(order.getId(),channelFlow, null);
		}
	}

	@Override
	public void releaseBid(HXOrder order) throws Exception {
		if(order.getTypeCode()!=HXTradeType.YFB.getBusiCode()){
			throw new TradeException(TradeResponseCode.ORDER_TRADE_TYPE_WRONG);
		}
		T6230 t6230=bidManageDao.getBidById(order.getBusinessId());
		if(t6230.F20==T6230_F20.YFB){
			if(t6230.F22.compareTo(tradeCommonDao.getCurrentTimestamp())<=0){
				RespBusinessParams busiParams=new RespBusinessParams();
				busiParams.setOLDREQSEQNO(order.getFlowNum());
				process(busiParams, null);
			}
		}
		else{
			order.setStatus(OrderStatus.SB.getCode());
			huaXingDao.completeOrder(order);
		}
	}

	@Override
	public void reProcess(HXOrder order) throws Exception {
		if(order.getTypeCode()!=HXTradeType.FBTZ.getBusiCode()){
			throw new TradeException(TradeResponseCode.ORDER_TRADE_TYPE_WRONG);
		}
    	RespBusinessParams busiParams=new RespBusinessParams();
    	busiParams.setOLDREQSEQNO(order.getFlowNum());
    	process(busiParams, order.getThirdPartyFlowNum());
	}
	
}
