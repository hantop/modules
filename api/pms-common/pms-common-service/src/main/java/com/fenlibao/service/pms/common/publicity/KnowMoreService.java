package com.fenlibao.service.pms.common.publicity;

import com.fenlibao.model.pms.common.publicity.KnowEarly;
import com.fenlibao.model.pms.common.publicity.KnowMore;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */
public interface KnowMoreService {

    /**
     * 网贷知多点列表
     * @param knowMore
     * @param bound
     * @param startTime
     * @param endTime
     * @return
     */
    List<KnowMore> getKnowMoreList(KnowMore knowMore, RowBounds bound, String startTime, String endTime);

    /**
     * 保存或者更新网贷知多点
     * @param knowMore
     */
    void editKnowMore(KnowMore knowMore);

    /**
     * 删除网贷知多点
     * @param idList
     */
    void deleteKnowMore(String[] idList);


    /**
     * 根据多个id获取风险早知道列表
     * @param idList
     * @return
     */
    List<KnowMore> getKnowEarlyListByIds(String[] idList);

    void updateAllPicUploade(KnowMore knowMore);
}
