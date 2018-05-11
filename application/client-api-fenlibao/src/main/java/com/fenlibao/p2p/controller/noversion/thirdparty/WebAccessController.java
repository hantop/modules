package com.fenlibao.p2p.controller.noversion.thirdparty;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.AccessToken;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.Constant;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.thirdparty.WebAccessService;
import com.fenlibao.p2p.service.user.LoginStateService;
import com.fenlibao.p2p.util.DateUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("webaccess")
public class WebAccessController {

	@Resource
    WebAccessService webAccessService;

    @Resource
	LoginStateService loginStateService;

    /**
	 * 目标系统自动登录
	 * @param paramForm
	 * @param accessToken
	 * @return
	 * @throws Exception
     */
    @RequestMapping(value = "autoLogin", method = RequestMethod.POST)		
    HttpResponse autoLogin(@ModelAttribute BaseRequestForm  paramForm,
    		@RequestParam(required = false, value = "accessToken") String accessToken) throws Exception{
    	HttpResponse response = new HttpResponse();
		if(!paramForm.validate() || StringUtils.isEmpty(accessToken)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		String decryptAccessToken;
		try {//AES解密
			decryptAccessToken = AES.getInstace().decrypt(accessToken);
		}catch (Exception e) {
			throw new BusinessException(ResponseCode.COMMON_PARAM_DECRYPT_FAILURE);
		}
		AccessToken accessData = webAccessService.getAccessToken(decryptAccessToken);
		if(accessData == null){
			throw new BusinessException(ResponseCode.COMMON_NOT_VALID_ACCESSTOKEN.getCode(),ResponseCode.COMMON_NOT_VALID_ACCESSTOKEN.getMessage());
		}
		if(!paramForm.getClientType().equals(String.valueOf(accessData.getTargetClientType()))){
			throw new BusinessException(ResponseCode.COMMON_ACCESSTOKEN_CLIENT_TYPE_INVALID.getCode(),ResponseCode.COMMON_ACCESSTOKEN_CLIENT_TYPE_INVALID.getMessage());
		}
		int isVerified = accessData.getIsVerified();
		long nowTime = DateUtil.dateToTimestampToSec(DateUtil.nowDate());
		long expireTime = DateUtil.dateToTimestampToSec(accessData.getExpireTime()) ;
		if(isVerified>0 || nowTime > expireTime ){
			throw new BusinessException(ResponseCode.COMMON_ACCESSTOKEN_TIMEOUT.getCode(),ResponseCode.COMMON_ACCESSTOKEN_TIMEOUT.getMessage());
		}
		int recordId = accessData.getRecordId();
		int updateSuc = webAccessService.updateVaildAccessToken(recordId);
		if(updateSuc==0){
			throw new BusinessException(ResponseCode.FAILURE.getCode(),ResponseCode.FAILURE.getMessage());
		}
		int userId = accessData.getUserId();
		int targetClientType = accessData.getTargetClientType();

		String token = loginStateService.saveLoginToken(String.valueOf(targetClientType), String.valueOf(userId));
		if(StringUtils.isEmpty(token)){
			throw new BusinessException(ResponseCode.COMMON_USER_TOKEN_BUILD_FAILURE.getCode(),ResponseCode.COMMON_USER_TOKEN_BUILD_FAILURE.getMessage());
		}
		Map<String,Object> returnMap = new HashMap<>();
		returnMap.put("userId",userId);
		returnMap.put("token",token);
		response.setData(returnMap);
        return response;
    }

	/**
	 * 获取目标系统自动登录的accessToken
	 * @param paramForm
	 * @param targetClientType
	 * @param token
	 * @param userId
	 * @return
     * @throws Exception
     */
	@RequestMapping(value = "getAccessToken", method = RequestMethod.GET)
    HttpResponse getAcessToken(@ModelAttribute BaseRequestForm paramForm,
							   @RequestParam(required = false, value = "targetClientType") String targetClientType,
							   @RequestParam(required = false, value = "token") String token,
							   @RequestParam(required = false, value = "userId") String userId) throws Exception{
		HttpResponse response = new HttpResponse();
		if(!paramForm.validate() || StringUtils.isEmpty(userId) || StringUtils.isEmpty(token)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
			return response;
		}
		if(StringUtils.isEmpty(targetClientType)){
			//如果没有targetClientType,则转成wap:5登录
			targetClientType= String.valueOf(Constant.CLIENTTYPE_WAP);
		}
		if(paramForm.getClientType().equals(targetClientType)){
			throw new BusinessException(ResponseCode.COMMON_ACCESSTOKEN_CLIENT_TYPE_NOT_SAME.getCode(),ResponseCode.COMMON_ACCESSTOKEN_CLIENT_TYPE_NOT_SAME.getMessage());
		}
		String encryptAccessToken;
		String accessToken = webAccessService.buildAccessToken(Integer.valueOf(userId),paramForm.getClientType(),targetClientType);
		if(StringUtils.isEmpty(accessToken)) {
			throw new BusinessException(ResponseCode.COMMON_ACCESSTOKEN_BUILD_FAILURE.getCode(),ResponseCode.COMMON_ACCESSTOKEN_BUILD_FAILURE.getMessage());
		}
		try {//AES加密
			encryptAccessToken = AES.getInstace().encrypt(accessToken);
		}catch (Exception e) {
			throw new BusinessException(ResponseCode.COMMON_PARAM_ENCRYPT_FAILURE);
		}
		response.getData().put("accessToken", encryptAccessToken);
		return response;
	}
}
