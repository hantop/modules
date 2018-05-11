package com.fenlibao.p2p.service.xinwang.project.impl;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.model.xinwang.consts.SysVariableConsts;
import com.fenlibao.p2p.model.xinwang.entity.account.PlatformAccount;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;
import com.fenlibao.p2p.model.xinwang.enums.project.ProjectType;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import com.fenlibao.p2p.service.xinwang.entrust.XWEntrustService;
import com.fenlibao.p2p.service.xinwang.project.XWEstablishProjectService;
import com.fenlibao.p2p.service.xinwang.project.XWEstablishProjectTransactionService;
import com.fenlibao.p2p.util.api.DateUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/18.
 */
@Service
public class XWEstablishProjectServiceImpl implements XWEstablishProjectService {

    protected final Logger LOG = LogManager.getLogger(this.getClass());

    @Resource
    XWProjectDao projectDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    XWAccountDao accountDao;

    @Resource
    PTCommonDao ptCommonDao;

    @Resource
    PTCommonService ptCommonService;

    @Resource
    XWEstablishProjectTransactionService establishProjectTransactionService;

    @Resource
    XWEntrustService entrustService;

    @Override
    public void establishProject(Integer loanId) throws Exception {
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(loanId);
        if(projectInfo==null){
            throw new XWTradeException(XWResponseCode.BID_NOT_EXIST);
        }
        if(projectInfo.getState()!=PTProjectState.DFB&&projectInfo.getState()!=PTProjectState.YFB){
            throw new XWTradeException(XWResponseCode.TRADE_RELEASE_BID_CONDITIONS_NOT_SATISFIED);
        }
        //如果是委托支付
        XWProjectExtraInfo projectExtraInfo=projectDao.getProjectExtraInfo(loanId);
        if(ProjectType.ENTRUST_PAY==projectInfo.getProjectType()){
            if(projectExtraInfo.getEntrustPayeeUserId()==null){
                throw new XWTradeException(XWResponseCode.XW_ENTRUST_AUTHORIZATION_FAIL.getCode(),"委托收款人id为空");
            }
        }
        // add by zeronx 2018-1-5 14:13  //按自然月时起息日没办法知道实际天数 只能取器大概值，取发布时间
        Integer projectPeroid = getProjectPeriod(projectInfo.getDayProjectPeriod(), projectInfo.getMonthProjectPeriod(), projectInfo.getEstablishTime());
        //组装请求
        String requestNo=XinWangUtil.createRequestNo();
        Date requestTime=new Date();
        Map<String,Object> reqData=new HashMap<>();
        reqData.put("requestNo", requestNo);
        reqData.put("platformUserNo", projectInfo.getBorrowerPlatformUserNo());
        reqData.put("projectNo", projectInfo.getProjectNo());
        reqData.put("projectAmount", projectInfo.getProjectAmount());
        reqData.put("projectName", projectInfo.getProjectName());
        reqData.put("projectDescription", null);//限制长度300
        reqData.put("projectType", projectInfo.getProjectType().getCode());
        reqData.put("projectPeriod", projectPeroid);//按自然月时起息日没办法知道实际天数
        reqData.put("annnualInterestRate", projectInfo.getAnnnualInterestRate());
        reqData.put("repaymentWay", projectInfo.getRepaymentWay().getCode());
        reqData.put("extend", null);//未知填什么
        reqData.put("timestamp", DateUtil.getYYYYMMDDHHMMSS(requestTime));
        //create order
        XWRequest req=new XWRequest();
        req.setInterfaceName(XinwangInterfaceName.ESTABLISH_PROJECT.getCode());
        req.setRequestNo(requestNo);
        req.setRequestTime(requestTime);
        req.setState(XWRequestState.DQR);
        req.setPlatformUserNo(projectInfo.getBorrowerPlatformUserNo());
        req.setUserId(projectInfo.getBorrowerUserId());
        requestDao.createRequest(req);
        //保存请求参数
        XWResponseMessage requestParams=new XWResponseMessage();
        requestParams.setRequestNo(requestNo);
        requestParams.setRequestParams(JSON.toJSONString(reqData));
        requestDao.saveRequestMessage(requestParams);
        //发送求
        String resultJson=XinWangUtil.serviceRequest(XinwangInterfaceName.ESTABLISH_PROJECT.getCode(),reqData);
        Map<String, Object> respMap = JSON.parseObject(resultJson);
        //save response msg
        XWResponseMessage responseParams=new XWResponseMessage();
        responseParams.setRequestNo(requestNo);
        responseParams.setResponseMsg(resultJson);
        requestDao.saveResponseMessage(responseParams);
        //处理结果
        String code = (String) respMap.get("code");
        String errorMessage = (String) respMap.get("errorMessage");
        if (("0").equals(code)&& "SUCCESS".equals((String) respMap.get("status"))) {
            establishSuccess(requestNo,loanId,projectInfo);
        }
        else{
            establishFail(requestNo,projectInfo,errorMessage);
        }

    }

    private Integer getProjectPeriod(Integer dayProjectPeriod, Integer monthProjectPeriod, Date establishTime) {
        // 如果月为空说明借款周期 为天 否则周期为月
        if (monthProjectPeriod == null || monthProjectPeriod == 0) {
            return dayProjectPeriod;
        }
        DateTime dateTime = new DateTime(establishTime);
        DateTime dateTime1 = dateTime.plusMonths(monthProjectPeriod);
        return Days.daysBetween(dateTime, dateTime1).getDays();
    }

    private void establishSuccess(String requestNo,Integer loanId,XWProjectInfo projectInfo) throws Exception{
        //如果是委托收款标，发送委托授权请求
        if(ProjectType.ENTRUST_PAY==projectInfo.getProjectType()){
            entrustService.authEntrustPay(loanId);
        }
        //平台发标
        establishProjectTransactionService.ptEstablishProject(requestNo,loanId,projectInfo);
        //给借款人发站内信
        try{
            PlatformAccount platformAccount= accountDao.getPlatformAccountInfoByPlatformUserNo(projectInfo.getBorrowerPlatformUserNo());
            String template=ptCommonDao.getSystemVariable(SysVariableConsts.PROJECT_ESTABLISH_SUCCESS_LETTER);
            template=template.replace("${userName}", platformAccount.getUserName());
            template=template.replace("${title}", projectInfo.getProjectName());
            ptCommonService.sendLetter(platformAccount.getUserId(), "借款已发布", template);
        }catch(Exception e){
            LOG.error("标"+projectInfo.getProjectName()+"发布后发送站内信失败",e);
        }
    }

    private void establishFail(String requestNo,XWProjectInfo projectInfo,String errorMessage) throws Exception{
        //请求失败或处理失败
        XWRequest param=new XWRequest();
        param.setRequestNo(requestNo);
        param.setState(XWRequestState.SB);
        requestDao.updateRequest(param);
        String errorLog=String.format(XWResponseCode.BID_ESTABLISH_FAIL.getMessage(),projectInfo.getProjectName(),errorMessage);
        LOG.error(errorLog);
        throw new XWTradeException(XWResponseCode.BID_ESTABLISH_FAIL.getCode(),errorLog);
    }

}
