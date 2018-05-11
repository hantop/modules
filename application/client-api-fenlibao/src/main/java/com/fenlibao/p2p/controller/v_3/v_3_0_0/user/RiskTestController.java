package com.fenlibao.p2p.controller.v_3.v_3_0_0.user;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fenlibao.p2p.model.entity.user.RiskTestResult;
import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.model.vo.user.RiskTestResultVO;
import com.fenlibao.p2p.service.user.RiskTestService;
import com.fenlibao.p2p.util.CommonTool;


/** P2P网络借贷风险承受力评估测试
 * @author: junda.feng
 */
@RestController("v_3_0_0/RiskTestController")
@RequestMapping(value = "risk", headers = APIVersion.v_3_0_0)
public class RiskTestController {
	
	private static final Logger logger= LogManager.getLogger(RiskTestController.class);

	@Resource
	RiskTestService riskTestService;
	
	/**
	 * 获取问题列表
	 * @param request
	 * @param paramForm
	 * @param page
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public HttpResponse getQuestionList(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm) throws Exception{
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}
			Map map=new HashMap<>();
			map.put("items",riskTestService.getQuestionList());
			response.setData(map);
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
	
	/**
	 * 提交测试结果
	 * @param request
	 * @param paramForm
	 * @param page
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "save", method = RequestMethod.GET)
    public HttpResponse save(HttpServletRequest request,
    		@ModelAttribute BaseRequestForm  paramForm,Integer score,Integer userId) throws Exception{
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate()){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
				return response;
			}
			if(0>score || score>100){
				response.setCodeMessage(ResponseCode.COMMON_PARAM_WRONG);
				return response;
			}
			RiskTestResult result= riskTestService.getResultByScore(score);
			if(result!=null){
				riskTestService.addTestResult(result.getId(), userId, score);
			}
			RiskTestResultVO vo=new RiskTestResultVO();
			vo.setResult(result.getResult());
			vo.setType(result.getType());
			response.setData(CommonTool.toMap(vo));
			return response;
		}catch(Exception ex){
			throw ex;
		}
	}
	
	
	
}