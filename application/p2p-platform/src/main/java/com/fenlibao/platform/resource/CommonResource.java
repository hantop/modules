package com.fenlibao.platform.resource;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.service.CommonService;

@Path("common")
public class CommonResource extends ParentResource {

	@Inject
	private CommonService commonService;
	
	/**
	 * 更新秘钥
	 * @param appid
	 * @param merchant_appid
	 * @param secret
	 * @param status
	 * @return
	 */
	@POST
	@Path("update/secret")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateSecret(@FormParam("appid")String appid, @FormParam("sign")String sign,
			@FormParam("merchant_appid")String merchant_appid, @FormParam("secret")String secret, 
			@FormParam("status")String status, @Context HttpServletRequest request) {
		Map<String, Object> response = success();
		Integer _status = null;
		if (StringUtils.isBlank(status) && StringUtils.isBlank(secret)) {
			response = failure(Response.SYSTEM_EMPTY_PARAMETERS);
			return jackson(response);
		}
		if (StringUtils.isNotBlank(status)) {
			_status = Integer.valueOf(status);
		}
		if (StringUtils.isBlank(secret)) {
			secret = null;
		}
		try {
			commonService.updateSecret(merchant_appid, secret, _status);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			response = failure();
		}
		return jackson(response);
	}
	
	/**
	 * 更新商户ip配置
	 * <p>exist ? delete : add
	 * @param appid
	 * @param sign
	 * @param merchant_appid
	 * @param ip
	 * @return
	 */
	@POST
	@Path("update/ip")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateIp(@FormParam("appid")String appid, @FormParam("sign")String sign,
			@FormParam("merchant_appid")String merchant_appid, @FormParam("ip")String ip) {
		Map<String, Object> response = success();
		if (!StringUtils.isNoneBlank(merchant_appid, ip)) {
			response = failure(Response.SYSTEM_EMPTY_PARAMETERS);
			return jackson(response);
		}
		boolean isExist = false;
		try {
			isExist = commonService.updateIp(merchant_appid, ip);
			if (isExist) {
				response.put(ip, "已停用");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			response = failure();
		}
		return jackson(response);
	}
}
