package com.fenlibao.platform.resource.thirdparty;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fenlibao.platform.thirdparty.util.security.MD5UtilDuozhuan;
import org.apache.commons.lang3.StringUtils;

import com.fenlibao.platform.resource.ParentResource;
import com.fenlibao.platform.service.thirdparty.TPUserService;
import com.fenlibao.platform.thirdparty.util.TPResponseUtil;

/**
 * 第三方数据提供
 * <p>很不愿意把这些写在这个project
 * @author zcai
 * @date 2016年5月27日
 */
@Path("tp") //该URI不校验IP、签名！
public class ThirdPartyResource extends ParentResource {
	
	@Inject
	private TPUserService tpUserService;

	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public String login(@QueryParam("username")String username,@QueryParam("password")String password) {
		Map<String, Object> params = TPResponseUtil.success();
		Map<String, String> data = new HashMap<>();
		if (!StringUtils.isNoneBlank(username,password)) {
			params = TPResponseUtil.failure();
			return jackson(params);
		}
		String token = tpUserService.getToken(username, password);
		data.put("token", token);
		params.put("data", data); //要按照第三方要求封装
		return jackson(params);
	}
	
	/**
	 * 融360登陆
	 * @param username
	 * @param password
	 * @return
	 */
	@GET
	@Path("login/rong360")
	@Produces(MediaType.APPLICATION_JSON)
	public String login360(@QueryParam("userName")String username,@QueryParam("password")String password) {
		Map<String, Object> params = TPResponseUtil.success();
		Map<String, String> data = new HashMap<>();
		if (!StringUtils.isNoneBlank(username,password)) {
			params = TPResponseUtil.failure();
			return jackson(params);
		}
		String token = tpUserService.getToken(username, password);
		data.put("token", token);
		params.put("data", data); //要按照第三方要求封装
		return jackson(params);
	}
	
}
