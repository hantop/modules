package com.fenlibao.p2p.controller.v_4.v_4_2_0.client;

import com.fenlibao.p2p.model.enums.VersionTypeEnum;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.AdImageVo;
import com.fenlibao.p2p.service.DeviceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController("v_4_2_0/DeviceController")
@RequestMapping(value = "device", headers = APIVersion.v_4_2_0)
public class DeviceController {

    private static final Logger logger = LogManager.getLogger(DeviceController.class);

    @Resource
    private DeviceService deviceService;

    /**
     * 设备启动页
     *
     * @return
     */
    @RequestMapping(value = "splash", method = RequestMethod.GET)
    HttpResponse splash(@ModelAttribute BaseRequestForm paramForm) {
        logger.info(paramForm.toString());
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate()) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM);
                return response;
            }

            String picName = this.deviceService.getStartpageImage(paramForm.getScreenType(), paramForm.getClientType());
            response.getData().put("splashImgUrl", picName);
        } catch (Exception ex) {
            response.setCodeMessage(ResponseCode.FAILURE);
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
    @RequestMapping(value = {"advert/image", "banner/image"}, method = RequestMethod.GET)
    HttpResponse adImg(HttpServletRequest request, @ModelAttribute BaseRequestForm paramForm,
                       @RequestParam(required = false, value = "advertType") String advertType
            , VersionTypeEnum versionType) {
        logger.info(paramForm.toString());
        HttpResponse response = new HttpResponse();
        try {
            if (!paramForm.validate() || versionType == null) {
                response.setCodeMessage(ResponseCode.EMPTY_PARAM);
                return response;
            }

            String version = request.getHeader("version");
            
            List<AdImageVo> list = this.deviceService.getAdimgList(
                    paramForm.getScreenType(), paramForm.getClientType(),
                    advertType,version,versionType==null?1:versionType.getIndex());
            response.getData().put("adList", list);
        } catch (Exception ex) {
            response.setCodeMessage(ResponseCode.FAILURE);
            logger.error("[DeviceController.adImg]" + ex.getMessage(), ex);
        }
        return response;
    }

}
