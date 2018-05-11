package com.fenlibao.pms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fenlibao.p2p.dao.xinwang.account.XWAccountDao;
import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.credit.SysCreditDao;
import com.fenlibao.p2p.dao.xinwang.order.SysOrderManageDao;
import com.fenlibao.p2p.dao.xinwang.project.XWProjectDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysMakeLoanDao;
import com.fenlibao.p2p.dao.xinwang.trade.SysRepayDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectExtraInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectRepayInfo;
import com.fenlibao.p2p.model.xinwang.enums.common.XWRequestState;
import com.fenlibao.p2p.model.xinwang.enums.trade.SysRepayOperationType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeQueryType;
import com.fenlibao.p2p.model.xinwang.enums.trade.XWTradeType;
import com.fenlibao.p2p.service.xinwang.XWBatchNotifyService;
import com.fenlibao.p2p.service.xinwang.common.SpringContextUtil;
import com.fenlibao.p2p.service.xinwang.order.SysOrderService;
import com.fenlibao.p2p.service.xinwang.project.XWProjectService;
import com.fenlibao.p2p.service.xinwang.query.XWQueryTransactionService;
import com.fenlibao.p2p.service.xinwang.trade.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/18.
 */
@Controller
public class ForTestRepay {
    @Resource
    XWMakeLoanService makeLoanService;
    @Resource
    XWProjectDao projectDao;
    @Resource
    XWAccountDao accountDao;
    @Resource
    PTCommonDao commonDao;

    @Resource
    XWRequestDao requestDao;

    @Resource
    SysMakeLoanDao makeLoanDao;

    @Resource
    SysOrderManageDao orderManageDao;

    @Resource
    SysOrderService orderService;

    @Resource
    XWProjectService projectService;

    @Resource
    XWMakeLoanTransationService makeLoanTransationService;

    @Resource
    XWRepayTransactionService repayTransactionService;

    @Resource
    XWMakeLoanInTransactionService inTransactionService;


    @Resource
    SysCreditDao creditDao;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    XWRepayService repayService;

        @Resource
        SysRepayDao repayDao;

    @Resource
    XWQueryTransactionService queryTransactionService;

    @RequestMapping(value = "forRepay")
    public ModelAndView forRepay() {



        updateProjectExtraInfo();
//        testNotify();
//        this.platfromRepay();

//        this.checkIfRepayFinish();
//        this.test();
//        XWRepayResultConfirmJob();
//        XWRepayRerunJob();
//        testXWRepayMarketingResultConfirmJob();
//        getRepayMarketingRerunList();
//        updateRepaymentPlanById();
//        testGetPro();
//        makeLoanTransationService.testTransacation();
        return null;

    }

    public void updateRepaymentPlanById(){
        Map<String,Object> param=new HashMap<>();
        param.put("id",920561);
        param.put("repayState","YH");
        param.put("now","now");
        repayDao.updateRepaymentPlanById(param);
    }

