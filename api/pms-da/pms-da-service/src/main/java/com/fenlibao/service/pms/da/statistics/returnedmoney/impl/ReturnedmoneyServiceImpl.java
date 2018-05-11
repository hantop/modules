package com.fenlibao.service.pms.da.statistics.returnedmoney.impl;

import com.fenlibao.dao.pms.da.statistics.returnedmoney.ReturnedmoneyMapper;
import com.fenlibao.model.pms.da.statistics.returnedmoney.ReturnedmoneyInfo;
import com.fenlibao.model.pms.da.statistics.returnedmoney.UserRefundStatus;
import com.fenlibao.service.pms.da.statistics.returnedmoney.ReturnedmoneyService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 回款信息
 *
 * Created by chenzhixuan on 2016/3/22.
 */
@Service
public class ReturnedmoneyServiceImpl implements ReturnedmoneyService {

    @Resource
    private ReturnedmoneyMapper returnedmoneyMapper;


    @Override
    public int getFirstReturnedmoneysTotal(List<Integer> receivableUsers, List<Integer> hasReceivableUsers, Date startDate, Date endDate) {
        return returnedmoneyMapper.getFirstReturnedmoneysTotal(receivableUsers, hasReceivableUsers, startDate, endDate);
    }

    @Override
    public int getReturnedmoneysTotal(boolean isFirstReturnedmoney, Integer status, Date startDate, Date endDate) {
        return returnedmoneyMapper.getReturnedmoneysTotal(isFirstReturnedmoney, status, startDate, endDate);
    }

    @Override
    public List<ReturnedmoneyInfo> getFirstReturnedmoneys(List<Integer> receivableUsers, List<Integer> hasReceivableUsers, Date startDate, Date endDate, RowBounds bounds) {
        return returnedmoneyMapper.getFirstReturnedmoneys(receivableUsers, hasReceivableUsers, startDate, endDate, bounds);
    }

    @Override
    public List<UserRefundStatus> getUserReturnTypes(Date startDate, Date endDate) {
        return returnedmoneyMapper.getUserReturnTypes(startDate, endDate);
    }

    @Override
    public List<ReturnedmoneyInfo> getReturnedmoneys(boolean isFirstReturnedmoney, Integer status, Date startDate, Date endDate, RowBounds bounds) {
        return returnedmoneyMapper.getReturnedmoneys(isFirstReturnedmoney, status, startDate, endDate, bounds);
    }
}
