package com.fenlibao.service.pms.common.screentype;

import com.fenlibao.model.pms.common.screentype.ScreenType;

import java.util.List;
import java.util.Map;

/**
 * 分辨率类型
 * <p>
 * Created by chenzhixuan on 2016/5/17.
 */
public interface ScreenTypeService {
    /**
     * 根据分辨率类型ID获取客户端类型code
     *
     * @param screenTypeId
     * @return
     */
    Integer getClientTypeCodeByScreenTypeId(byte screenTypeId);

    /**
     * 以客户端类型为Key的Map
     * @param screenTypes
     * @return
     */
    Map<String, List<ScreenType>> buildClientTypeCodeMap(List<ScreenType> screenTypes);

    List<ScreenType> getScreenTypes();

    /**
     * 根据客户端类型编码获取分辨率类型
     *
     * @param clientTypeCode
     * @return
     */
    List<ScreenType> getScreenTypesByClientTypeCode(String clientTypeCode);


    /**
     * 根据客户端类型ID获取分辨率类型
     *
     * @param clientTypeId
     * @return
     */
    List<ScreenType> getScreenTypesByClientTypeId(Integer clientTypeId);

    /**
     * 根据编码获取分辨率类型
     *
     * @param code
     * @return
     */
    ScreenType getScreenTypeByCode(String code);

    /**
     * 根据ID获取分辨率类型
     *
     * @param id
     * @return
     */
    ScreenType getScreenTypeById(Integer id);
}
