package com.fenlibao.p2p.dao.xinwang.common;

import com.fenlibao.p2p.model.xinwang.entity.common.XWCapitalFlow;
import com.fenlibao.p2p.model.xinwang.param.errorLog.ErrorLogParam;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */
public interface PTCommonDao {
    long insertT6123(Integer userId, String title, String state) ;
    void insertT6124(long letterId, String content) ;
    Integer insertT1040(Integer type, String content) ;
    void insertT1041(Integer msgId, String mobile) ;
    String getSystemVariable(String id) ;
    void insertT6102(XWCapitalFlow flow);
    Date getCurrentDate();
    void batchInsertT6102(List<XWCapitalFlow> list);
    void batchInsertTransactionExtend(List<XWCapitalFlow> list);

    /**
     * 获取平台特殊用户id
     * @return
     */
    int getSpecialUserId();

    /**
     * 插入错误日志
     */
    void insertErrorLog(ErrorLogParam errorLogParam);
}
