package com.fenlibao.p2p.service;

import com.fenlibao.p2p.model.entity.TEnum;
import com.fenlibao.p2p.model.global.HttpResponse;

import java.util.List;

public interface EnumService {

	List<TEnum> getEnum(String enumTable, String enumColumn);

	HttpResponse addEnum(TEnum tenum);

	HttpResponse updateEnum(TEnum tenum);

	HttpResponse deleteEnum(String id);

	HttpResponse testMultiDataSource();

    int testTransaction();

}
