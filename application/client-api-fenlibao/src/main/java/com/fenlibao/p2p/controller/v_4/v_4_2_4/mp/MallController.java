package com.fenlibao.p2p.controller.v_4.v_4_2_4.mp;

import com.fenlibao.p2p.model.global.APIVersion;
import com.fenlibao.p2p.model.global.BaseRequestForm;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.mp.vo.mall.MallCommodityVO;
import com.fenlibao.p2p.model.mp.vo.mall.MallConsumePatternsVO;
import com.fenlibao.p2p.service.mp.IMallService;
import com.fenlibao.p2p.util.loader.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城控制器
 * @author zcai
 * @date 2016年3月25日
 */
@RestController("v_4_2_4/MallController")
@RequestMapping(value = "mall", headers = APIVersion.v_4_2_4)
public class MallController {

	private static final Logger logger = LogManager.getLogger(MallController.class);
	
	@Resource
	private IMallService mallService;
	
	/**
	 * 获取商品
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "commodity", method = RequestMethod.GET)
	public HttpResponse getCommodity(BaseRequestForm params) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<>();
		List<MallCommodityVO> commodity = mallService.getCommodity();
		data.put("commodity", commodity);
		data.put("slogan", Config.get("mall.commodity.slogan"));
		response.setData(data);
		return response;
	}
	
	/**
	 * 获取消费方式
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "consume/patterns", method = RequestMethod.GET)
	public HttpResponse getConsumePatterns(BaseRequestForm params) {
		HttpResponse response = new HttpResponse();
		Map<String, Object> data = new HashMap<>();
		List<MallConsumePatternsVO> consumePatterns = mallService.getConsumePatterns();
		data.put("consumePatterns", consumePatterns);
		data.put("slogan", Config.get("mall.consume.slogan"));
		response.setData(data);
		return response;
	}
	
}
