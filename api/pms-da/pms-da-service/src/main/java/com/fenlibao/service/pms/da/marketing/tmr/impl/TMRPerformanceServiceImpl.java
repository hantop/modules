package com.fenlibao.service.pms.da.marketing.tmr.impl;

import com.fenlibao.common.pms.util.tool.DateUtil;
import com.fenlibao.dao.pms.da.marketing.tmr.TMRPerformanceMapper;
import com.fenlibao.model.pms.da.marketing.*;
import com.fenlibao.service.pms.da.marketing.tmr.TMRPerformanceService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Louis Wang on 2016/3/9.
 */
@Service
public class TMRPerformanceServiceImpl implements TMRPerformanceService {

    @Resource
    TMRPerformanceMapper tmrMapper;

    /**
     * 导入电销通话记录表单
     * @param tmr
     * @param importList
     * @return
     */
    @Transactional
    @Override
    public Integer createTMRPerformance(TMRPerformanceVO tmr, List<String[]> importList) {

        List<String> returnCodeList = new ArrayList<>();
        Integer tmrId = tmrMapper.createTMRPerformance(tmr);
    //    Map<String,Object> callOutTelMap = new HashMap<>();

        //验证数据正确性

        for (int i = 0; i < importList.size(); i++) {
            StringBuilder message  = new StringBuilder();
            boolean isPre = true;
            message.append("第" + (i + 2) + "行：");
            String row[] = importList.get(i);
            String phone = StringUtils.trim(row[0]);
            String phoneTime = StringUtils.trim(row[1]);
            if(StringUtils.isBlank(phone)){
                message.append(" 手机号码为空!\r");
                isPre = false;
            }
            if(StringUtils.isBlank(phoneTime)){
                message.append(" 导入时间为空!\n");
                isPre = false;
            }

            if(!isPre){
                returnCodeList.add(message.toString());
            }
        }

        if (importList.size() > 10000) {
            returnCodeList.add("一次不能导入超过10000条记录！");
        }

        if(returnCodeList.isEmpty()){
            List<TMRExcelVO> tmrList = new ArrayList<>();
            try {
                for (int i = 0; i < importList.size(); i++) {
                    if (importList.get(i) != null) {
                        String row[] = importList.get(i);
                        String phone = StringUtils.trim(row[0]);
                        //@Wang@ 重新拼接时间方法
                        String phoneTime = StringUtils.trim(row[1]).substring(0,10).concat(" " + row[1].substring(10));
                        if(!StringUtils.isEmpty(phone)){
                            /*if(i == 0){
                                callOutTelMap.put(phone,phoneTime);
                            }else { //解析每条数据，如果有重复手机号码的保存最先开始时间段通话的
                                String tmpKey = "";
                                String tmpValue = "";
                                for(Map.Entry<String, Object> entry:callOutTelMap.entrySet()){
                                    if(entry.getKey().equals(phone)){

                                        int flag = DateUtil.compareDate(entry.getValue().toString(),phoneTime);
                                        if(flag < 0){
                                            tmpKey = entry.getKey();
                                            tmpValue = phoneTime;
                                        }
                                    }else {
                                        tmpKey = phone;
                                        tmpValue = phoneTime;
                                    }
                                }
                                if(!StringUtils.isEmpty(tmpKey) && !StringUtils.isEmpty(tmpValue)){
                                    callOutTelMap.put(tmpKey,tmpValue);
                                }
                            }*/
                            //modify @Jing 20160429 需要导入所有的通话记录
                            TMRExcelVO tmrVO = new TMRExcelVO();
                            tmrVO.setTrmId(tmr.getId());
                            tmrVO.setPhoneNumber(phone);
                            tmrVO.setCallTime(phoneTime);
                            tmrList.add(tmrVO);
                        }
                    }
                }
            }catch (Exception e){
                returnCodeList.add("请检查数据格式是否正确，稍后尝试！！！");
            }
            if(tmrList != null && tmrList.size() > 0){
                tmrMapper.saveTMRCallTelList(tmrList);
            }
            returnCodeList.add("success");
            /*List<TMRExcelVO> tmrList = new ArrayList<>();
            if(!callOutTelMap.isEmpty()){
                for(Map.Entry<String, Object> entry:callOutTelMap.entrySet()){
                    TMRExcelVO tmrVO = new TMRExcelVO();
                    tmrVO.setTrmId(tmr.getId());
                    tmrVO.setPhoneNumber(entry.getKey());
                    tmrVO.setCallTime(entry.getValue().toString());

                    tmrList.add(tmrVO);
                }
                tmrMapper.saveTMRCallTelList(tmrList);
                returnCodeList.add("success");
            }*/
        }
        return tmr.getId();
    }

