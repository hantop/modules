package com.fenlibao.p2p.controller.noversion.thirdparty;

import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.common.util.encrypt.PasswordCryptUtil;
import com.fenlibao.p2p.model.consts.ThirdpartTypeConst;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.vo.UserThirdpartyVO;
import com.fenlibao.p2p.model.vo.WeixinStatusVO;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.payment.tp.baofoo.BaofooBindCardService;
import com.fenlibao.p2p.service.thirdparty.ThirdpartyService;
import com.fenlibao.p2p.service.user.AutoBidService;
import com.fenlibao.p2p.util.CommonTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	@Resource
	private BaofooBindCardService baofooBindCardService;
	@Resource
	private AutoBidService autoBidService;


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
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			} else {
				if(StringUtils.isNotBlank(phoneNum) && StringUtils.isNotBlank(username)) {
					response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
				} else {
					Throwable throwable = null;
					Map<String, Object> map = new HashMap<>();

					if(StringUtils.isNotBlank(phoneNum)) {
						try{
							phoneNum = AES.getInstace().decrypt(phoneNum);
							map.put("phoneNum", phoneNum);
						} catch(Exception e) {
							throwable = e;
							response.setCodeMessage(ResponseCode.COMMON_PARAM_ENCRYPT_FAILURE);
							return response;
						}
					} else {
						try{
							username = AES.getInstace().decrypt(username);
							map.put("username", username);
						} catch(Exception e) {
							throwable = e;
							response.setCodeMessage(ResponseCode.COMMON_PARAM_DECRYPT_FAILURE);
							return response;
						}
					}
					try{
						password = AES.getInstace().decrypt(password);
					} catch(Exception e) {
						throwable = e;
						response.setCodeMessage(ResponseCode.COMMON_PARAM_DECRYPT_FAILURE);
						return response;
					}

					if(throwable == null) {
						// 根据手机号或登陆名查询用户信息
						UserInfo userInfo = userInfoService.getUserInfoByPhoneNumOrUsername(map);
						if (userInfo == null) {
							response.setCodeMessage(ResponseCode.USER_NOT_EXIST);
							return response;
						} else {
							// 密码进行AES解码进行加密后进行对比
							String pwd = userInfo.getPassword();
							if (PasswordCryptUtil.crypt(password).equals(pwd)) {
								String userId = userInfo.getUserId();
								// 第三方类型为微信(1)
								int isbind = thirdpartyService.isBindThirdparty(openId, userId, ThirdpartTypeConst.weixinType);
								if(isbind>0){
									response.setCodeMessage(ResponseCode.WEIXIN_IS_BINDED);
									return response;
								}
								UserThirdpartyVO resultVO = thirdpartyService.bind(openId, userId, userInfo.getUsername(), ThirdpartTypeConst.weixinType);
								if(resultVO==null) {
									response.setCodeMessage(ResponseCode.WEIXIN_BIND_FAILED);
									return response;
								}else{
									try {
										response.setCodeMessage(ResponseCode.SUCCESS);
									} catch(Exception e){
										e.printStackTrace();
										response.setCodeMessage(ResponseCode.FAILURE);
									}
									return response;
								}
							} else {
								response.setCodeMessage(ResponseCode.USER_PASSWORD_ERROR);
								return response;
							}
						}
					}
				}
			}
		}catch(Exception e){
			response.setCodeMessage(ResponseCode.FAILURE);
		}

		return response;
	}

	@RequestMapping(value = "weixin/autoLogin", method = RequestMethod.POST)
	HttpResponse autoLogin(@ModelAttribute BaseRequestForm paramForm,
						   @RequestParam(required = true, value = "openId") String openId) {
		HttpResponse response = new HttpResponse();
		if(StringUtils.isEmpty(openId)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try{
			int userId = thirdpartyService.isCanAutoLogin(openId,ThirdpartTypeConst.weixinType);
			if(userId>0){
				// 获取用户账户信息字段Map
				Map<String, Object> data = new HashMap<>();

				UserAccountInfoVO userAccountInfoVO = thirdpartyService.getUserInfo(userId,paramForm.getClientType(),paramForm.getDeviceId());
				data = userInfoService.getUserAccountInfoDataMap(userAccountInfoVO,1);

				boolean containBaofoo = baofooBindCardService.validateBind(userId);
				List<Map<String,Object>> bankList = (List<Map<String,Object>>)data.get("bankList");
				if (containBaofoo&&bankList.size()!=0) {
					List<Map<String, Object>> newBankList = checkBaofooCardInfo(bankList);
					data.put("bankList", newBankList);
				}else if(containBaofoo&&bankList.size()==0) {//连连未认证，宝付已认证
					List<Map<String, Object>> newBankList = userInfoService.getUserBaofooCardInfo(userId);
					Map<String, Object> param = newBankList.get(0);
					String bankNum = "";
					try {
						bankNum = StringHelper.decode((String) param.get("bankNum"));
					} catch (Throwable e) {
						e.printStackTrace();
					}
					try {
						bankNum = AES.getInstace().encrypt(bankNum);
					} catch (Throwable throwable) {
						throwable.printStackTrace();
					}
					param.put("bankNum", bankNum);
					newBankList = new ArrayList<>();
					newBankList.add(param);
					data.put("bankList", newBankList);
				}
				data.put("autoBidding", autoBidService.checkUserAutoBid(Integer.valueOf((String) data.get("userId"))));

				Map<String, Object> hxcgInfo = new HashMap<>();
				//HXAccountInfo accountInfo = hxUserService.getAccountInfo(Integer.valueOf((String) data.get("userId")));
				hxcgInfo.put("isHXOpenAccount", 0);
				hxcgInfo.put("isAccountActivity", 0);
				try {
					hxcgInfo.put("cgAccount", 0);
				} catch (Throwable throwable) {
					throwable.printStackTrace();
					hxcgInfo.put("cgAccount", 0);
				}

				data.put("hxcgInfo", hxcgInfo);
				response.setData(data);
				response.setCodeMessage(ResponseCode.SUCCESS);
				return response;
			}else{
				response.setCodeMessage(ResponseCode.WEIXIN_NOT_BIND);
				return response;
			}
		}catch(Exception e){
			response.setCodeMessage(ResponseCode.FAILURE);
		}
		return response;
	}

	@RequestMapping(value = "weixin/isBind", method = RequestMethod.POST)
	HttpResponse isBindWeixin(@ModelAttribute BaseRequestForm paramForm,
							  @RequestParam(required = true, value = "openId") String openId) {
		HttpResponse response = new HttpResponse();
		if(StringUtils.isEmpty(openId)){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try{
			int isbind = thirdpartyService.isBindThirdpartyOpenId(openId, ThirdpartTypeConst.weixinType);
			if(isbind>0){
				WeixinStatusVO weixinStatusVO = new WeixinStatusVO();
				weixinStatusVO.setWeixinStatus(Status.BINDED.name());
				weixinStatusVO.setWeixinMsg(Status.BINDED.getChineseName());
				response.setData(CommonTool.toMap(weixinStatusVO));
				response.setCodeMessage(ResponseCode.SUCCESS);
				return response;
			}else{
				WeixinStatusVO weixinStatusVO = new WeixinStatusVO();
				weixinStatusVO.setWeixinStatus(Status.UNBIND.name());
				weixinStatusVO.setWeixinMsg(Status.UNBIND.getChineseName());
				response.setData(CommonTool.toMap(weixinStatusVO));
				response.setCodeMessage(ResponseCode.SUCCESS);
				return response;
			}
		}catch(Exception e){
			response.setCodeMessage(ResponseCode.FAILURE);
		}
		return response;
	}

	@RequestMapping(value = "weixin/cancelAutoLogin", method = RequestMethod.POST)
	HttpResponse cancelAutoLogin(@RequestParam(required = true, value = "openId") String openId) {
		HttpResponse response = new HttpResponse();
		if(StringUtils.isEmpty(openId) ){
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
			return response;
		}
		try{
			int flag = thirdpartyService.cancelAutoLogin(openId,ThirdpartTypeConst.weixinType);
			if(flag>0){
				response.setCodeMessage(ResponseCode.WEIXIN_CANCEL_SUCCESS);
				return response;
			}else{
				response.setCodeMessage(ResponseCode.FAILURE);
				return response;
			}
		}catch(Exception e){
			response.setCodeMessage(ResponseCode.FAILURE);
		}
		return response;
	}

	private List<Map<String,Object>> checkBaofooCardInfo(List<Map<String,Object>> bankList) {
		Map<String, Object> card = bankList.get(0);
		Map<String, Object> bankParam = new HashMap<>();

		bankParam.put("bankName", card.get("bankName"));

		bankParam.put("bankNum",card.get("bankNum"));

		bankParam.put("bankCode", card.get("bankCode"));

		bankParam.put("bankType", 1);

		bankParam.put("bankCardId", card.get("bankCardId"));

		bankParam.put("bankInfoCompleteFlag", card.get("bankInfoCompleteFlag"));

		bankParam.put("bankAuthStatus", card.get("bankAuthStatus"));

		bankList.add(bankParam);

		return bankList;
	}
}
