package com.fenlibao.p2p.controller.v_1.v_1_0_0.bid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseEnum;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.service.bid.BidExtendService;

/**
 * Created by LouisWang on 2015/8/15.
 */
@RestController("v_1_0_0/KDBbidController")
@RequestMapping("kdb")
public class KDBbidController {
    private static final Logger logger= LogManager.getLogger(KDBbidController.class);
    @Resource
    private BidExtendService bidEtService;
    /**
     *  开店宝查询标的申购记录
     * /bid/bidRecords
     */
    @RequestMapping(value = "bidRecords", method = RequestMethod.GET,headers = APIVersion.V_1_0_0)
    HttpResponse bidRecords(@ModelAttribute BaseRequestForm paramForm,
                            @RequestParam(required = true, value = "kdbPlantId") String kdbPlantId,
                            @RequestParam(required = false, value = "timestamp") String timestamp,
                            @RequestParam(required = true, value = "isUp") String isUp     //必须
    ) {
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate()
                || StringUtils.isBlank(kdbPlantId)
                || StringUtils.isBlank(isUp)){
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
            return apiResponse;
        }

        Map<String,Object> mapData = new HashMap<String, Object>();
        try {
            List<InvestRecordsVO> investList = null;
            investList = bidEtService.getBidInvestRecords(kdbPlantId,timestamp,isUp);
            mapData.put("kdbPlansgList",investList);
            apiResponse.setData(mapData);
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(),ResponseEnum.RESPONSE_SERVER_ERROR.getMessage() );
        }
        return apiResponse;
    }
}
