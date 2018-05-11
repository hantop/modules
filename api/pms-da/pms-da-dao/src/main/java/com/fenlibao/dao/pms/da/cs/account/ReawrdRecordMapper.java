package com.fenlibao.dao.pms.da.cs.account;

import com.fenlibao.model.pms.da.cs.account.ReawrdRecord;
import com.fenlibao.model.pms.da.cs.form.TransactionForm;
import com.fenlibao.model.pms.da.reward.UserRedpackets;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2016/1/4.
 */
public interface ReawrdRecordMapper {
    /**
     * 根据手机号分页获取奖励记录列表
     *
     * @param transaction
     *@param bounds  @return
     */
    List<ReawrdRecord> findReawrdRecord(TransactionForm transaction, RowBounds bounds);

    List<UserRedpackets> getUserRedpackets(TransactionForm transaction, RowBounds bounds);

    List<ReawrdRecord> getCashRedPackets(TransactionForm transaction, RowBounds bounds);

    List<ReawrdRecord> getUserExperienceGold(TransactionForm transaction, RowBounds bounds);

    List<ReawrdRecord> getUserRateCoupon(TransactionForm transaction, RowBounds bounds);
}
