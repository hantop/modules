package com.fenlibao.service.pms.da.statistics.returnedmoney;

import com.fenlibao.model.pms.da.statistics.returnedmoney.ReturnedmoneyInfo;
import com.fenlibao.model.pms.da.statistics.returnedmoney.UserRefundStatus;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

/**
 * 回款信息
 * <p/>
 * Created by chenzhixuan on 2016/3/22.
 */
public interface ReturnedmoneyService {

    /**
     * 按用户ID获取首次回款列表总计
     *
     * @param receivableUsers
     * @param hasReceivableUsers
     * @param startDate
     * @param endDate
     * @return
     */
    int getFirstReturnedmoneysTotal(List<Integer> receivableUsers, List<Integer> hasReceivableUsers, Date startDate, Date endDate);

    /**
     * 回款列表总计
     *
     * @param isFirstReturnedmoney
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    int getReturnedmoneysTotal(boolean isFirstReturnedmoney, Integer status, Date startDate, Date endDate);

    /**
     * 按用户ID获取首次回款列表
     *
     * @param receivableUsers
     * @param hasReceivableUsers
     * @param startDate
     * @param endDate
     * @return
     */
    List<ReturnedmoneyInfo> getFirstReturnedmoneys(List<Integer> receivableUsers, List<Integer> hasReceivableUsers, Date startDate, Date endDate, RowBounds bounds);

    /**
     * 用户回款状态
     *
     * @return
     */
    List<UserRefundStatus> getUserReturnTypes(Date startDate, Date endDate);

    /**
     * 回款列表
     *
     * @param isFirstReturnedmoney
     * @param status
     * @param startDate
     * @param endDate
     * @param bounds
     * @return
     */
    List<ReturnedmoneyInfo> getReturnedmoneys(boolean isFirstReturnedmoney, Integer status, Date startDate, Date endDate, RowBounds bounds);
}
