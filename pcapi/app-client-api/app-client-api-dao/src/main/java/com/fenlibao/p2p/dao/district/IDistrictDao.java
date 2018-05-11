package com.fenlibao.p2p.dao.district;

public interface IDistrictDao {

    /**
     * 根据城市编码获取省编码
     * @param cityCode
     * @return
     */
    String getProvinceCodeByCityCode(String cityCode);
	
}
