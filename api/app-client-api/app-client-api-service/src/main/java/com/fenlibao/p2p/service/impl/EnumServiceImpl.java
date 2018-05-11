package com.fenlibao.p2p.service.impl;

import com.fenlibao.p2p.dao.EnumDao;
import com.fenlibao.p2p.model.entity.TEnum;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.EnumService;
import com.fenlibao.p2p.util.loader.Config;
//import com.fenlibao.p2p.util.loader.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnumServiceImpl implements EnumService {
	
	@Resource
	private EnumDao enumDao;
	
	public List<TEnum> getEnum(String enumTable, String enumColumn) {
		return enumDao.getEnum(enumTable, enumColumn);
	}
	
	public HttpResponse addEnum(TEnum tenum) {
		HttpResponse response = new HttpResponse();
		int result = enumDao.addEnum(tenum);
		if (result < 1) {
			response.setMessage("添加枚举失败");
		}
		return response;
	}
	
	public HttpResponse updateEnum(TEnum tenum) {
		HttpResponse response = new HttpResponse();
		int result = enumDao.updateEnum(tenum);
		if (result < 1) {
			response.setCodeMessage("213", Config.get("213"));
		}
		return response;
	}
	
	public HttpResponse deleteEnum(String id) {
		HttpResponse response = new HttpResponse();
		int result = enumDao.deleteEnum(id);
		if (result < 1) {
			response.setCodeMessage(ResponseCode.FAILURE);
		}
		return response;
	}

	public HttpResponse testMultiDataSource() {
		HttpResponse apiResponse = new HttpResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", enumDao.testMultiDataSource());
		apiResponse.setData(map);
		return apiResponse;
	}

    @Transactional
    public int testTransaction() {
        TEnum tEnum1 = new TEnum();
        tEnum1.setId(20);
        tEnum1.setEnumValue("okay");
        int flag1 = enumDao.testTransaction1(tEnum1);
        TEnum tEnum2 = new TEnum();
        tEnum2.setId(21);
        tEnum2.setEnumValue("google");
        int flag2 = enumDao.testTransaction2(tEnum2);
        int result = (flag1 + flag2) / 0;
        return result;
    }

}
