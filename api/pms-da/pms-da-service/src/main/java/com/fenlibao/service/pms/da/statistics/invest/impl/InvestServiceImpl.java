package com.fenlibao.service.pms.da.statistics.invest.impl;

import com.fenlibao.dao.pms.da.statistics.invest.InvestMapper;
import com.fenlibao.model.pms.da.statistics.invest.ClientTypeInfo;
import com.fenlibao.model.pms.da.statistics.invest.InvestForm;
import com.fenlibao.model.pms.da.statistics.invest.InvestInfo;
import com.fenlibao.service.pms.da.statistics.invest.InvestService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2015/11/6.
 */
@Service
public class InvestServiceImpl implements InvestService {

    @Resource
    private InvestMapper investMapper;


    @Override
    public List<InvestInfo> getInvestStatistics(InvestForm investForm, RowBounds bounds){
        List<Integer> userIds = new ArrayList<>();
        List<Map<String, Object>> investTotal = new ArrayList<>();
        List<Map<String, Object>> investBase = new ArrayList<>();
        /*投资记录*/
        List<InvestInfo> investList = investMapper.getInvestDetail(investForm, bounds);
        for (InvestInfo investInfo: investList) {
            if (!userIds.contains(investInfo.getUserId())) {
                userIds.add(Integer.valueOf(investInfo.getUserId()));
            }
        }
        if(userIds.size() > 0){
            investTotal = investMapper.getInvestMoneyAndTimesTotal(userIds);
            investBase = investMapper.getUserBaseInfo(userIds);
            for (InvestInfo investInfo: investList) {
                Map<String, Object> totalExtend = getInvestTotalExtend(investTotal, investInfo.getUserId());
                Map<String, Object> baseExtend = getInvestBaseExtend(investBase, investInfo.getUserId());
                if(totalExtend != null){
                    investInfo.setInvestCount(
                            Integer.valueOf(totalExtend.get("investCount").toString() == null ? null : totalExtend.get("investCount").toString()));
                    investInfo.setInvestTotalMoney(
                            new BigDecimal(totalExtend.get("investTotalMoney").toString() == null ? null : totalExtend.get("investTotalMoney").toString()));
                }
                if(baseExtend != null){
                    investInfo.setRealName(
                            (baseExtend.get("realName").toString() == null ? null : baseExtend.get("realName").toString()));
                    investInfo.setSurplusMoney(
                            new BigDecimal(baseExtend.get("surplusMoney").toString() == null ? null : baseExtend.get("surplusMoney").toString()));
                }
            }
        }
        return investList;
    }

    private Map<String, Object> getInvestTotalExtend(List<Map<String, Object>> investTotal, Integer userId){
        for (Map<String, Object> investTotalDetail : investTotal) {
            if (userId.toString().equals(investTotalDetail.get("userId").toString())) {
                return investTotalDetail;
            }
        }
        return null;
    }

    private Map<String, Object> getInvestBaseExtend(List<Map<String, Object>> investBase, Integer userId){
        for (Map<String, Object> investBaseDetail : investBase) {
            if (userId.toString().equals(investBaseDetail.get("userId").toString())) {
                return investBaseDetail;
            }
        }
        return null;
    }


    @Override
    public InvestInfo getInvestTotal(InvestForm investForm) {
        //获取用户的投资总计
        return investMapper.getInvestTotal(investForm);
    }

    @Override
    public List<ClientTypeInfo> getClientTypeList(Integer typeId) {
        Map<String,Object> map = new HashMap();
        map.put("typeId",typeId);
        return investMapper.getClientTypeList(map);
    }

    @Override
    public InvestInfo getUserInvestDetails(String userId) {
        InvestInfo investInfo = null;
//        if(!StringUtils.isEmpty(userId)){
//            investInfo = investMapper.getUserInvestDetails(Integer.parseInt(userId));
//            if(investInfo != null){
//                BigDecimal totalMoney = investInfo.getOutmoney();
//                DecimalFormat formatMoney = new DecimalFormat();
//                formatMoney.applyPattern("##,###.00");
//                String strMoney = formatMoney.format(totalMoney);
//                investInfo.setFormatMoney(strMoney);
//            }
//        }
        return investInfo;
    }

    @Override
    public List<InvestInfo> getUserRankingDetails(String userId, RowBounds bounds) {
        List<InvestInfo> UserRankList = null;
        if(!StringUtils.isEmpty(userId)){
            UserRankList = investMapper.getUserRankingDetails(Integer.parseInt(userId), bounds);
        }
        return UserRankList;
    }


    private Map<String,Object> getParams(InvestForm investForm){
        Map<String,Object> paramMap = new HashMap<String,Object>();
//        //根据条件查询数据
//        if(!StringUtils.isEmpty(investForm)){
//            String beginDate = investForm.getBeginDate();
//            String endDate = investForm.getEndDate();
//            String InvestStartDate = investForm.getInvestStartDate();
//            String InvestEndDate = investForm.getInvestEndDate();
//
//            if(!StringUtils.isEmpty(beginDate)){
//                Date beginTime = DateUtil.StringToDate(beginDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
//                paramMap.put("beginTime",beginTime);
//            }
//            if(!StringUtils.isEmpty(endDate)){
//                Date endTime = DateUtil.StringToDate(endDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
//                paramMap.put("endTime",endTime);
//            }
//            if(!StringUtils.isEmpty(InvestStartDate)){
//                Date InvestStartTime = DateUtil.StringToDate(InvestStartDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
//                paramMap.put("InvestStartTime",InvestStartTime);
//            }
//            if(!StringUtils.isEmpty(InvestEndDate)){
//                Date InvestEndTime = DateUtil.StringToDate(InvestEndDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
//                paramMap.put("InvestEndTime",InvestEndTime);
//            }
//            if(!StringUtils.isEmpty(investForm.getPhoneNum())){
//                paramMap.put("phoneNum",investForm.getPhoneNum());
//            }
//            if(!StringUtils.isEmpty(investForm.getMoneyStart())){
//                paramMap.put("moneyStart",investForm.getMoneyStart());
//            }
//            if(!StringUtils.isEmpty(investForm.getMoneyEnd())){
//                paramMap.put("moneyEnd",investForm.getMoneyEnd());
//            }
//            if(!StringUtils.isEmpty(investForm.getClientType())){
//                paramMap.put("clientType",investForm.getClientType());
//            }
//        }
        return paramMap;
    }

    @Override
    public List<Map<String, String>> getProductTypeExtend() {
        List<Map<String, String>> productTypeExtend = investMapper.getProductTypeExtend();
        return productTypeExtend;
    }
}

