package com.fenlibao.p2p.service.district;

import java.util.List;
import java.util.Map;

/**
 * 省份城市
 * Created by chenzhixuan on 2015/8/24.
 */
public interface DistrictService {

    /**
     * 获取所有省份
     */
    List<Map<String, String>> getAllProivce() throws Throwable;

    /**
     * 根据省ID获取市
     * @param provinceId
     * @return
     * @throws Throwable
     */
    List<Map<String, String>> getCity(int provinceId) throws Throwable;


//    /**
//     * 根据市ID获取县
//     * @param cityId
//     * @return
//     * @throws Throwable
//     */
//    List<Map<String, String>> getCounty(int cityId) throws Throwable;
//
//    /**
//     * 根据县ID和类型获取县信息
//     * @param type
//     * @param countyId
//     * @return
//     */
//    CountyVO getCounty(String type, int countyId) throws Throwable;
    
    /**
     * 根据城市编码获取省编码
     * @param cityCode
     * @return
     */
    String getProvinceCodeByCityCode(String cityCode);
    
}
