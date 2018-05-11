package com.fenlibao.dao.pms.da.reward.cashRedPacket;

import com.fenlibao.model.pms.da.finance.ReplacementRecharge;
import com.fenlibao.model.pms.da.reward.BackVoucherGrantStatistics;
import com.fenlibao.model.pms.da.reward.UserCashRedPacket;
import com.fenlibao.model.pms.da.reward.UserRedpackets;
import com.fenlibao.model.pms.da.reward.form.UserRedpacketsForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Set;

public interface UserRedpacketsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRedpackets record);

    int insertSelective(UserRedpackets record);

    UserRedpackets selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRedpackets record);

    int updateByPrimaryKey(UserRedpackets record);

    List<UserRedpackets> findPager(UserRedpackets userRedpackets, RowBounds bounds);

    /**
     * 获取所有指定记录
     * @param userRedpackets
     * @return
     */
    List<UserRedpackets> findAllActivityCode(UserRedpackets userRedpackets);

    /**
     * 批量插入
     * @param insertList
     * @return
     */
    int batchInsert(Set<UserRedpackets> insertList);

    List<UserRedpackets> findAll(UserRedpackets userRedpackets);
    
    int batchUpdateUserRedpacket(List<UserRedpackets> userRedpacketsList);
    
    List<UserCashRedPacket> getCashRedPacketRecordsDetail(UserCashRedPacket userCashRedPacket,RowBounds bounds);
    
    Integer insertCashRedPacketRecordDetail(List<UserCashRedPacket> insertList);

    Integer batchUpdateCashRedPacketRecordDetail(List<UserCashRedPacket> userCashRedPacketList);
    
    List<BackVoucherGrantStatistics> backVoucherGrantStatistics(Integer grantId);

    List<UserRedpackets> getRedPacketActivateCode(UserRedpackets item);

    Integer getInServiceRedpackets();

    List<UserRedpackets> findAllReport(UserRedpacketsForm userRedpackets);

    /**
     * 新网请求状态
     * @param requestId
     * @return
     */
    String getXWRequestState(int requestId);

    /**
     * 批量插入代充值列表
     * @param insertList
     * @return
     */
    Integer batchInsertReplacementRecharge(List<ReplacementRecharge> insertList);
}