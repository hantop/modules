package com.fenlibao.dao.pms.da.biz.xieyi;

import com.fenlibao.model.pms.da.biz.xieyi.XieyiInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/8/31.
 */
public interface XieyiMapper {
    
    List<XieyiInfo> getXieyis(RowBounds bounds);

    int deleteById(@Param("id") String id);

    int saveDownloadRecord(Map<String, Object> params);

    Map<String,Object> getPlatformUserNoByAccount(String account);

    int saveEntrustUpload(@Param("dId") Long id, @Param("name") String origName, @Param("reName") String reName, @Param("size") long size, @Param("format") String pdf, @Param("createTime") Date date, @Param("uploadUser") String uploadUser);

    int deleteByRecordId(@Param("id") String id);
}
