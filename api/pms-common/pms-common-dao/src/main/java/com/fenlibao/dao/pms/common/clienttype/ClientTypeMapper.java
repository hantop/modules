package com.fenlibao.dao.pms.common.clienttype;

import com.fenlibao.model.pms.common.clienttype.ClientType;

import java.util.List;

/**
 * 客户端类型
 * <p>
 * Created by chenzhixuan on 2016/5/17.
 */
public interface ClientTypeMapper {
    List<ClientType> getClientTypes();

    ClientType getClientTypeByCode(String code);

    ClientType getClientTypeById(Integer code);
}