    @Override
    public List<TMRPerformanceVO> getTMRPerformanceList(TMRPerformanceForm tmrPerformanceForm, RowBounds bounds) {

        List<TMRPerformanceVO> tmrInfoList = new ArrayList<>();
        Map<String, Object> paramMap = setParam(tmrPerformanceForm);
        paramMap.put("visible",tmrPerformanceForm.isVisible());
        if(!paramMap.isEmpty() && paramMap.size() > 0){
            tmrInfoList = tmrMapper.getTMRPerformanceList(paramMap, bounds);
        }
        return tmrInfoList;
    }

    @Transactional
    @Override
    public String calculatePerformance(Integer id) throws Exception{
        //获取导入的数据
        List<TMRExcelVO> excels = tmrMapper.getImportDataById(id);
        String doFlag = "";
        List<TMRInvestUserVO> investUserList = new ArrayList<>();
        Map<String,Object> callOutTelMap = new HashMap<>(); //保存每个手机通话投资记录

        for (TMRExcelVO excel: excels) {
            Date callTime = DateUtil.getDate(excel.getEndTime().getTime());
            Date InvestTime = DateUtil.dateAdd(callTime,14);    //获取截至日期
            Date InvestTimeEnd = DateUtil.StringToDate(DateUtil.getDate(InvestTime) + " 23:59:59", "yyyy-MM-dd HH:mm:ss");  //时间转换具体到时分秒
            String callPhone = excel.getPhoneNumber();
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("callPhone",callPhone);
            paramMap.put("callTime",excel.getEndTime());
            paramMap.put("investTime",InvestTimeEnd);
            List<TMRInvestUserVO> investUsers = tmrMapper.findUserInvestBehavior(paramMap); //查找用户投资行为

            /**
             * 因为一个客户可能多个电话，但是业绩是算15 天的，这里的解决是每个通话
             * 时间的在15 内的投资，如何有相同的交集，就取一个。
             */
            if(!callOutTelMap.isEmpty()){
                if(investUsers != null && investUsers.size() > 0){
                    List<TMRInvestUserVO> newInvesters = null;
                    for(Map.Entry<String, Object> entry:callOutTelMap.entrySet()){
                        String phoneKey = entry.getKey();
                        if(callPhone.equals(phoneKey)){
                            List<TMRInvestUserVO> investerMap = (List<TMRInvestUserVO>) entry.getValue();
                            newInvesters = investerMap;
                            for (TMRInvestUserVO investerLoop:investUsers) {
                                boolean isStauts = existsInvesterInfo(investerLoop,investerMap);
                                if(!isStauts){
                                    newInvesters.add(investerLoop);
                                }
                            }

                        }
                    }
                    //将新添加的元素放进对应key 的集合
                    if(newInvesters != null && newInvesters.size() > 0){
                        callOutTelMap.put(callPhone,newInvesters);
                    }else {
                        callOutTelMap.put(callPhone,investUsers);
                    }
                }
            }else {
                callOutTelMap.put(callPhone,investUsers);   //保存每个手机投资记录
            }
        }

        if (!callOutTelMap.isEmpty()){
            for(Map.Entry<String, Object> entry:callOutTelMap.entrySet()){
                List<TMRInvestUserVO> investerMap = (List<TMRInvestUserVO>) entry.getValue();
                investUserList.addAll(investerMap);
            }
        }

        if(investUserList != null && investUserList.size() > 0){

            for (TMRInvestUserVO  invester : investUserList) {
                invester.setTmrId(id);
                if(invester.getActivateRedBag().compareTo(new BigDecimal(0)) > 0){
                    //通过流水表来查询具体的返现红包
                    BigDecimal cashBack = new BigDecimal(0);
                    Map<String,Object> cashBackMap = new HashMap<>();
                    cashBackMap.put("userId",invester.getUserId());
                    cashBackMap.put("createTime",invester.getInvestTime());

                    List<TMRInvestUserVO> tmpUserList = tmrMapper.getCashBack(cashBackMap);
                    if(tmpUserList != null && tmpUserList.size() > 0){
                        for (TMRInvestUserVO tmpUser: tmpUserList) {
                            cashBack = cashBack.add(tmpUser.getActivateRedBag());
                        }
                        invester.setActivateRedBag(cashBack);
                    }else{
                        invester.setActivateRedBag(new BigDecimal(0));
                    }

                }
                String dateTmp = invester.getInvestDate();
                if(!StringUtils.isEmpty(dateTmp)){
                    if(Integer.parseInt(dateTmp) > 0){  //按月投资
                        invester.setInvestDate(dateTmp.concat("_M"));
                    }else {
                        invester.setInvestDate(invester.getInvestDays().concat("_D"));
                    }
                }
            }
            tmrMapper.saveUserInvestBehavior(investUserList);

            doFlag = "1";
        }else{
            doFlag = "0";
        }
        tmrMapper.updateTMRInfoDispose(id);    //已经计算过 只是做一次计算
        return doFlag;
    }

