package com.fenlibao.p2p.service.xinwang.checkfile;


import com.fenlibao.p2p.model.xinwang.checkfile.CheckfileDateStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/18.
 */
public interface XWCheckfileDownloadService {
    void insertCheckfile(String tempString, String fileName) throws Exception;
    void downloadCheckFiles(String fileDate)throws Exception;
    void confirmCheckFile(String fileDate)throws Exception;
    void postXinwang(String fileDate)throws  Exception;
    void insertCheckFileStatus(CheckfileDateStatus cfs);
    void updateCheckFileStatus(CheckfileDateStatus cfs);
    CheckfileDateStatus getCheckFileStatus(CheckfileDateStatus cfs);

    List<Map<String,Object>> getCheckWithDrawDiffData(String dateString);

    List<Map<String,Object>> getCheckRechargeDiffData(String dateString);


}
