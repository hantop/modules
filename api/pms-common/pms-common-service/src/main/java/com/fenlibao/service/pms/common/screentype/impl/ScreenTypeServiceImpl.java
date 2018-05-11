package com.fenlibao.service.pms.common.screentype.impl;

import com.fenlibao.dao.pms.common.screentype.ScreenTypeMapper;
import com.fenlibao.model.pms.common.screentype.ScreenType;
import com.fenlibao.service.pms.common.screentype.ScreenTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhixuan on 2016/5/17.
 */
@Service
public class ScreenTypeServiceImpl implements ScreenTypeService {
    @Resource
    private ScreenTypeMapper screenTypeMapper;

    @Override
    public Integer getClientTypeCodeByScreenTypeId(byte screenTypeId) {
        return screenTypeMapper.getClientTypeCodeByScreenTypeId(screenTypeId);
    }

    @Override
    public Map<String, List<ScreenType>> buildClientTypeCodeMap(List<ScreenType> screenTypes) {
        Map<String, List<ScreenType>> map = new HashMap<>();
        List<ScreenType> tempList = null;
        String tempClienttypeCode = null;
        for (ScreenType o : screenTypes) {
            tempList = new ArrayList<>();
            tempClienttypeCode = o.getClienttypeCode();
            for (ScreenType o2 : screenTypes) {
                if (o2.getClienttypeCode().equals(tempClienttypeCode)) {
                    tempList.add(o2);
                }
            }
            map.put(tempClienttypeCode, tempList);
        }
        return map;
    }

    @Override
    public List<ScreenType> getScreenTypes() {
        return screenTypeMapper.getScreenTypes();
    }

    public List<ScreenType> getScreenTypesByClientTypeCode(String clientTypeCode) {
        return screenTypeMapper.getScreenTypesByClientTypeCode(clientTypeCode);
    }

    @Override
    public List<ScreenType> getScreenTypesByClientTypeId(Integer clientTypeId) {
        return screenTypeMapper.getScreenTypesByClientTypeId(clientTypeId);
    }

    public ScreenType getScreenTypeByCode(String code) {
        return screenTypeMapper.getScreenTypeByCode(code);
    }

    public ScreenType getScreenTypeById(Integer id) {
        return screenTypeMapper.getScreenTypeById(id);
    }

}