    /**
     * 自动还款
     */
    public void testAutoRepay(){
        try {
            repayService.repayApply(2157943, SysRepayOperationType.REPAY, true,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void checkIfRepayFinish(){
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(1954592);
        try {
            repayService.checkIfRepayFinish(projectInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test(){
        XWProjectExtraInfo projectExtraInfo=projectDao.getProjectExtraInfo(1954592);

        System.out.println(111);
    }

    public void testNotify(){
        XWBatchNotifyService batchNotifyService = (XWBatchNotifyService) SpringContextUtil.getBean(XWRepayService.class);
        HashMap<String,Object> market=new HashMap<String,Object>();
        market.put("createTime","20180310180327");
        market.put("transactionTime","20180310180330");
        market.put("status","SUCCESS");
        market.put("bizType","MARKETING");
        market.put("asyncRequestNo","201803101843588df08a64-9");


        HashMap<String,Object> notMarkt=new HashMap<String,Object>();
        notMarkt.put("createTime","20180310180327");
        notMarkt.put("transactionTime","20180310180330");
        notMarkt.put("status","SUCCESS");
        notMarkt.put("bizType","REPAYMENT");
        notMarkt.put("asyncRequestNo","201803101843579ce321cc-2");
        List<Map<String,Object>> repayMarketingResponseList=new ArrayList<>();

        repayMarketingResponseList.add(market);
        repayMarketingResponseList.add(notMarkt);
        try{
            batchNotifyService.handleNotify(repayMarketingResponseList);
        }
        catch(Exception e){

            e.printStackTrace();
        }


    }

    public void updateProjectExtraInfo(){
        Map<String,Object> ProjectExtraInfoMap= new HashMap<>();
        ProjectExtraInfoMap.put("remainTerms",0);
        ProjectExtraInfoMap.put("id",2158296);
        this.projectDao.updateProjectExtraInfo(ProjectExtraInfoMap);
    }
    public void platfromRepay() {

        int orderId=20894638;
        List<String> reqList=new ArrayList<>();
        HashMap<Integer,List<String>> map = new HashMap();

        reqList.add("2018030814484191b2aed6-0");

        try {
            repayTransactionService.xinwangRepayFinish("2018030814484191b2aed6-0", XWTradeType.REPAYMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.put(orderId,reqList);
        SysProjectRepayInfo projectRepayInfo= null;
        XWProjectInfo projectInfo= null;
        try{
            projectRepayInfo = repayDao.getProjectRepayInfoByOrderId(orderId);
            projectInfo = projectDao.getProjectInfoById(projectRepayInfo.getProjectId());
            Integer projectId=projectRepayInfo.getProjectId();
//            repayTransactionService.platfromMarketing(projectId,projectInfo,projectRepayInfo,map.get(orderId));
            repayTransactionService.platfromRepay(projectId,projectInfo,projectRepayInfo,map.get(orderId));
        }
        catch(Exception ex){
            logger.error("还款订单"+orderId+"加息中断后重跑出错："+ex.toString(), ex);
        }


    }

    public void testGetPro(){
        SysProjectRepayInfo projectRepayInfo=repayDao.getProjectRepayInfoByOrderId(17882063);
        XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectRepayInfo.getProjectId());
        System.out.println(1111111);
    }


    public void XWRepayRerunJob(){
        {
            try {
                Map<Integer,List<String>> map=new HashMap<>();
                //距离新网还款成功超过1小时平台还没还款的请求
                List<XWRequest> requestList = repayDao.getRepayRerunList();
                for (XWRequest request : requestList) {
                    List<String> reqList=map.get(request.getOrderId());
                    if(reqList==null){
                        reqList=new ArrayList<>();
                        map.put(request.getOrderId(),reqList);
                    }
                    reqList.add(request.getRequestNo());
                }
                for(Integer orderId:map.keySet()){
                    SysProjectRepayInfo projectRepayInfo=repayDao.getProjectRepayInfoByOrderId(orderId);
                    XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectRepayInfo.getProjectId());
                    try{
                        Integer projectId=projectRepayInfo.getProjectId();
                        repayTransactionService.platfromRepay(projectId,projectInfo,projectRepayInfo,map.get(orderId));
                        //标是否还完
                        //repayService.checkIfRepayFinish(projectInfo);
                    }
                    catch(Exception ex){
                        logger.error("还款订单"+orderId+"平台还款中断后重跑出错："+ex.toString(), ex);
                    }
                    //发信息
                    try {
                        repayTransactionService.sendLetterAndMsg(Integer.valueOf(projectInfo.getProjectNo()), projectInfo, projectRepayInfo);
                    }
                    catch(Exception e){
                        logger.error("标"+projectInfo.getProjectName()+"还款后发送信息出错："+e.getMessage(),e);
                    }
                }
            } catch (Throwable e) {
                logger.error(e.toString(), e);
            }
        }
    }

    public void getRepayMarketingRerunList(){
        try {
            Map<Integer,List<String>> map=new HashMap<>();
            //距离新网还款加息成功超过1小时平台还没还款加息的请求

            List<XWRequest> requestList = repayDao.getRepayMarketingRerunList();
            for (XWRequest request : requestList) {
//                if(request.getOrderId()!=18012518)continue;
                List<String> reqList=map.get(request.getOrderId());
                if(reqList==null){
                    reqList=new ArrayList<>();
                    map.put(request.getOrderId(),reqList);
                }
                reqList.add(request.getRequestNo());
            }
            for(Integer orderId:map.keySet()){
                SysProjectRepayInfo projectRepayInfo=repayDao.getProjectRepayInfoByOrderId(orderId);
                XWProjectInfo projectInfo=projectDao.getProjectInfoById(projectRepayInfo.getProjectId());
                try{
                    Integer projectId=projectRepayInfo.getProjectId();
                    repayTransactionService.platfromMarketing(projectId,projectInfo,projectRepayInfo,map.get(orderId));
                }
                catch(Exception ex){
                    logger.error("还款订单"+orderId+"加息中断后重跑出错："+ex.toString(), ex);
                }
                //发信息
                try {
                    repayTransactionService.sendLetterAndMsg(Integer.valueOf(projectInfo.getProjectNo()), projectInfo, projectRepayInfo);
                }
                catch(Exception e){
                    logger.error("标"+projectInfo.getProjectName()+"还款后发送信息出错："+e.getMessage(),e);
                }
            }
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
    }

    public void XWRepayResultConfirmJob(){

        Map<Integer,List<String>> map=new HashMap<>();
        //距离批次受理超过30分钟的请求
        List<XWRequest> requestList=repayDao.getRepayResultConfirmList();
        for (XWRequest request : requestList) {
           if(request.getOrderId()!=446732) continue;
            // 查询新网请求状态
            try {
                String json=queryTransactionService.queryTransaction(null,null, XWTradeQueryType.TRANSACTION.name(),request.getRequestNo());
                Map<String, Object> respMap = JSON.parseObject(json);
                String code=(String)respMap.get("code");
                String status=(String)respMap.get("status");
                if(("0").equals(code)&& "SUCCESS".equals(status)){
                    //保存返回报文
                    XWResponseMessage responseParams=new XWResponseMessage();
                    responseParams.setRequestNo(request.getRequestNo());
                    responseParams.setResponseMsg(json);
                    requestDao.saveResponseMessage(responseParams);

                    JSONArray records=(JSONArray)respMap.get("records");
                    Map<String,String> item=(Map<String,String>)records.get(0);
                    String repayStatus=item.get("status");
                    if("SUCCESS".equals(repayStatus)){
                        repayTransactionService.xinwangRepayFinish(request.getRequestNo(), XWTradeType.parse(item.get("confirmTradeType")));
                    }
                    else if("FAIL".equals(repayStatus)){
                        XWRequest requestParams=new XWRequest();
                        requestParams.setRequestNo(request.getRequestNo());
                        requestParams.setState(XWRequestState.SB);
                        requestDao.updateRequest(requestParams);
                    }
                }
                else {
                    logger.error("查询还款请求"+request.getRequestNo()+"失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("新网还款结果"+request.getRequestNo()+"异常");
            }
        }
    }


}
