package com.fenlibao.dao.pms.da.cs;

import com.fenlibao.model.pms.da.cs.BorrowerAccountInfo;
import com.fenlibao.model.pms.da.cs.BussinessInfo;
import com.fenlibao.model.pms.da.cs.form.BorrowerAccountForm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;


public interface BorrowerAccountMapper {

    List<BorrowerAccountInfo> getBorrowerAccountInfoList(BorrowerAccountForm borrowerAccountForm, RowBounds bounds);

    /**
     * 用户待还金额
     * @param userIds
     * @return
     */
    List<Map<String, Object>> getAmountToBePaid(
            @Param("userIds") List<Integer> userIds
    );

    /**
     * 待还笔数
     * @param userIds
     * @return
     */
    List<Map<String, Object>> getNumTobePaid(
            @Param("userIds") List<Integer> userIds
    );

    /**
     * 逾期金额
     * @param userIds
     * @return
     */
    List<Map<String, Object>> getOverdueAmount(
            @Param("userIds") List<Integer> userIds
    );

    /**
     * 逾期笔数
     * @param userIds
     * @return
     */
    List<Map<String, Object>> getOverdueNum(
            @Param("userIds") List<Integer> userIds
    );

    /**
     * 企业借款人企业信息
     * @param userId
     * @return
     */
    BussinessInfo getBussinessInfoByUserId(Integer userId);
}
