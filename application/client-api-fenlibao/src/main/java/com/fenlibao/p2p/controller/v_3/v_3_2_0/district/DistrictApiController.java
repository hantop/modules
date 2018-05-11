package com.fenlibao.p2p.controller.v_3.v_3_2_0.district;

import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.district.DistrictService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省份城市Controller
 * Created by chenzhixuan on 2015/8/24.
 */
@RestController("v_3_2_0/DistrictApiController")
@RequestMapping(value = "district", headers = APIVersion.v_3_2_0)
public class DistrictApiController {
    private static final Logger logger= LogManager.getLogger(DistrictApiController.class);

    @Resource
    private DistrictService districtService;

    @RequestMapping(value = "provinces", method = RequestMethod.GET)
    HttpResponse getAllProvince(@ModelAttribute BaseRequestForm paramForm) {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()) {
            logger.info("request param is empty");
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
        } else {
            try {
                List<Map<String, String>> proivces = districtService.getAllProivce();
                Map<String, Object> data = new HashMap<>();
                data.put("list", proivces);
                response.setData(data);
            } catch (Throwable throwable) {
                logger.debug(throwable.getMessage());
                response.setCodeMessage(ResponseCode.FAILURE);
            }
        }
        return response;
    }

    @RequestMapping(value = "citys", method = RequestMethod.GET)
    HttpResponse getCitys(
            @ModelAttribute BaseRequestForm paramForm,
            @RequestParam(required = false, value = "provinceId") int provinceId) {
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()) {
            logger.info("request param is empty");
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
        } else {
            if (provinceId <= 0) {
                response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
            } else {
                try {
                    List<Map<String, String>> citys = districtService.getCity(provinceId);
                    Map<String, Object> data = new HashMap<>();
                    data.put("list", citys);
                    response.setData(data);
                } catch (Throwable throwable) {
                    logger.debug(throwable.getMessage());
                    response.setCodeMessage(ResponseCode.FAILURE);
                }
            }
        }
        return response;
    }

//    @RequestMapping(value = "countys", method = RequestMethod.GET)
//    HttpResponse getCountys(
//            @ModelAttribute BaseRequestForm paramForm,
//            @RequestParam(required = false, value = "cityId") int cityId) {
//        HttpResponse response = new HttpResponse();
//        if (!paramForm.validate()) {
//            logger.info("request param is empty");
//            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
//        } else {
//            if (cityId <= 0) {
//                response.setCodeMessage(ResponseEnum.RESPONSE_DISTRICT_ERROR.getCode(), "参数错误");
//            } else {
//                try {
//                    List<Map<String, String>> countys = districtService.getCounty(cityId);
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("list", countys);
//                    response.setData(data);
//                } catch (Throwable throwable) {
//                    logger.debug(throwable.getMessage());
//                    response.setCodeMessage(ResponseEnum.RESPONSE_DISTRICT_ERROR.getCode(), "获取县失败");
//                }
//            }
//        }
//        return response;
//    }
//
//    @RequestMapping(value = "county", method = RequestMethod.GET)
//    HttpResponse getCounty(
//            @ModelAttribute BaseRequestForm paramForm,
//            @RequestParam(required = false, value = "type") String type,
//            @RequestParam(required = false, value = "countyId") int countyId) {
//        HttpResponse response = new HttpResponse();
//        if (!paramForm.validate()
//                || StringUtils.isBlank(type)) {
//            logger.info("request param is empty");
//            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
//        } else {
//            if (countyId <= 0
//                    || (!type.equals("1"))) {
//                response.setCodeMessage(ResponseEnum.RESPONSE_DISTRICT_ERROR.getCode(), "参数错误");
//            } else {
//                try {
//                    CountyVO county = districtService.getCounty(type, countyId);
//                    response.setData(Jackson.getMapData(county));
//                } catch (Throwable throwable) {
//                    logger.debug(throwable.getMessage());
//                    response.setCodeMessage(ResponseEnum.RESPONSE_DISTRICT_ERROR.getCode(), "获取县失败");
//                }
//            }
//        }
//        return response;
//    }

}
