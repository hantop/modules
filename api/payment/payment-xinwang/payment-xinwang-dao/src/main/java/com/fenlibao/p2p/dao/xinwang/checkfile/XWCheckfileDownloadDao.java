package com.fenlibao.p2p.dao.xinwang.checkfile;

import com.fenlibao.p2p.model.xinwang.checkfile.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/16.
 */
public interface XWCheckfileDownloadDao {
    void insertCheckfileRecharge(CheckfileRecharge cr);
    void insertCheckfileWithdraw(CheckfileWithdraw cw);
    void insertCheckfileCommission(CheckfileCommission cc);
    void insertCheckfileUser(CheckfileUser cu);
    void insertCheckfileBackrollRecharge(CheckfileBackrollRecharge cbr);
    void insertCheckfileTransaction(CheckfileTransaction ct);

    /**
     * 插入一条对账数据
     * @param cfds
     */
    void insertCheckFileStatus(CheckfileDateStatus cfds);

    CheckfileDateStatus getCheckFileStatus(CheckfileDateStatus cfds);
    void updateCheckFileStatus(CheckfileDateStatus cfds);

    List<Map<String,Object>> getCheckDiffData(String dateString);

    List<Map<String,Object>> getCheckRechargeDiffData(String dateString);

}
