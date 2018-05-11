package com.fenlibao.service.pms.da.marketing.tmr;

import com.fenlibao.model.pms.da.marketing.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Louis Wang on 2016/3/9.
 */

public interface TMRPerformanceService {

    /**
     * 插入电销的通话记录和电销记录
     * @param tmr
     * @return
     * @throws Exception
     */
    Integer createTMRPerformance(TMRPerformanceVO tmr, List<String[]> importList);

    /**
     * 查询导入的报表信息
     * @return
     * @throws Exception
     */
    List<TMRPerformanceVO> getTMRPerformanceList(TMRPerformanceForm tmrPerformanceForm, RowBounds bounds);

    /**
     * 计算每条导入数据的电销业绩
     * @return
     * @throws Exception
     */
    String calculatePerformance(Integer id) throws Exception;

    /**
     * 获取投资用户的信息
     * @return
     * @throws Exception
     */
    List<TMRInvestUserVO> getTMRInvesterList(TMRInvestUserForm investUserForm, RowBounds bounds);

    /**
     * 删除营销报表数据
     *
     * @param id
     * @return
     * @throws Throwable
     */
    void deletePerformance(Integer id);

    /**
     * 获取电销人员信息
     *
     * @param id
     * @return
     * @throws Throwable
     */
    TMRPerformanceVO getTMRinfo(Integer id);

    /**
     * 查询是否已经导入相同文件
     *
     * @param fileName
     * @return
     * @throws Throwable
     */
    TMRPerformanceVO findRecordByFileName(String fileName);

    List<TMRInvestUserVO> getTMRInvesterTotal(TMRInvestUserForm investUserForm, RowBounds rowBounds);

    List<TMRExcelVO> troubleUserInfo(Integer tmrId, RowBounds bounds);
}