    /**
     *  判断是否已经有相同类型的投资
     * @param investerLoop
     * @param investerMap
     */
    private boolean existsInvesterInfo(TMRInvestUserVO investerLoop, List<TMRInvestUserVO> investerMap) {
        boolean isNew = false;
        Timestamp saveInvesterTime = investerLoop.getInvestTime();
        for (TMRInvestUserVO invester:investerMap) {
            Timestamp investerTime = invester.getInvestTime();
            if (investerTime.equals(saveInvesterTime)){
                isNew = true;
            }
        }
        return isNew;
    }

    @Override
    public List<TMRInvestUserVO> getTMRInvesterList(TMRInvestUserForm investUserForm, RowBounds bounds) {
        List<TMRInvestUserVO> investerList = new ArrayList<>();
        Map<String, Object> paramMap = getTMRParams(investUserForm);

        if(!paramMap.isEmpty() && paramMap.size() > 0){
            investerList = tmrMapper.getTMRInvesterList(paramMap, bounds);
        }
        return investerList;
    }

    @Override
    public List<TMRInvestUserVO> getTMRInvesterTotal(TMRInvestUserForm investUserForm, RowBounds rowBounds) {
        List<TMRInvestUserVO> investerList = new ArrayList<>();
        Map<String, Object> paramMap = getTMRParams(investUserForm);

        if(!paramMap.isEmpty() && paramMap.size() > 0){
            investerList = tmrMapper.getTMRInvesterTotal(paramMap, rowBounds);
        }
        return investerList;
    }

    @Override
    public List<TMRExcelVO> troubleUserInfo(Integer tmrId, RowBounds bounds) {
        List<TMRExcelVO> tmrs = tmrMapper.getImportDataById(tmrId);
        List<TMRExcelVO> tmrExcelVOs = null;
        //判断是否有重复的或者是没有注册的
        if(tmrs != null && tmrs.size() > 0){
            tmrExcelVOs = troubleDeals(tmrId,tmrs,bounds);
        }
        return tmrExcelVOs;
    }

    /**
     * 导入报表里面
     * @param tmrId
     * @param tmrs
     * @param bounds
     * @return
     */
    private List<TMRExcelVO> troubleDeals(Integer tmrId, List<TMRExcelVO> tmrs, RowBounds bounds) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tmrId",tmrId);
        paramMap.put("list",tmrs);
        List<TMRExcelVO> tmrExcelVOs = tmrMapper.troubleDeals(paramMap,bounds);

