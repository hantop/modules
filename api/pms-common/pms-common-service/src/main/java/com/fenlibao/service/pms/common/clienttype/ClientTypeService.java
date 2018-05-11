package com.fenlibao.service.pms.common.clienttype;

import com.fenlibao.model.pms.common.clienttype.ClientType;

import java.util.List;

/**
 * 客户端类型
 * <p>
 * Created by chenzhixuan on 2016/5/17.
 */
public interface ClientTypeService {

    List<ClientType> getClientTypes();

    ClientType getClientTypeByCode(String code);

    ClientType getClientTypeById(Integer id);
}
