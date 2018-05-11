package com.fenlibao.p2p.controller.v_4.v_4_0_0.redpacket;

import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.invite.InviteUrlInfoVO_131;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.bid.TenderShareService;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("v_4_0_0/RedpacketController")
@RequestMapping(value = "redpacket", headers = APIVersion.v_4_0_0)
public class RedpacketController {
    private static final Logger logger= LogManager.getLogger(RedpacketController.class);
    @Resource
    private RedpacketService redpacketService;
    @Resource
    private TenderShareService tenderShareService;
    @Resource
    private UserInfoService userInfoService;

    /**
     * @author wangyunjing
     * @date 20151016  根据条件获取返现红包的内容
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "getredpacket", method = RequestMethod.GET)
    public HttpResponse getRedPacket(
            @ModelAttribute BaseRequestFormExtend paramForm,String pageNo,String pagesize,
            String type,     //查询红包类型(1:返现红包)
            String status,     //查询红包的状态(1:未使用 2:已使用  3:已过期)
            String bidId,
            String planId,
            Integer newPlan
    ) {
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate() || !StringUtils.isNoneBlank(pageNo, type, status)){
            apiResponse.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return apiResponse;
        }

        //不能同时使用
        if(StringUtils.isNotBlank(bidId) && StringUtils.isNotBlank(planId)){
            apiResponse.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
            return apiResponse;
        }

        Map<String,Object> mapData = new HashMap<String, Object>();
        try {
            int offset = 0, _pagesize = InterfaceConst.PAGESIZE;
            if (!StringUtils.isBlank(pagesize)) {
                _pagesize = Integer.valueOf(pagesize);
                if (_pagesize < 1) {
                    _pagesize = InterfaceConst.PAGESIZE;
                }
            }
            if (!StringUtils.isBlank(pageNo)) {
                offset = Integer.valueOf(pageNo);
                if (offset < 1) {
                    offset=1;
                }
                offset = (offset - 1) * _pagesize;
            }
            //根据条件查询返现红包
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", paramForm.getUserId());
            params.put("status", status);
            params.put("type", type);
            params.put("bidId", bidId);
            params.put("planId", planId);
            params.put("newPlan", newPlan);
            params.put("offset", offset);
            params.put("pagesize", _pagesize);
            List<UserRedPacketInfo> userRedPacketInfos = redpacketService.getRedpackets(params);
            mapData.put("list", userRedPacketInfos);
            apiResponse.setData(mapData);
        } catch (BusinessException busi) {
            apiResponse.setCodeMessage(busi);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            apiResponse.setCodeMessage(ResponseCode.FAILURE);
        }
        return apiResponse;
    }

    /**
     * @author wangyunjing
     * @date 20151022  获取登录用户可以使用的红包数量
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "redpacketcount", method = RequestMethod.GET)
    public HttpResponse redpacketcount(
            @ModelAttribute BaseRequestFormExtend paramForm, String bidId,
            String status     //查询红包的状态(1:未使用 2:已使用  3:已过期)
    ) {
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate()){
            apiResponse.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return apiResponse;
        }

        Map<String,Object> mapData = new HashMap<String, Object>();
        try {
            //根据条件查询用户可以使用的红包
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("userId",paramForm.getUserId());
            params.put("status",status);
            params.put("bidId",bidId);
            int redpacketCount = redpacketService.getRedpacketCount(params);
            mapData.put("count", redpacketCount);
            apiResponse.setData(mapData);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            apiResponse.setCodeMessage(ResponseCode.FAILURE );
        }
        return apiResponse;
    }

    /**
     * 投资分享红包
     * @param params
     * @param bidId
     * @return
     */
    @RequestMapping(value = "share", method = RequestMethod.POST)
    public HttpResponse share(BaseRequestFormExtend params, String bidId, int itemType,String newPlan) {
        HttpResponse response = new HttpResponse();
        if (!params.validate() || StringUtils.isBlank(bidId)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        try {
            int userId = params.getUserId();
            UserInfo userInfo = userInfoService.getUser(null, null, userId+"");
            if (userInfo == null) {
                throw new BusinessException(ResponseCode.USER_NOT_EXIST);
            }
            if (newPlan!=null&&newPlan.equals("1")) {
                itemType = 3;
            }

            InviteUrlInfoVO_131 inviteUrlVO = tenderShareService.addTenderShare(userId,Integer.valueOf(bidId),itemType);
            response.setData(CommonTool.toMap(inviteUrlVO));
        } catch (BusinessException busi) {
            response.setCodeMessage(busi);
            logger.warn("投资分享红包失败,userId=[{}],bidId=[{}],msg=[{}]", params.getUserId(), bidId, busi.getMessage());
        } catch (Exception e) {
            response.setCodeMessage(ResponseCode.FAILURE);
            logger.error("投资分享红包失败,userId=[{}],bidId=[{}]", params.getUserId(), bidId);
            logger.error("投资分享红包失败", e);
        }
        return response;
    }

    /**
     * 领取投资分享红包
     * @param params
     * @param phoneNum
     * @param redEnvelopeCode
     * @return
     */
    @RequestMapping(value = "receive", method = RequestMethod.POST)
    public HttpResponse receive(BaseRequestForm params, String phoneNum, String redEnvelopeCode) {
        HttpResponse response = new HttpResponse();
        if (!params.validate() || !StringUtils.isNoneBlank(phoneNum, redEnvelopeCode)) {
            response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
        }
        if (!Validator.isMobile(phoneNum)) {
            response.setCodeMessage(ResponseCode.COMMON_PHONE_FORMAT_WRONG);
            return response;
        }
        try {
            Map<String, Object> data = tenderShareService.addReceiveRecord(phoneNum, redEnvelopeCode);
            response.setData(data);
        } catch (BusinessException busi) {
            response.setCodeMessage(busi);
            logger.warn("领取投资分享红包失败,phoneNum=[{}],redEnvelopeCode=[{}],msg=[{}]", phoneNum, redEnvelopeCode, busi.getMessage());
        } catch (Exception e) {
            response.setCodeMessage(ResponseCode.FAILURE);
            logger.error("领取投资分享红包失败,phoneNum=[{}],redEnvelopeCode=[{}]", phoneNum, redEnvelopeCode);
            logger.error("领取投资分享红包失败", e);
        }
        return response;
    }
}
