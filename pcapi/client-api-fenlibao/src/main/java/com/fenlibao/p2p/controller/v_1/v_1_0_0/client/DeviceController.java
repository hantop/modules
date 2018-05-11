package com.fenlibao.p2p.controller.v_1.v_1_0_0.client;

import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseEnum;
import com.fenlibao.p2p.model.vo.AdImageVo;
import com.fenlibao.p2p.model.vo.ApkUpdateVo;
import com.fenlibao.p2p.service.DeviceService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Config;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

@RestController("v_1_0_0/DeviceController")
@RequestMapping("device")
public class DeviceController {

    private static final Logger logger = LogManager.getLogger(DeviceController.class);

    @Resource
    private DeviceService deviceService;

    /**
     * 设备启动页
     *
     * @return
     */
    @RequestMapping(value = "splash", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse splash(@ModelAttribute BaseRequestForm paramForm) {
        logger.info(paramForm.toString());
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate()) {
                response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
                return response;
            }

            String picName = this.deviceService.getStartpageImage(paramForm.getScreenType(), paramForm.getClientType());
            response.getData().put("splashImgUrl", picName);
        } catch (Exception ex) {
            response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
            logger.error("[DeviceController.splash]" + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * 广告图接口
     * @param advertType  广告类型
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "advert/image", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse adImg(HttpServletRequest request,@ModelAttribute BaseRequestForm paramForm,
    		@RequestParam(required = false, value = "advertType") String advertType,VersionTypeEnum versionType) {
        logger.info(paramForm.toString());
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate()||versionType == null) {
                response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
                return response;
            }

            String version = request.getHeader("version");
            
            List<AdImageVo> list = this.deviceService.getAdimgList(paramForm.getScreenType(), paramForm.getClientType(),advertType,version,versionType==null?1:versionType.getIndex());
            response.getData().put("adList", list);
        } catch (Exception ex) {
            response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
            logger.error("[DeviceController.adImg]" + ex.getMessage(), ex);
        }
        return response;
    }

    /**
     * 客户端升级接口
     *
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "app", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    HttpResponse appUpdate(
            @ModelAttribute BaseRequestForm paramForm,
            @RequestParam(required = false, value = "versionCode") String versionCode,
            @RequestParam(required = false, value = "channelCode") String channelCode) {
        logger.info(paramForm.toString());
        HttpResponse response = new HttpResponse();
        try {
            if (StringUtils.isEmpty(versionCode)) {
                response.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(), ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
                return response;
            }
            if(StringUtils.isEmpty(channelCode)){//设置默认渠道
            	channelCode = Config.get("app.default.channelCode");
            }
            ApkUpdateVo vo = this.deviceService.getApk(versionCode,Integer.parseInt(paramForm.getClientType()),channelCode);
            response.setData(CommonTool.toMap(vo));
        } catch (Exception ex) {
            response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
            logger.error("[DeviceController.appUpdate]" + ex.getMessage(), ex);
        }
        return response;
    }

}
