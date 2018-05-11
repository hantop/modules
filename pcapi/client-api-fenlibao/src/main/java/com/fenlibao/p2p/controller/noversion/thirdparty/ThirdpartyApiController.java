package com.fenlibao.p2p.controller.noversion.thirdparty;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.model.consts.ThirdpartTypeConst;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseEnum;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.vo.UserThirdpartyVO;
import com.fenlibao.p2p.model.vo.WeixinStatusVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.thirdparty.ThirdpartyService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.loader.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenzhixuan on 2015/8/21.
 */
@RestController
public class ThirdpartyApiController {
    private static final Logger logger = LogManager.getLogger(ThirdpartyApiController.class);

    @Resource
    private ThirdpartyService thirdpartyService;
    @Resource
    private UserInfoService userInfoService;

    @RequestMapping(value = "weixin/bind", method = RequestMethod.POST)
    HttpResponse bind(@RequestParam(required = true, value = "openId") String openId,
                      @RequestParam(required = false, value = "phoneNum") String phoneNum,
                      @RequestParam(required = false, value = "username") String username,
                      @RequestParam(required = true, value = "password") String password) {
        HttpResponse response = new HttpResponse();
        
        try{
	        // 参数为空
	        if (!StringUtils.isNoneBlank(openId, password)
	                || (StringUtils.isBlank(phoneNum) && StringUtils.isBlank(username))) {
	            logger.info("request param is empty");
	            response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
	        } else {
	            if(StringUtils.isNotBlank(phoneNum) && StringUtils.isNotBlank(username)) {
	                response.setCodeMessage(Message.STATUS_213, "用户名和手机号不能同时存在");
	            } else {
	                Throwable throwable = null;
	                Map<String, Object> map = new HashMap<>();
	
	                if(StringUtils.isNotBlank(phoneNum)) {
	                    try{
	                        phoneNum = AES.getInstace().decrypt(phoneNum);
	                        map.put("phoneNum", phoneNum);
	                    } catch(Exception e) {
	                        throwable = e;
	                        response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), "手机号解密失败");
	                        return response;
	                    }
	                } else {
	                    try{
	                        username = AES.getInstace().decrypt(username);
	                        map.put("username", username);
	                    } catch(Exception e) {
	                        throwable = e;
	                        response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), "用户名解密失败");
	                        return response;
	                    }
	                }
	                try{
	                    password = AES.getInstace().decrypt(password);
	                } catch(Exception e) {
	                    throwable = e;
	                    response.setCodeMessage(ResponseEnum.RESPONSE_INVALID_PARAM.getCode(), "密码解密失败");
	                    return response;
	                }
	
	                if(throwable == null) {
	                    // 根据手机号或登陆名查询用户信息
	                    UserInfo userInfo = userInfoService.getUserInfoByPhoneNumOrUsername(map);
	                    if (userInfo == null) {
	                        response.setCodeMessage(ResponseEnum.RESPONSE_USERNAME_NOT_EXIST.getCode(), ResponseEnum.RESPONSE_USERNAME_NOT_EXIST.getMessage());
	                        return response;
	                    } else {
	                        // 密码进行AES解码进行加密后进行对比
	                        String pwd = userInfo.getPassword();
	                        if (PasswordCryptUtil.crypt(password).equals(pwd)) {
	                            String userId = userInfo.getUserId();
	                            // 第三方类型为微信(1)
	                            int isbind = thirdpartyService.isBindThirdparty(openId, userId, ThirdpartTypeConst.weixinType);
	                            if(isbind>0){
	                            	response.setCodeMessage(ResponseEnum.RESPONSE_WX_HAVE_BIND.getCode(), ResponseEnum.RESPONSE_WX_HAVE_BIND.getMessage());
	                                return response;
	                            }
	                            UserThirdpartyVO resultVO = thirdpartyService.bind(openId, userId, userInfo.getUsername(), ThirdpartTypeConst.weixinType);
	                            if(resultVO==null) {
	                                response.setCodeMessage(ResponseEnum.RESPONSE_WX_BIND_FAILED.getCode(), ResponseEnum.RESPONSE_WX_BIND_FAILED.getMessage());
	                                return response;
	                            }else{
	                            	try {
										response.setCodeMessage(ResponseEnum.RESPONSE_SUCCESS.getCode(), "绑定成功");
									} catch(Exception e){
										e.printStackTrace();
										response.setCodeMessage(ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getCode(), ResponseEnum.RESPONSE_INTERAL_SEVER_ERROR.getMessage());
									}
	                            	return response;
	                            }
	                        } else {
	                            response.setCodeMessage(Message.STATUS_1015, Message.get(Message.STATUS_1015));
	                            return response;
	                        }
	                    }
	                }
	            }
	        }
        }catch(Exception e){
        	response.setCodeMessage(ResponseEnum.RESPONSE_SERVER_ERROR.getCode(),ResponseEnum.RESPONSE_SERVER_ERROR.getMessage());
		}

        return response;
    }
    
