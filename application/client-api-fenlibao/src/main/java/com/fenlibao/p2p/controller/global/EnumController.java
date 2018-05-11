package com.fenlibao.p2p.controller.global;

import com.fenlibao.p2p.model.entity.TEnum;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.global.ResponseCode;
import com.fenlibao.p2p.service.EnumService;
import com.fenlibao.p2p.util.loader.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * RESTful接口模板
 *
 * @author Lullaby
 *
 */
@RestController
@RequestMapping("enumapi")
public class EnumController {

	private static final Logger logger = LogManager.getLogger(EnumController.class);
	
	@Resource
	private EnumService enumService;

	/**
	 * 获取枚举
	 *
	 * @param enumTable 枚举表名
	 * @param enumColumn 枚举字段名
	 * @return json
	 */
	@RequestMapping(value = "enum", method = RequestMethod.GET)
    HttpResponse getEnum(
			@RequestParam(required = false, value = "enumTable") String enumTable,
			@RequestParam(required = false, value = "enumColumn") String enumColumn) {
		HttpResponse response = new HttpResponse();
		try {
			response.getData().put("enums", enumService.getEnum(enumTable, enumColumn));
		} catch (Exception e) {
			response.setCodeMessage(ResponseCode.FAILURE);
			logger.error("[EnumController.getEnum]", e);
		}
		return response;
	}

	/**
	 * 新增枚举
	 *
	 * @param enumTable 枚举表名
	 * @param enumColumn 枚举字段名
	 * @param enumKey 枚举key
	 * @param enumValue 枚举value
	 * @return json
	 */
	@RequestMapping(value = "enum", method = RequestMethod.POST)
    HttpResponse addEnum(
			@RequestParam(required = true, value = "enumTable") String enumTable,
			@RequestParam(required = true, value = "enumColumn") String enumColumn,
			@RequestParam(required = true, value = "enumKey") String enumKey,
			@RequestParam(required = true, value = "enumValue") String enumValue,
			TEnum tenum) {
		return enumService.addEnum(tenum);
	}

	/**
	 * 根据enumTable/enumColumn/enumKey修改enumValue
	 *
	 * @param enumTable 枚举表名
	 * @param enumColumn 枚举字段名
	 * @param enumKey 枚举key
	 * @param enumValue 枚举value
	 * @return json
	 */
	@RequestMapping(value = "enum", method = RequestMethod.PUT)
    HttpResponse updateEnum(
			@RequestParam(required = true, value = "enumTable") String enumTable,
			@RequestParam(required = true, value = "enumColumn") String enumColumn,
			@RequestParam(required = true, value = "enumKey") String enumKey,
			@RequestParam(required = true, value = "enumValue") String enumValue,
			TEnum tenum) {
		return enumService.updateEnum(tenum);
	}

    /**
     * 根据id删除枚举
     *
     * @param id 主键id
     * @return json
     */
	@RequestMapping(value = "enum/{id}", method = RequestMethod.DELETE)
    HttpResponse deleteEnum(@PathVariable(value = "id") String id) {
		return enumService.deleteEnum(id);
	}

}
