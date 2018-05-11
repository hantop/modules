package com.fenlibao.service.pms.common.publicity;

import com.fenlibao.model.pms.common.publicity.KnowEarly;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */
public interface KnowEarlyService {

    /**
     * 风险早知道列表
     * @param KnowEarly
     * @param bound
     * @param startTime
     * @param endTime
     * @return
     */
    List<KnowEarly> getKnowEarlyList(KnowEarly KnowEarly, RowBounds bound, String startTime, String endTime);

    /**
     * 保存或更新风险早知道
     * @param KnowEarly
     */
    void editKnowEarly(KnowEarly KnowEarly);

    /**
     * 批量删除风险早知道
     * @param idList
     */
    void deleteKnowEarly(String[] idList);

    /**
     * 根据多个id获取风险早知道列表
     * @param idList
     * @return
     */
    List<KnowEarly> getKnowEarlyListByIds(String[] idList);

    void updateAllPicUploade(KnowEarly knowEarly);
}
