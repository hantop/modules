package com.fenlibao.service.pms.da.finance.accountManagement.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.dao.pms.da.cs.guarantee.GuaranteeMapper;
import com.fenlibao.dao.pms.da.finance.accountManagement.AccountManagementMapper;
import com.fenlibao.model.pms.da.cs.account.Transaction;
import com.fenlibao.model.pms.da.cs.form.RechargeForm;
import com.fenlibao.model.pms.da.finance.T6101Extend;
import com.fenlibao.model.pms.da.finance.enums.PlatformRole;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.order.SysRechargeManageDao;
import com.fenlibao.p2p.dao.xinwang.pay.XWRechargeDao;
import com.fenlibao.p2p.model.xinwang.config.XinWangConfig;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.order.RechargeOrderEntity;
import com.fenlibao.p2p.model.xinwang.entity.order.SystemOrder;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.PayConpany;
import com.fenlibao.p2p.model.xinwang.enums.PaymentMode;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.account.XWUserInfoService;
import com.fenlibao.p2p.service.xinwang.enterprise.XWEnpBindcardService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import com.fenlibao.service.pms.da.finance.accountManagement.AccountManagementFinanceService;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2017/6/26.
 */

@Service
public class AccountManagementFinanceServiceImpl implements AccountManagementFinanceService {

    protected Logger logger = LogManager.getLogger(AccountManagementFinanceService.class);

    @Autowired
    private AccountManagementMapper accountManagementMapper;

    @Autowired
    private XWUserInfoService userInfoService;

    @Autowired
    private XWRechargeDao rechargeDao;

    @Autowired
    private SysRechargeManageDao sysRechargeManageDao;

    @Autowired
    XWRequestDao requestDao;

    @Autowired
    SysOrderManageDao orderManageDao;

    @Autowired
    private GuaranteeMapper guaranteeMapper;

    @Autowired
    XWEnpBindcardService xwEnpBindcardService;

    public static XinWangConfig CONFIG = ConfigFactory.create(XinWangConfig.class);
    public List<T6101Extend> findList(){
        return accountManagementMapper.findList();
    }

    public List<Transaction> findTradeHistory(int userId, String accountType,Date startDate,Date endDate,RowBounds bounds){
        // 资金账户id
        int flowId = accountManagementMapper.getFlowId(userId, accountType);
        // 交易记录
        List<Transaction> transactionList = accountManagementMapper.findTradeHistory(flowId,startDate,endDate,bounds);
        List<Map<String, Object>> tradeTypeMaps = new ArrayList<>();
        if(transactionList.size() > 0){
            tradeTypeMaps = accountManagementMapper.getTradeTypeMap();
            for (Transaction trancation: transactionList) {
                Map<String, Object> tradeTypeTemp = getTradeTypeValue(trancation.getTradeTypeCode(), tradeTypeMaps);
                if(tradeTypeTemp != null){
                    if(tradeTypeTemp.get("tradeTypeCode").toString().equals("7004") || tradeTypeTemp.get("tradeTypeCode").toString().equals("7005")){
                        trancation.setTradeTypeName("其他");
                    }else{
                        trancation.setTradeTypeName(tradeTypeTemp.get("tradeTypeName").toString());
                    }
                }
            }
        }
        return transactionList;
    }

    @Override
    public Map<String, Object> doRecharge(int userId, PlatformRole platformRole, String uri, int orderId, String requestNo, PaymentMode paymode, String bankcode) throws XWTradeException {
        String platformUserNo = platformRole.getNo();
        Date requestTime = new Date();
        XinwangUserInfo userInfo = userInfoService.queryUserInfo(platformUserNo);
        RechargeOrderEntity rechargeOrder = sysRechargeManageDao.getOrder(orderId);
        SystemOrder order = orderManageDao.get(orderId,false);
        if (rechargeOrder == null) {
            logger.warn("充值订单不存在，userId=[{}],orderId=[{}]", userId, orderId);
            throw new XWTradeException(XWResponseCode.COMMON_RECORD_NOT_EXIST);
        }
        if (!XWOrderStatus.DTJ.equals(order.getOrderStatus())) {
            logger.warn("充值订单状态不正确，userId=[{}],orderId=[{}]", userId, orderId);
            throw new XWTradeException(XWResponseCode.COMMON_ORDER_STATUS_WRONG);
        }
        //组装请求
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("platformUserNo", platformUserNo);
        reqData.put("requestNo", requestNo);
        reqData.put("redirectUrl", XinWangUtil.CONFIG.redirectIP()+uri);
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        reqData.put("amount", rechargeOrder.F03);
        reqData.put("expectPayCompany", PayConpany.parse(CONFIG.payCompanyChannel()));//TODO 修改支付公司
        reqData.put("rechargeWay", paymode.getCode());
        if (StringUtils.isEmpty(bankcode)) {
            reqData.put("bankcode", userInfo.getBankcode());
        }else{
            reqData.put("bankcode", bankcode);
        }
        DateTime dateTime = new DateTime();
        reqData.put("expired", dateTime.plusMinutes(30).toString("yyyyMMddHHmmss"));
        //保存请求参数
        XWResponseMessage requestParams=new XWResponseMessage();
        requestParams.setRequestNo(requestNo);
        requestParams.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestParams);
        Map<String,Object> sendData = null;
        try {
            sendData = XinWangUtil.gatewayRequest(XinwangInterfaceName.RECHARGE.getCode(),reqData);
        } catch (Exception e) {
            logger.warn("用户新网存管充值组装参数异常：userId:[{}],requestNo:[{}]", userId, requestNo);
            throw new XWTradeException(XWResponseCode.XW_ASSEMBLE_REQUEST_PARAM_WRONG);
        }
        //组装参数没有报错才会创建新网订单
        XWRequest request = new XWRequest();
        request.setPlatformUserNo(platformUserNo);
        request.setInterfaceName(XinwangInterfaceName.RECHARGE.getCode());
        request.setOrderId(orderId);
        request.setRequestNo(requestNo);
        request.setState(XWRequestState.DTJ);
        request.setRequestTime(requestTime);
        requestDao.createRequest(request);
        rechargeDao.insertRechargeRequest(reqData);
        return sendData;
    }

    @Override
    public Map<String,Object> bindBank(RechargeForm bindInfo,String platformRole, String redirectUrl){
        Map<String, Object> data;
        try {
            data = xwEnpBindcardService.getBindcardInfo(bindInfo.getUserId(), UserRole.parse(platformRole), bindInfo.getBankCode(), bindInfo.getBankcardNo(), redirectUrl);
            return data;
        }catch (Exception e ){
            logger.warn("用户新网存管企业绑卡参数异常：enpId:[{}],requestNo:[{}]");
            throw new XWTradeException(XWResponseCode.XW_ASSEMBLE_REQUEST_PARAM_WRONG);
        }

    }

    private Map<String, Object> getTradeTypeValue(String tradeType, List<Map<String, Object>> tradeTypeMap){
        for (Map<String, Object> tradeTypeTemp : tradeTypeMap) {
            if (tradeType.equals(tradeTypeTemp.get("tradeTypeCode").toString())) {
                return tradeTypeTemp;
            }
        }
        return null;
    }
}
