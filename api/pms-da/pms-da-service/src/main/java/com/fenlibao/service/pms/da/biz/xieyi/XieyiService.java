package com.fenlibao.service.pms.da.cs.xieyi;

import com.fenlibao.model.pms.da.biz.xieyi.XieyiInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/8/31.
 */
public interface XieyiService {

    List<XieyiInfo> getXieyis(RowBounds bounds);

    boolean deleteById(String id);

    Map<String,Object> getPlatformUserNoByAccount(String account);

    Long saveXieyiInfo(String uploadUser, String origName, String reName, long size, String wtzfxy, Date yesterday, String s, String s1);
}
