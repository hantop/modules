package com.fenlibao.service.pms.common.base.impl;

import com.fenlibao.dao.pms.common.base.EnumMapper;
import com.fenlibao.model.pms.common.global.TEnum;
import com.fenlibao.service.pms.common.base.EnumService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Lullaby on 2015-09-22 15:41
 */
@Service
public class EnumServiceImpl implements EnumService {

    @Resource
    private EnumMapper enumMapper;

    public List<TEnum> getEnum(String tableName) {
        return enumMapper.getEnumByTableName(tableName);
    }

    public List<TEnum> getEnum(String tableName, String columnName) {
        return enumMapper.getEnumByTableNameAndColumnName(tableName, columnName);
    }

    public String getEnumValue(String tableName, String columnName, String enumKey) {
        return enumMapper.getEnumValue(tableName, columnName, enumKey);
    }

    public List<TEnum> getEnumList(TEnum tenum, RowBounds bounds) {
        return enumMapper.getEnumList(tenum, bounds);
    }

    public TEnum getEnumById(int id) {
        return enumMapper.getEnumById(id);
    }

    public int updateEnum(TEnum tenum) {
        return enumMapper.updateEnum(tenum);
    }

    public int addEnum(TEnum tenum) {
        return enumMapper.addEnum(tenum);
    }

    public int deleteEnum(String[] idArray) {
        return enumMapper.deleteEnum(idArray);
    }

}