        return tmrExcelVOs;
    }

    private Map<String, Object> getTMRParams(TMRInvestUserForm investUserForm) {
        Map<String, Object> map = new HashMap<>();
        map.put("id",investUserForm.getId());

        String beginDate = investUserForm.getStartTime();
        String endDate = investUserForm.getEndTime();
        if(!StringUtils.isEmpty(beginDate)){
            Date costStartDate = DateUtil.StringToDate(beginDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            map.put("costStartTime",costStartDate);
        }
        if(!StringUtils.isEmpty(endDate)){
            Date costEndDate = DateUtil.StringToDate(endDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            map.put("costEndTime",costEndDate);
        }
        return map;
    }

    @Override
    public void deletePerformance(Integer id) {
            if(id != null){
                tmrMapper.deletePerformance(id);
            }
    }

    @Override
    public TMRPerformanceVO getTMRinfo(Integer id) {
       return tmrMapper.getTMRinfo(id);
    }

    @Override
    public TMRPerformanceVO findRecordByFileName(String fileName) {

        return tmrMapper.findRecordByFileName(fileName);
    }

   

    private Map<String, Object> setParam(TMRPerformanceForm tmrForm) {
        Map<String, Object> paramMap = new HashMap<>();
        if(tmrForm != null){
            paramMap.put("tmrName", tmrForm.getTmrName());
            paramMap.put("fileName", tmrForm.getFileName());

            String beginDate = tmrForm.getStartTime();
            String endDate = tmrForm.getEndTime();
            Date costStartDate;
            Date costEndDate;
            /**
             * 这个统计的时间是开始得到收益的时间，我们计算收益的时间是T+1,
             * 所以查询范围内的体验金应该要提前一天
             */
            if(!StringUtils.isEmpty(beginDate)){
                costStartDate = DateUtil.StringToDate(beginDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("costStartTime",costStartDate);
            }else {
                String defaultStartDate =  defaultDate(0);
                costStartDate = DateUtil.StringToDate(defaultStartDate, "yyyy-MM-dd HH:mm:ss");
                paramMap.put("costStartTime",costStartDate);
                tmrForm.setStartTime(DateUtil.getDate(costStartDate));
            }
            if(!StringUtils.isEmpty(endDate)){
                costEndDate = DateUtil.StringToDate(endDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                paramMap.put("costEndTime",costEndDate);
            }else {
                String defaultEndDate =  defaultDate(1);
                costEndDate = DateUtil.StringToDate(defaultEndDate, "yyyy-MM-dd HH:mm:ss");
                paramMap.put("costEndTime",costEndDate);
                tmrForm.setEndTime(DateUtil.getDate(costEndDate));
            }
        }
        return paramMap;
    }

    /**
     * 默认自然月
     * @param i 0 开始 1 结束
     */
    private String defaultDate(int i) {
        Calendar calTime = Calendar.getInstance();
        //设置天数为-1天，表示当月减一天即为上一个月的月末时间
//        calTime.set(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        /* 上个月时间范围
        if(i == 0){
            calTime.set(calTime.get(Calendar.YEAR), calTime.get(Calendar.MONTH),1,0,0,0);
        }else if(i == 1){
            calTime.set(calTime.get(Calendar.YEAR), calTime.get(Calendar.MONTH),calTime.get(Calendar.DAY_OF_MONTH),23,59,59);
        }*/
        if(i == 0){
            calTime.set(calTime.get(Calendar.YEAR), calTime.get(Calendar.MONTH),calTime.get(Calendar.DATE),0,0,0);
        }else if(i == 1){
            calTime.set(calTime.get(Calendar.YEAR), calTime.get(Calendar.MONTH),calTime.get(Calendar.DATE),23,59,59);
        }
        return sdf.format(calTime.getTime());
    }
}
