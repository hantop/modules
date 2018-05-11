package com.fenlibao.p2p.service.xinwang.trade.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.trade.ExceptionRepayDao;
import com.fenlibao.p2p.model.xinwang.config.XinWangConfig;
import com.fenlibao.p2p.model.xinwang.consts.SysCommonConsts;
import com.fenlibao.p2p.model.xinwang.consts.SysTradeFeeCode;
import com.fenlibao.p2p.model.xinwang.entity.account.XWFundAccount;
import com.fenlibao.p2p.model.xinwang.entity.account.XinwangAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.trade.ExceptionRepay;
import com.fenlibao.p2p.model.xinwang.entity.trade.XWExceptionRepayPO;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWBizType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;
import com.fenlibao.p2p.service.xinwang.trade.XWExceptionRepay;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.fenlibao.p2p.util.xinwang.HttpUtil.LOG;

/**
 * Created by Administrator on 2017/9/18.
 */
@Service
public class XWExceptionRepayImpl implements XWExceptionRepay {
    @Resource
    ExceptionRepayDao exceptionRepayDao;
    @Resource
    XWRequestDao requestDao;
    @Resource
    XWAccountDao accountDao;
    @Resource
    PTCommonDao commonDao;

    @Override
    public void exceptionRepay() throws Exception {
        List<ExceptionRepay> debtUsers = exceptionRepayDao.getDebtUsers();
        Date todayTime = new Date();
        String todayDateStr = DateUtil.getYYYY_MM_DD(todayTime);
        Date todayDate = DateUtil.StrToDate(todayDateStr, DateUtil.yyyy_MM_dd);
        for (ExceptionRepay debtUser : debtUsers) {
            if (debtUser.getLastRepayDate() == null || debtUser.getLastRepayDate().getTime() < todayDate.getTime()) {
                String userRole = "INVESTOR";
                if(debtUser.getUserRole() != null && !debtUser.getUserRole().isEmpty()){
                    userRole = debtUser.getUserRole();
                }
                BigDecimal amount;
                if(debtUser.getDebtAmount().compareTo(new BigDecimal(100)) >= 0){
                    amount = new BigDecimal(100);
                }else {
                    amount = debtUser.getDebtAmount();
                }

                try {
                    Map<String, Object> sendData = buildBatchs(debtUser.getUserId(), userRole, amount);
                    sendRequest(sendData, debtUser.getUserId());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendRequest(Map<String, Object> sendData, Integer userId) throws Exception{
        String batchNo=(String)sendData.get("batchNo");
        //发送请求
        String resultJson="";
        try {
            resultJson = XinWangUtil.serviceRequest(XinwangInterfaceName.ASYNC_TRANSACTION.getCode(),sendData);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info("用户：" + userId + " 扣款批次：" + batchNo + "，受理失败");
        }
        Map<String, Object> respMap = JSON.parseObject(resultJson);
        //批次受理结果
        String code = (String) respMap.get("code");
        String errorMessage = (String) respMap.get("errorMessage");
        if (("0").equals(code)&& "SUCCESS".equals((String) respMap.get("status"))) {
            XWRequest updatequestParam=new XWRequest();
            updatequestParam.setBatchNo(batchNo);
            updatequestParam.setState(XWRequestState.DQR);
            requestDao.updateRequest(updatequestParam);
            LOG.info("用户：" + userId + " 扣款批次：" + batchNo + "，成功受理");
        }else{
            //保存返回报文
            XWResponseMessage responseParams=new XWResponseMessage();
            responseParams.setBatchNo(batchNo);
            responseParams.setResponseMsg(resultJson);
            requestDao.saveResponseMessage(responseParams);
            //受理失败
            XWRequest updatequestParam=new XWRequest();
            updatequestParam.setBatchNo(batchNo);
            updatequestParam.setState(XWRequestState.SB);
            requestDao.updateRequest(updatequestParam);
            LOG.info("用户：" + userId + " 扣款批次：" + batchNo + "，受理失败："+errorMessage);
        }
    }

    @Transactional
    public Map<String, Object> buildBatchs(Integer userId, String userRole, BigDecimal amount) throws Exception {
        Date requestTime = new Date();
        String platformUserNo = userRole + userId;
        //組裝請求
        String batchNo = XinWangUtil.createRequestNo();
        List<Map<String, Object>> bizDetails = new ArrayList<>();
        //detail1是平台收费
        Map<String, Object> detail1 = new HashMap<>();
        detail1.put("bizType", XWBizType.DEDUCT.getCode());
        detail1.put("sourcePlatformUserNo", platformUserNo);
        detail1.put("targetPlatformUserNo", XinWangUtil.CONFIG.incomeAccount());
        detail1.put("amount", amount);
        List<Map<String, Object>> details = new ArrayList<>();
        details.add(detail1);

        Map<String, Object> request = new HashMap<>();
        String requestNo = XinWangUtil.createRequestNo();
        request.put("requestNo", requestNo);
        request.put("tradeType", XWTradeType.PLATFORM_SERVICE_DEDUCT.getCode());
        request.put("details", details);
        bizDetails.add(request);
        //创建新网订单
        XWRequest req = new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.ASYNC_TRANSACTION.getCode());
        req.setBatchNo(batchNo);
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DTJ);
        req.setPlatformUserNo(platformUserNo);
        req.setUserId(userId);
        requestDao.createRequest(req);
        //保存请求参数
        XWResponseMessage requestParams = new XWResponseMessage();
        requestParams.setRequestNo(requestNo);
        requestParams.setBatchNo(batchNo);
        requestParams.setRequestParams(JSON.toJSONString(request));
        requestDao.saveRequestMessage(requestParams);
        //保存放款请求编号到投资记录表
        Map<String, Object> saveRequestNoParams = new HashMap<>();
        saveRequestNoParams.put("userId", userId);
        saveRequestNoParams.put("userRole", userRole);
        saveRequestNoParams.put("exceptionRequestNo", requestNo);
        saveRequestNoParams.put("amount", amount);
        exceptionRepayDao.saveExceptionRepayRequestNo(saveRequestNoParams);

        Map<String, Object> batch = new HashMap<>();
        batch.put("batchNo", batchNo);
        batch.put("bizDetails", bizDetails);
        batch.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        return batch;
    }

    @Override
    public void handleNotify(List<Map<String, Object>> details) throws Exception {
        for(Map<String,Object> response:details){
            String requestNo=(String)response.get("asyncRequestNo");
            String status=(String)response.get("status");
            if("SUCCESS".equals(status)){
                //结束新网请求
                XWRequest requestParams=new XWRequest();
                requestParams.setRequestNo(requestNo);
                requestParams.setState(XWRequestState.CG);
                requestDao.updateRequest(requestParams);

                XWExceptionRepayPO exceptionRepayPO = exceptionRepayDao.findExceptionRepayPoByRequestNo(requestNo);
                if (exceptionRepayPO == null) {
                    LOG.error("资金异常处理失败：请求"+requestNo+"对应的流水不存在");
                    continue;
                }

                Map<String, Object> paramExceptionRepay = new HashMap<>();
                paramExceptionRepay.put("userId", exceptionRepayPO.getUserId());
                paramExceptionRepay.put("userRole", exceptionRepayPO.getUserRole());
                paramExceptionRepay.put("deductAmount", exceptionRepayPO.getDeductAmount());
                paramExceptionRepay.put("lastRepayDate", new Date());
                exceptionRepayDao.updateExceptionRepay(paramExceptionRepay);

                updateAccountInsertFlow(exceptionRepayPO.getUserId(), exceptionRepayPO.getUserRole(), exceptionRepayPO.getDeductAmount());
            } else{
                XWRequest requestParams=new XWRequest();
                requestParams.setRequestNo(requestNo);
                requestParams.setState(XWRequestState.SB);
                requestDao.updateRequest(requestParams);
            }
        }
    }

    private void updateAccountInsertFlow(Integer userId, String userRole, BigDecimal deductAmount) {
        String targetPlatformUserNo = XinWangUtil.CONFIG.incomeAccount();

        XinwangAccount xinwangAccount = accountDao.getXinwangAccount(targetPlatformUserNo);
        String platformUserRole = xinwangAccount.getUserRole().getCode();
        String platformMarkingAccount = "XW_" + platformUserRole + "_WLZH";
        String deductUserType = "XW_" + userRole + "_SDZH";

        Map<String, Object> m1 = new HashMap<>();
        m1.put("type", platformMarkingAccount);
        m1.put("amount", deductAmount);
        m1.put("userId", xinwangAccount.getUserId());
        accountDao.updateFundAccountPlus(m1);

        //平台收入账户
        XWFundAccount platformIncomeWLZH = accountDao.getFundAccount(xinwangAccount.getUserId(), SysFundAccountType.parse(platformMarkingAccount));
        //扣款用户
        XWFundAccount deductUserSDZH = accountDao.getFundAccount(userId, SysFundAccountType.parse(deductUserType));

        XWCapitalFlow wlzhFlow=new XWCapitalFlow();
        wlzhFlow.setFundAccountId(platformIncomeWLZH.getId());
        wlzhFlow.setTadeType(SysTradeFeeCode.NBZZ);
        wlzhFlow.setOtherFundAccountId(deductUserSDZH.getId());
        wlzhFlow.setCreateTime(new Date());
        wlzhFlow.setIncome(deductAmount);
        wlzhFlow.setBalance(platformIncomeWLZH.getAmount());
        wlzhFlow.setRemark("异常资金扣减");
        commonDao.insertT6102(wlzhFlow);
    }


}
