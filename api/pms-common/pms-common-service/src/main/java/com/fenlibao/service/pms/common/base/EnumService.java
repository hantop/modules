package com.fenlibao.service.pms.common.base;

import com.fenlibao.model.pms.common.global.TEnum;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Lullaby on 2015-09-22 15:41
 */
public interface EnumService {

    List<TEnum> getEnum(String tableName);

    List<TEnum> getEnum(String tableName, String columnName);

    String getEnumValue(String tableName, String columnName, String enumKey);

    List<TEnum> getEnumList(TEnum tenum, RowBounds bounds);

    TEnum getEnumById(int id);

    int updateEnum(TEnum tenum);

    int addEnum(TEnum tenum);

    int deleteEnum(String[] idArray);

}
