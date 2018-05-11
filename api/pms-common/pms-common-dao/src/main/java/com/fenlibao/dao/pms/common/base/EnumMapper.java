package com.fenlibao.dao.pms.common.base;

import com.fenlibao.model.pms.common.global.TEnum;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Lullaby on 2015-09-22 15:44
 */
public interface EnumMapper {

    List<TEnum> getEnumByTableName(String tableName);

    List<TEnum> getEnumByTableNameAndColumnName(String tableName, String columnName);

    String getEnumValue(String tableName, String columnName, String enumKey);

    List<TEnum> getEnumList(TEnum tenum, RowBounds bounds);

    TEnum getEnumById(int id);

    int updateEnum(TEnum tEnum);

    int addEnum(TEnum tEnum);

    int deleteEnum(String[] idArray);

}