//    @RequestMapping(value = "weixin/autoLogin", method = RequestMethod.POST)
//    HttpResponse autoLogin(@ModelAttribute BaseRequestForm paramForm,
//    				  @RequestParam(required = true, value = "openId") String openId) {
//        HttpResponse response = new HttpResponse();
//        if(StringUtils.isEmpty(openId)){
//            response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
//            return response;
//        }
//        try{
//	        int userId = thirdpartyService.isCanAutoLogin(openId,ThirdpartTypeConst.weixinType);
//	        if(userId>0){
//	        	UserAccountInfoVO userAccountInfoVO = thirdpartyService.getUserInfo(userId,paramForm.getClientType(),paramForm.getDeviceId());
//				response.setData(userInfoService.getUserAccountInfoDataMap(userAccountInfoVO, true));
//				response.setCodeMessage(ResponseEnum.RESPONSE_SUCCESS.getCode(),ResponseEnum.RESPONSE_SUCCESS.getMessage());
//				return response;
//	        }else{
//	        	response.setCodeMessage(ResponseEnum.RESPONSE_WX_NOT_BIND.getCode(),ResponseEnum.RESPONSE_WX_NOT_BIND.getMessage());
//	        	return response;
//	        }
//        }catch(Exception e){
//        	response.setCodeMessage(ResponseEnum.RESPONSE_FAILURE.getCode(),ResponseEnum.RESPONSE_FAILURE.getMessage());
//		}
//        return response;
//    }
    
    @RequestMapping(value = "weixin/isBind", method = RequestMethod.POST)		
    HttpResponse isBindWeixin(@ModelAttribute BaseRequestForm paramForm,
    				  @RequestParam(required = true, value = "openId") String openId) {
        HttpResponse response = new HttpResponse();
        if(StringUtils.isEmpty(openId)){
            response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
            return response;
        }
        try{
	        int isbind = thirdpartyService.isBindThirdpartyOpenId(openId, ThirdpartTypeConst.weixinType);
	        if(isbind>0){
	        	WeixinStatusVO weixinStatusVO = new WeixinStatusVO();
	        	weixinStatusVO.setWeixinStatus(Status.BINDED.name());
	        	weixinStatusVO.setWeixinMsg(Status.BINDED.getChineseName());
	        	response.setData(CommonTool.toMap(weixinStatusVO));
				response.setCodeMessage(ResponseEnum.RESPONSE_SUCCESS.getCode(),ResponseEnum.RESPONSE_SUCCESS.getMessage());
				return response;
	        }else{
	        	WeixinStatusVO weixinStatusVO = new WeixinStatusVO();
	        	weixinStatusVO.setWeixinStatus(Status.UNBIND.name());
	        	weixinStatusVO.setWeixinMsg(Status.UNBIND.getChineseName());
	        	response.setData(CommonTool.toMap(weixinStatusVO));
				response.setCodeMessage(ResponseEnum.RESPONSE_SUCCESS.getCode(),ResponseEnum.RESPONSE_SUCCESS.getMessage());
				return response;
	        }
        }catch(Exception e){
        	response.setCodeMessage(ResponseEnum.RESPONSE_FAILURE.getCode(),ResponseEnum.RESPONSE_FAILURE.getMessage());
		}
        return response;
    }
    
    @RequestMapping(value = "weixin/cancelAutoLogin", method = RequestMethod.POST)
    HttpResponse cancelAutoLogin(@RequestParam(required = true, value = "openId") String openId) {
    	HttpResponse response = new HttpResponse();
        if(StringUtils.isEmpty(openId) ){
            response.setCodeMessage(Message.STATUS_1013, Message.get(Message.STATUS_1013));
            return response;
        }
        try{
	        int flag = thirdpartyService.cancelAutoLogin(openId,ThirdpartTypeConst.weixinType);
	        if(flag>0){
	        	response.setCodeMessage(ResponseEnum.RESPONSE_WX_CANCEL_SUCCESS.getCode(),ResponseEnum.RESPONSE_WX_CANCEL_SUCCESS.getMessage());
				return response;
	        }else{
	        	response.setCodeMessage(ResponseEnum.RESPONSE_FAILURE.getCode(),ResponseEnum.RESPONSE_FAILURE.getMessage());
	            return response;
	        }
        }catch(Exception e){
        	response.setCodeMessage(ResponseEnum.RESPONSE_FAILURE.getCode(),ResponseEnum.RESPONSE_FAILURE.getMessage());
		}
        return response;
    }
}
