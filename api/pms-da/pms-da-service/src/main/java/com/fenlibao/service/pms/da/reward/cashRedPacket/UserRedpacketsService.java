package com.fenlibao.service.pms.da.reward.cashRedPacket;

import com.fenlibao.model.pms.da.reward.BackVoucherGrantStatistics;
import com.fenlibao.model.pms.da.reward.RewardRecord;
import com.fenlibao.model.pms.da.reward.UserCashRedPacket;
import com.fenlibao.model.pms.da.reward.UserRedpackets;
import com.fenlibao.model.pms.da.reward.form.UserRedpacketsForm;
import com.fenlibao.service.pms.da.exception.ExcelException;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface UserRedpacketsService {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRedpackets record);

    int insertSelective(UserRedpackets record);

    UserRedpackets selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRedpackets record);

    int updateByPrimaryKey(UserRedpackets record);

    List<UserRedpackets> findPager(UserRedpackets userRedpackets, RowBounds bounds);

    /**
     * 获取指定记录的所有红包记录
     *
     * @param userRedpackets
     * @return
     */
    List<UserRedpackets> findAllActivityCode(UserRedpackets userRedpackets);

    /**
     * 插入导入的返现券记录和流水记录
     *
     * @param newRecord
     * @param list
     * @return
     */
    List<String> insertSelective(RewardRecord newRecord, List<String[]> list) throws ExcelException;

    List<UserRedpackets> findAll(UserRedpackets userRedpackets);

    /**
     * 发放返现券
     *
     * @param rewardRecord
     */
    String grantBackVoucher(RewardRecord rewardRecord, UserRedpackets userRedpackets) throws Exception;

    /**
     * 查询现金红包发放详情
     */
    List<UserCashRedPacket> getCashRedPacketRecordsDetail(UserCashRedPacket userCashRedPacket, RowBounds bounds);

    List<String> importCashRedPacketRecords(RewardRecord newRecord, List<String[]> detailList);

    String grantCashRedPacket(RewardRecord rewardRecord) throws Exception;

    String cancelBackVoucher(RewardRecord rewardRecord, UserRedpackets userRedpacket) throws Exception;

    String cancelCashRedPacket(RewardRecord rewardRecord) throws Exception;
    
    List<BackVoucherGrantStatistics> backVoucherGrantStatistics(Integer grantId);

    List<UserRedpackets> findUserRedpacketsAll(RewardRecord rewardRecord, UserRedpackets userRedpackets);

    RewardRecord grantBackVoucherResult(RewardRecord rewardRecord, List<UserRedpackets> userRedpacketses);

    Byte selectGrantedById(Integer id);

    void updateRewardRecord(RewardRecord rewardRecord);

    RewardRecord getRewardRecordById(RewardRecord rewardRecord);

    /**
     * 根据code 导入报表信息和手机号码获取红包编码
     * @return
     */
    List<UserRedpackets> getRedPacketActivateCode(UserRedpackets item);

    //查询当前是否有在发放的红包
    Integer getInServiceRedpackets();

    List<UserRedpackets> findAllReport(UserRedpacketsForm userRedpackets);

    List<UserCashRedPacket> getCustodyCashRedPacket(RewardRecord rewardRecord) throws Exception;

    /**
     * 发放存管版本现金红包
     * @param rewardRecord
     * @return
     * @throws Exception
     */
    void doAlternativeRechargeReward(RewardRecord rewardRecord,  List<UserCashRedPacket> userCashRedPackets) throws Exception;

    String modifyLocalDetail(RewardRecord rewardRecord, List<UserCashRedPacket> userCashRedPacketList) throws Exception;
}