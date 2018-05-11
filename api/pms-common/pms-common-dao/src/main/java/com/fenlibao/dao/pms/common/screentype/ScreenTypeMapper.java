package com.fenlibao.dao.pms.common.screentype;

import com.fenlibao.model.pms.common.screentype.ScreenType;

import java.util.List;

/**
 * 分辨率类型
 * <p>
 * Created by chenzhixuan on 2016/5/17.
 */
public interface ScreenTypeMapper {

    Integer getClientTypeCodeByScreenTypeId(byte screenTypeId);

    List<ScreenType> getScreenTypes();

    List<ScreenType> getScreenTypesByClientTypeCode(String clientTypeCode);

    List<ScreenType> getScreenTypesByClientTypeId(Integer clientTypeId);

    ScreenType getScreenTypeByCode(String code);

    ScreenType getScreenTypeById(Integer id);

}
