package com.fenlibao.dao.pms.common.publicity;

import com.fenlibao.model.pms.common.publicity.KnowMore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 网贷知多点
 * Created by Administrator on 2018/3/5.
 */
public interface KnowMoreMapper {


    List<KnowMore> getKnowMoreList(@Param("knowMore") KnowMore knowMore, RowBounds bound, @Param("startTime") String startTime, @Param("endTime") String endTime);

    void editKnowMore(@Param("knowMore")KnowMore knowMore);

    void deleteKnowMore(@Param("idList")String[] idList);

    void updateAllPicUploade(KnowMore knowMore);

    List<KnowMore> getKnowEarlyListByIds(@Param("idList")String[] idList);
}
