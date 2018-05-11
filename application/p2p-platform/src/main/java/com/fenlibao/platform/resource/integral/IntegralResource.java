package com.fenlibao.platform.resource.integral;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.model.Restriction;
import com.fenlibao.platform.resource.ParentResource;
import com.fenlibao.platform.service.integral.IntegralService;

/**
 * Created by Lullaby on 2016/2/16.
 */
@Path("points")
public class IntegralResource extends ParentResource {
	
	@Inject
	private IntegralService integralService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Restriction get() {
        Restriction key = new Restriction();
        key.setAppid("hiya");
        key.setSecret("hiya back");
        return key;
    }
    
    /**
     * 获取会员积分
     * @param appid
     * @param sign
     * @param openid
     * @return
     * @throws Exception
     */
    @GET
	@Path("member")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMemberPoints(@QueryParam("appid")String appid, @QueryParam("sign")String sign,
			@QueryParam("openid")String openid) throws Exception {
    	Map<String, Object> response = success();
    	if (!StringUtils.isNoneBlank(appid, sign, openid)) {
            response = failure(Response.SYSTEM_EMPTY_PARAMETERS);
            return jackson(response);
        }
    	Integer points = integralService.getPoints(appid, openid);
    	response.put("points", points);
    	return jackson(response);
    }

}
