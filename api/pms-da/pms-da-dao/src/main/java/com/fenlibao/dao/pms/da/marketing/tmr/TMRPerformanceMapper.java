package com.fenlibao.dao.pms.da.marketing.tmr;

import com.fenlibao.model.pms.da.marketing.TMRExcelVO;
import com.fenlibao.model.pms.da.marketing.TMRInvestUserVO;
import com.fenlibao.model.pms.da.marketing.TMRPerformanceVO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2016/3/9.
 */

public interface TMRPerformanceMapper {

    Integer createTMRPerformance(TMRPerformanceVO tmr) ;

    void saveTMRCallTelList(List<TMRExcelVO> tmrList);

    List<TMRPerformanceVO> getTMRPerformanceList(Map<String, Object> paramMap, RowBounds bounds);

    List<TMRExcelVO> getImportDataById(Integer id);

    List<TMRInvestUserVO> findUserInvestBehavior(Map<String, Object> paramMap);

    void saveUserInvestBehavior(List<TMRInvestUserVO> investUserList);

    List<TMRInvestUserVO> getTMRInvesterList(Map<String, Object> paramMap, RowBounds bounds);

    void deletePerformance(Integer id);

    TMRPerformanceVO getTMRinfo(Integer id);

    void updateTMRInfoDispose(Integer id);

    TMRPerformanceVO findRecordByFileName(String fileName);

    List<TMRInvestUserVO> getCashBack(Map<String, Object> cashBackMap);

    List<TMRInvestUserVO> getTMRInvesterTotal(Map<String, Object> paramMap, RowBounds rowBounds);

    List<TMRExcelVO> troubleDeals(Map<String, Object> paramMap, RowBounds bounds);
}
