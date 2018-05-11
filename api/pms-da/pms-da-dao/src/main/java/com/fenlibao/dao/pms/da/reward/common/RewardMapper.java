package com.fenlibao.dao.pms.da.reward.common;

import com.fenlibao.model.pms.da.reward.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface RewardMapper {
    List<RewardRecord> getRewardRecords(RewardRecordCondition rewardRecordCondition, RowBounds bounds);

    Integer getUserIdbyPhone(String phone);

    /**
     * 查找xw_account userId
     * @param platformUserNo
     * @return
     */
    Integer getUserXWUserId(String platformUserNo);

    int insertSelective(RewardRecord newRecord);

    int insertRewardRecord(RewardRecord record);

    int updateRewardRecord(RewardRecord record);

    Integer getSalaryAccountId();

    T6101 getT6101ForUpdate(T6101 t6101);

    Integer batchUpdateT6101(List<T6101> t6101sToUpdate);

    Integer batchInsertT6102(List<T6102> t6102sToInsert);

    Integer insertT6123(T6123 t6123);

    Integer insertT1040(T1040 t1040);

    Integer batchInsertT6124(List<T6124> t6124sToInsert);

    Integer batchInsertT1041(List<T1041> t1041sToInsert);

    RewardRecord getRewardRecordById(RewardRecord rewardRecord);

    Integer deleteRewardRecord(RewardRecord record);

    /**
     * 根据发送记录的id获取待发送记录的发送状态
     *
     * @param id
     * @return
     */
    Byte selectGrantedById(Integer id);

    void cumsumRewardRecord(RewardRecord rewardRecord);

    Integer getInServiceRewards();

    /**
     * 获取是否已发放
     *
     * @param id
     * @return
     */
    Byte getIsGrantedById(Integer id);

    /**
     * 发放营销款账号
     * @return
     */
    T6101 getCustodyAccount(@Param("userId") int userId, @Param("accountCode")String accountCode);

    /**
     * 总要求发送数
     * @param recordIds
     * @return
     */
    List<Map<String, Object>> getRecordCount(@Param("recordIds") List<Integer> recordIds);

    /**
     * 总要求发送金额
     * @param recordIds
     * @return
     */
    List<Map<String, Object>> getRecordSum(@Param("recordIds") List<Integer> recordIds);

    /**
     * 查询新网请求状态
     * @param recordId
     * @return
     */
    List<Map<String, Object>> getXwRechargeState(int recordId);
}
