package com.fenlibao.dao.pms.da.finance.returncash;

import com.fenlibao.model.pms.da.finance.vo.ReturncachRedpacketVO;
import com.fenlibao.model.pms.da.finance.vo.UserUsedReturncachRedpacketVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public interface ReturncashRedpacketMapper {
    /**
     * 获取已使用的返现红包金额
     *
     * @param startDate
     * @param endDate
     * @param activityCode
     * @param systemgrantFlag
     * @return
     */
    BigDecimal findUsedTotalCost(
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            @Param(value = "activityCode") String activityCode,
            @Param(value = "systemgrantFlag") boolean systemgrantFlag);
    
    /**
     * 统计返现券的激活数量
     * @param startDate
     * @param endDate
     * @param activityCode
     * @param systemgrantFlag
     * @return
     */
    Integer findTotlActiveCount(
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            @Param(value = "activityCode") String activityCode,
            @Param(value = "systemgrantFlag") boolean systemgrantFlag);

    /**
     * 获取已使用的返现红包详情
     *
     * @param startDate
     * @param endDate
     * @param redpacketId
     * @param systemgrantFlag
     * @param phoneNum
     * @param phoneNum
     * @param bounds
     * @return
     */
    List<UserUsedReturncachRedpacketVO> findUserUsedReturncachRedpackets(
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            @Param(value = "redpacketId") Integer redpacketId,
            @Param(value = "systemgrantFlag") boolean systemgrantFlag,
            @Param(value = "phoneNum") String phoneNum,
            RowBounds bounds);

    /**
     * 获取已使用的返现红包
     *
     * @param startDate
     * @param endDate
     * @param activityCode
     * @param systemgrantFlag
     * @param bounds
     * @return
     */
    List<ReturncachRedpacketVO> findUsedReturncachRedpackets(
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            @Param(value = "activityCode") String activityCode,
            @Param(value = "systemgrantFlag") boolean systemgrantFlag,
            RowBounds bounds);

    /**
     * 统计返现券的发送数量
     * @param startDate
     * @param endDate
     * @param activityCode
     * @param systemgrantFlag
     * @return
     */
    Integer findTotlRedNumber(@Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            @Param(value = "activityCode") String activityCode,
            @Param(value = "systemgrantFlag") boolean systemgrantFlag);

    /**
     * 单独一种返现券的发送数量
     * @param redpacketId
     * @return
     */
	Integer getEachRedPacketSendAmount(
			@Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            @Param(value = "activityCode") String activityCode,
            @Param(value = "redpacketId")Integer redpacketId,
            @Param(value = "systemgrantFlag") boolean systemgrantFlag);
}
