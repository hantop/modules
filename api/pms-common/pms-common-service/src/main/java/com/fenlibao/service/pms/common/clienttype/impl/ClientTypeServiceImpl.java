package com.fenlibao.service.pms.common.clienttype.impl;

import com.fenlibao.dao.pms.common.clienttype.ClientTypeMapper;
import com.fenlibao.model.pms.common.clienttype.ClientType;
import com.fenlibao.service.pms.common.clienttype.ClientTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户端类型
 * <p>
 * Created by chenzhixuan on 2016/5/17.
 */
@Service
public class ClientTypeServiceImpl implements ClientTypeService {
    @Resource
    private ClientTypeMapper clientTypeMapper;

    @Override
    public List<ClientType> getClientTypes() {
        return clientTypeMapper.getClientTypes();
    }

    @Override
    public ClientType getClientTypeByCode(String code) {
        return clientTypeMapper.getClientTypeByCode(code);
    }

    @Override
    public ClientType getClientTypeById(Integer id) {
        return clientTypeMapper.getClientTypeById(id);
    }
}
