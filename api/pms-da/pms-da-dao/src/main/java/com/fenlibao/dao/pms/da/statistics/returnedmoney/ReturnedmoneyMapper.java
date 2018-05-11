package com.fenlibao.dao.pms.da.statistics.returnedmoney;

import com.fenlibao.model.pms.da.statistics.returnedmoney.ReturnedmoneyInfo;
import com.fenlibao.model.pms.da.statistics.returnedmoney.UserRefundStatus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

/**
 * 回款信息
 * <p/>
 * Created by chenzhixuan on 2016/3/22.
 */
public interface ReturnedmoneyMapper {

    int getFirstReturnedmoneysTotal(
            @Param(value = "receivableUsers") List<Integer> receivableUsers,
            @Param(value = "hasReceivableUsers") List<Integer> hasReceivableUsers,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate);

    int getReturnedmoneysTotal(
            @Param(value = "isFirstReturnedmoney") boolean isFirstReturnedmoney,
            @Param(value = "status") Integer status,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate);

    List<ReturnedmoneyInfo> getFirstReturnedmoneys(
            @Param(value = "receivableUsers") List<Integer> receivableUsers,
            @Param(value = "hasReceivableUsers") List<Integer> hasReceivableUsers,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            RowBounds bounds);

    List<UserRefundStatus> getUserReturnTypes(
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate);

    List<ReturnedmoneyInfo> getReturnedmoneys(
            @Param(value = "isFirstReturnedmoney") boolean isFirstReturnedmoney,
            @Param(value = "status") Integer status,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            RowBounds bounds);
}
