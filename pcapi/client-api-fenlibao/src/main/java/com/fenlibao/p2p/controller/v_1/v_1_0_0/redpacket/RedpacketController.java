package com.fenlibao.p2p.controller.v_1.v_1_0_0.redpacket;

import com.fenlibao.p2p.model.entity.UserRedPacketInfo;
import com.fenlibao.p2p.model.entity.coupons.UserCouponStatisticsInfo;
import com.fenlibao.p2p.model.enums.bid.BidTypeEnum;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.model.vo.bidinfo.BidTypeVO;
import com.fenlibao.p2p.service.redpacket.RedpacketService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.paginator.domain.Pager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController("v_1_0_0/RedpacketController")
@RequestMapping("redpacket")
public class RedpacketController {
    private static final Logger logger= LogManager.getLogger(RedpacketController.class);
    @Resource
    private RedpacketService redpacketService;

    /**
     * @author wangyunjing
     * @date 20151016
     * @todo 根据条件获取返现红包的内容
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "getredpacket", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getRedPacket(
            @ModelAttribute BaseRequestFormExtend paramForm,
            Integer page,Integer limit,
            String type,     //查询红包类型(1:返现红包)
            String status,     //查询红包的状态(1:未使用 2:已使用  3:已过期)
            String bidId,
            String planId,
            Integer newPlan
    ) {
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate() || !StringUtils.isNoneBlank(type,status)
                ){
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
            return apiResponse;
        }

        //不能同时使用
        if(StringUtils.isNotBlank(bidId) && StringUtils.isNotBlank(planId)){
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(),ResponseEnum.RESPONSE_INVALID_PARAM.getMessage());
            return apiResponse;
        }

        if(page==null)page=1;
        if(limit==null)limit=InterfaceConst.PAGINATOR_PAGE_DEFAULT_LIMIT;
             			
        Map<String,Object> mapData = new HashMap<String, Object>();
        try {
        	
            //根据条件查询返现红包
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", paramForm.getUserId());
            params.put("status", status);
            params.put("type", type);
            params.put("bidId", bidId);
            params.put("planId", planId);
            params.put("newPlan", newPlan);
            params.put("offset", (page - 1) * limit);
            params.put("pagesize", limit);
            List<UserRedPacketInfo> userRedPacketInfos = redpacketService.getRedpackets(params);
//            mapData.put("list", userRedPacketInfos);
//            apiResponse.setData(mapData);
            Pager pager = new Pager();
            int totalCount = redpacketService.getRedpacketCount(params);
            pager.setTotalCount(totalCount);
            pager.setPage(page);
            pager.setLimit(limit);
            pager.setTotalPages(totalCount%limit==0?totalCount/limit:totalCount/limit+1);
            pager.setItems(userRedPacketInfos);
            apiResponse.setData(CommonTool.toMap(pager));
        
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
     * @date 20151022
     * @todo 获取登录用户可以使用的红包数量
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "redpacketcount", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse redpacketcount(
            @ModelAttribute BaseRequestFormExtend paramForm,
            String bidId, String status     //查询红包的状态(1:未使用 2:已使用  3:已过期)
    ) {
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate() || !StringUtils.isNoneBlank(bidId, status)){
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
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
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(),ResponseEnum.RESPONSE_SERVER_ERROR.getMessage() );
        }
        return apiResponse;
    }

    /**
     * @author wangyunjing
     * @date 20151016
     * @todo 根据条件获取返现红包的内容
     * @param paramForm
     * @return
     */
    @RequestMapping(value = "redpacketStatistics", method = RequestMethod.GET, headers = APIVersion.V_1_0_0)
    public HttpResponse getRedpacketStatistics(@ModelAttribute BaseRequestFormExtend paramForm) {
        HttpResponse apiResponse = new HttpResponse();
        //参数验证
        if(!paramForm.validate()){
            apiResponse.setCodeMessage(ResponseEnum.RESPONSE_EMPTY_PARAM.getCode(),ResponseEnum.RESPONSE_EMPTY_PARAM.getMessage());
            return apiResponse;
        }

        Map<String,Object> mapData = new HashMap<String, Object>();
        try {
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("userId",paramForm.getUserId());
            UserCouponStatisticsInfo ucsi = redpacketService.getRedpacketsStatistics(params);
            apiResponse.setData(CommonTool.toMap(ucsi));

        } catch (BusinessException busi) {
            apiResponse.setCodeMessage(busi);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            apiResponse.setCodeMessage(ResponseCode.FAILURE);
        }
        return apiResponse;
    }
}
