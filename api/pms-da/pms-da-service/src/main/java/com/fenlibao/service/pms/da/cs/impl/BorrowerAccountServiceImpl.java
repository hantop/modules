package com.fenlibao.service.pms.da.cs.impl;

import com.fenlibao.dao.pms.da.cs.BorrowerAccountMapper;
import com.fenlibao.model.pms.da.cs.BorrowerAccountInfo;
import com.fenlibao.model.pms.da.cs.BussinessInfo;
import com.fenlibao.model.pms.da.cs.form.BorrowerAccountForm;
import com.fenlibao.service.pms.da.cs.BorrowerAccountService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2015/12/23.
 */
@Service
public class BorrowerAccountServiceImpl implements BorrowerAccountService{

    @Resource
    private BorrowerAccountMapper borrowerAccountMapper;

    @Override
    public BussinessInfo getBussinessInfoByUserId(Integer userId) {
        return borrowerAccountMapper.getBussinessInfoByUserId(userId);
    }

    @Override
    public List<BorrowerAccountInfo> getBorrowerAccountInfoList(BorrowerAccountForm borrowerAccountFormOri, RowBounds bounds) {
        BorrowerAccountForm borrowerAccountForm = buildBorrowerAccountForm(borrowerAccountFormOri);
        List<BorrowerAccountInfo> borrowerAccountInfoList = borrowerAccountMapper.getBorrowerAccountInfoList(borrowerAccountForm, bounds);
        if(borrowerAccountInfoList.size() > 0){
            List<Integer> userIds = new ArrayList<>();
            // 待还金额
            List<Map<String, Object>> amountToBePaidList = new ArrayList<>();
            // 待还笔数
            List<Map<String, Object>> numTobePaidList = new ArrayList<>();
            // 逾期金额
            List<Map<String, Object>> overdueAmountList = new ArrayList<>();
            // 逾期笔数
            List<Map<String, Object>> overdueNumList = new ArrayList<>();
            for (BorrowerAccountInfo borrowerAccountInfo : borrowerAccountInfoList) {
                if(!userIds.contains(borrowerAccountInfo.getUserId())){
                    userIds.add(borrowerAccountInfo.getUserId());
                }
            }
            amountToBePaidList =  borrowerAccountMapper.getAmountToBePaid(userIds);
            numTobePaidList =  borrowerAccountMapper.getNumTobePaid(userIds);
            overdueAmountList =  borrowerAccountMapper.getOverdueAmount(userIds);
            overdueNumList =  borrowerAccountMapper.getOverdueNum(userIds);
            for (BorrowerAccountInfo borrowerAccountInfo : borrowerAccountInfoList) {
                Map<String, Object> amountToBePaid = getPersonalDetail(amountToBePaidList, borrowerAccountInfo.getUserId());
                Map<String, Object> numTobePaid = getPersonalDetail(numTobePaidList, borrowerAccountInfo.getUserId());
                Map<String, Object> overdueAmount = getPersonalDetail(overdueAmountList, borrowerAccountInfo.getUserId());
                Map<String, Object> overdueNum = getPersonalDetail(overdueNumList, borrowerAccountInfo.getUserId());
                if(amountToBePaid != null){
                    borrowerAccountInfo.setAmountToBePaid(
                            amountToBePaid.get("amountToBePaid").toString() == null ? null : amountToBePaid.get("amountToBePaid").toString());
                }
                if(numTobePaid != null){
                    borrowerAccountInfo.setNumTobePaid(
                            numTobePaid.get("numTobePaid").toString() == null ? null : numTobePaid.get("numTobePaid").toString());
                }
                if(overdueAmount != null){
                    borrowerAccountInfo.setOverdueAmount(
                            overdueAmount.get("overdueAmount").toString() == null ? null : overdueAmount.get("overdueAmount").toString());
                }
                if(overdueNum != null){
                    borrowerAccountInfo.setOverdueNum(
                            overdueNum.get("overdueNum").toString() == null ? null : overdueNum.get("overdueNum").toString());
                }
            }
        }
        return borrowerAccountInfoList;
    }

    private Map<String, Object> getPersonalDetail(List<Map<String, Object>> detailList, Integer userId){
        for (Map<String, Object> detail : detailList) {
            if (userId.toString().equals(detail.get("userId").toString())) {
                return detail;
            }
        }
        return null;
    }

    private BorrowerAccountForm buildBorrowerAccountForm(BorrowerAccountForm borrowerAccountFormOri){
        if(borrowerAccountFormOri == null){
            return null;
        }else{
            if(borrowerAccountFormOri.getAccount() != null && borrowerAccountFormOri.getAccount() != ""){
                borrowerAccountFormOri.setAccount(borrowerAccountFormOri.getAccount().trim());
            }
            if(borrowerAccountFormOri.getName() != null && borrowerAccountFormOri.getName() != ""){
                borrowerAccountFormOri.setName(borrowerAccountFormOri.getName().trim());
            }
            return borrowerAccountFormOri;
        }
    }

}
