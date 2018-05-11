package com.fenlibao.p2p.controller.v_3.v_3_2_0.umeng;

import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.UmengService;
import com.fenlibao.p2p.service.common.RedisService;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/24.
 */
@RestController("v_3_2_0/UmengContoller")
@RequestMapping(value = "umeng", headers = APIVersion.v_3_2_0)
public class UmengContoller {

    private static final Logger logger= LogManager.getLogger(UmengContoller.class);



    @Resource
    private RedisService redisService;



    @RequestMapping(value = "saveDeviceToken",method= RequestMethod.POST)
    HttpResponse saveDeviceToken(@ModelAttribute BaseRequestForm paramForm,
                                 @RequestParam(required = false, value = "token") String token,
                                 @RequestParam(required = false, value = "userId") String userId,
                                 @RequestParam(required = false, value = "deviceToken") String deviceToken) throws Throwable{
        HttpResponse response = new HttpResponse();
        if (!paramForm.validate()) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(), ResponseCode.EMPTY_PARAM.getMessage());
            return response;
        }
        this.redisService.setRedis("umeng:user:"+userId,deviceToken+"#"+paramForm.getClientType(), RedisConst.$DEVICE_TOKEN_TIMEOUT);
        return response;
    }

}
