package com.fenlibao.p2p.service;

import com.fenlibao.p2p.model.vo.AdImageVo;
import com.fenlibao.p2p.model.vo.ApkUpdateVo;

import java.util.List;

public interface DeviceService {

	/**
	 * 获取广告图信息
	 * @param screenType 屏幕类型
	 * @param clientType 客户端类型
	 * @param advertType 广告类型
	 * @param versionType 客户端版本类型
	 * @return
	 */
	public List<AdImageVo> getAdimgList(String screenType, String clientType,String advertType,String versionType,int versionTypeEnum);
	
	/**
	 * 获取启动页图片路径
	 * @param screenType 屏幕类型
	 * @param clientType 客户端类型
	 * @return
	 */
	public String getStartpageImage(String screenType, String clientType);
	
	/**
	 * 获取最近的APK文件信息
	 * @return
	 */
	public ApkUpdateVo getApk(String version,int clientType,String channelCode);
}
