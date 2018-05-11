package com.fenlibao.dao.pms.common.publicity;

import com.fenlibao.model.pms.common.publicity.KnowEarly;
import com.fenlibao.model.pms.common.publicity.KnowMore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 网贷知多点
 * Created by Administrator on 2018/3/5.
 */
public interface KnowEarlyMapper {


    List<KnowEarly> getKnowEarlyList(@Param("knowEarly") KnowEarly knowEarly, RowBounds bound, @Param("startTime") String startTime, @Param("endTime") String endTime);

    void editKnowEarly(@Param("knowEarly") KnowEarly knowEarly);

    void deleteKnowEarly(@Param("idList") String[] idList);

    List<KnowEarly> getKnowEarlyListByIds(@Param("idList")String[] idList);

    void updateAllPicUploade(KnowEarly knowEarly);
}
