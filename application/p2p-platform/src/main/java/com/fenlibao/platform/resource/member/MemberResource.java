package com.fenlibao.platform.resource.member;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.fenlibao.platform.common.exception.BusinessException;
import com.fenlibao.platform.common.util.StringHelper;
import com.fenlibao.platform.model.Response;
import com.fenlibao.platform.model.member.MerchantMember;
import com.fenlibao.platform.resource.ParentResource;
import com.fenlibao.platform.service.CommonService;
import com.fenlibao.platform.service.member.MemberService;

@Path("member")
public class MemberResource extends ParentResource {

	@Inject
	private MemberService memberService;
	@Inject
	private CommonService commonService;
	
	/**
	 * 第三方会员注册
	 */
	@POST
	@Path("register")
	@Produces(MediaType.APPLICATION_JSON)
	public String register(@FormParam("appid")String appid, @FormParam("sign")String sign,
						   @FormParam("phone_num")String phone_num, @Context HttpServletRequest request) throws Exception {
        Map<String, Object> response = success();
        if (!StringUtils.isNoneBlank(appid, sign, phone_num)) {
            response = failure(Response.SYSTEM_EMPTY_PARAMETERS);
            return jackson(response);
        }
        if (!commonService.existsKey(phone_num)) {
        	try {
        		if (!StringHelper.isMobileNO(phone_num)) {
        			throw new BusinessException(Response.MEMBER_PHONE_NUM_FORMAT_ERROR);
        		}
        		String openid = memberService.register(appid, phone_num);
        		response.put("openid", openid);
            } catch (BusinessException b) {
            	response = failure(b);
            	logger.error("appid=[{}],phone_num=[{}],msg=[{}]", 
            			appid, phone_num, b.getMessage());
            } catch (Exception e) {
            	response = failure();
            	logger.error(String.format("register fail >>> appid=[%s],phone_num=[%s]", 
            			appid, phone_num), e);
            } finally {
            	commonService.removeKey(phone_num);
    		}
        } else {
    		logger.info("phone_num[{}]重复提交。。。", phone_num);
    		response = failure(Response.SYSTEM_REQUEST_FREQUENT);
    	}
		return jackson(response);
	}

	/**
	 * 第三方会员消费记录上传
	 */
	@POST
	@Path("consume/record")
	@Produces(MediaType.APPLICATION_JSON)
	public String saveConsumeRecord(
			@FormParam("appid") String appid,
			@FormParam("sign") String sign,
			@FormParam("openid") String openid,
			@FormParam("amount") String amount,
			@FormParam("typecode") String typecode,
			@FormParam("pos_sn") String pos_sn, @Context HttpServletRequest request) throws Exception {
		Map<String, Object> response = success();
		if (StringUtils.isAnyBlank(appid, sign, openid, amount, typecode, pos_sn)) {
			response = failure(Response.SYSTEM_EMPTY_PARAMETERS);
            return jackson(response);
		}
		if (!commonService.existsKey(pos_sn)) {
	        try {
	    		verifyPos_sn(pos_sn);
	    		verifyTypecode(typecode);
	    		verifyAmount(amount);
	    		MerchantMember member = memberService.getMerchantMember(openid);
	    		if (member != null) {
	    			memberService.doConsumeRecord(openid, amount, typecode, pos_sn, member);
	    		} else {
	    			response = failure(Response.MEMBER_NOT_EXIST);
	    		}
            } catch (BusinessException b) {
            	response = failure(b);
            	logger.error("appid=[{}],openid=[{}],amount=[{}],msg=[{}]", 
            			appid, openid, amount, b.getMessage());
            } catch (Exception e) {
            	response = failure();
            	logger.error(String.format("saveConsumeRecord >>> appid=[%s],openid=[%s]", 
            			appid, openid), e);
			} finally {
				commonService.removeKey(pos_sn);
			}
        } else {
    		logger.info("pos_sn[{}]重复提交。。。", pos_sn);
    		response = failure(Response.SYSTEM_REQUEST_FREQUENT);
    	}
		return jackson(response);
	}
	
	private void verifyPos_sn(String pos_sn) throws Exception {
		String reg = "(\\d*|[A-Za-z]*|_*){1,100}$";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(pos_sn);
		boolean isTrue = m.matches();
		if (!isTrue) {
			throw new BusinessException(Response.INTEGRAL_POS_SN_FORMAT_ERROR);
		}
	}
	
	private void verifyTypecode(String typecode) throws Exception {
		String reg = "[[A-Za-z]*|_]{1,64}$";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(typecode);
		boolean isTrue = m.matches();
		if (!isTrue) {
			throw new BusinessException(Response.INTEGRAL_TYPE_CODE_FORMAT_ERROR);
		}
	}
	
	private void verifyAmount(String amount) throws Exception {
		String reg = "^-?[\\d+(\\.\\d)?]{1,10}$";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(amount);
		boolean isTrue = m.matches();
		if (!isTrue) {
			throw new BusinessException(Response.INTEGRAL_AMOUNT_FORMAT_ERROR);
		}
	}
	
}
