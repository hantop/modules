package com.fenlibao.pms.controller.base;

import com.fenlibao.model.pms.common.global.TEnum;
import com.fenlibao.service.pms.common.base.EnumService;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lullaby on 2015/7/30.
 */
public class BaseController {

    @Resource
    private EnumService enumService;

    public List<TEnum> getEnum(String tableName) {
        return enumService.getEnum(tableName);
    }

    public List<TEnum> getEnum(String tableName, String columnName) {
        return enumService.getEnum(tableName, columnName);
    }

    public String getEnumValue(String tableName, String columnName, String enumKey) {
        return enumService.getEnumValue(tableName, columnName, enumKey);
    }

    public String getEnumValue(String enumColumn, Object enumKey, List<TEnum> enums) {
        String value = "";
        for (Iterator<TEnum> ite = enums.iterator(); ite.hasNext();) {
            TEnum item = ite.next();
            String columnName = item.getEnumColumn();
            String columnKey = item.getEnumKey();
            if (enumColumn.equals(columnName)) {
                if (enumKey != null && (enumKey.toString()).equals(columnKey)) {
                    value = item.getEnumValue();
                }
            }
        }
        return value;
    }

}
