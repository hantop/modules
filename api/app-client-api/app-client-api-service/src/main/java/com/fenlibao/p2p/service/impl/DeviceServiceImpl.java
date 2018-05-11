package com.fenlibao.p2p.service.impl;

import com.fenlibao.p2p.dao.AdImageDao;
import com.fenlibao.p2p.dao.StartpageImageDao;
import com.fenlibao.p2p.dao.TApkUpdateDao;
import com.fenlibao.p2p.model.entity.AdImage;
import com.fenlibao.p2p.model.entity.TApkUpdate;
import com.fenlibao.p2p.model.entity.TStartupImage;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.vo.AdImageVo;
import com.fenlibao.p2p.model.vo.ApkUpdateVo;
import com.fenlibao.p2p.service.DeviceService;
import com.fenlibao.p2p.util.CommonTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private AdImageDao adImgDao;

    @Resource
    private StartpageImageDao startpageImageDao;

    @Resource
    private TApkUpdateDao apkUpdateDao;

    @Override
    public List<AdImageVo> getAdimgList(String screenTypeId, String clientTypeId,String advertType,String versionType) {
        return getAdimgList(screenTypeId, clientTypeId, advertType, versionType,  1) ;
    }

    @Override
    public List<AdImageVo> getAdimgList(String screenTypeId, String clientTypeId, String advertType,
                                        String versionType, int versionTypeEnum) {
        List<AdImageVo> resultList = new ArrayList<AdImageVo>();

        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isEmpty(screenTypeId)){
        	screenTypeId="0";
        }
        if(StringUtils.isEmpty(advertType)){
        	advertType = String.valueOf(InterfaceConst.DEFAULT_ADVERT_TYPE);
        }
        map.put("screenTypeId", screenTypeId);
        map.put("clientTypeId", clientTypeId);
        map.put("versionType", versionType);
        map.put("advertType", advertType);
        map.put("versionTypeEnum", versionTypeEnum);

        List<AdImage> list = this.adImgDao.getAdImg(map);
        for (AdImage ad : list) {
            AdImageVo vo = new AdImageVo();
            vo.setAdPicUrl(ad.getPicUrl());
            StringBuffer link =new StringBuffer(ad.getResponseLink());
            StringBuffer newLink=new StringBuffer();
        	for(int i=0;i<link.length();i++){
        		if(CommonTool.isChinese(link.charAt(i))){
        			try {
						newLink.append(URLEncoder.encode(String.valueOf(link.charAt(i)), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
        		}else{
        			newLink.append(link.charAt(i));
        		}
        	}
            vo.setAdWebUrl(newLink.toString());
            vo.setLoginFlag(ad.getLoginFlag());
            resultList.add(vo);
        }
        return resultList;
    }

    @Override
    public String getStartpageImage(String screenTypeId, String clientTypeId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("screenTypeId", screenTypeId);
        map.put("clientTypeId", clientTypeId);
        TStartupImage image = startpageImageDao.getStartpageImage(map);

        String picName = "";
        if (null != image) {
            picName = image.getImgServer() + image.getImgName();
        }
        return picName;
    }

    @Override
    public ApkUpdateVo getApk(String versionCode,int clientType,String channelCode) {
        ApkUpdateVo vo = new ApkUpdateVo();
        vo.setUpdateFlag(0);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("clientType", clientType);
        //客户端升级统一升到到一个包  modify by laubrence 2016-1-28 16:39:38
//        if(StringUtils.isNotEmpty(channelCode)){
//        	map.put("channelCode", channelCode);
//        }
        
        TApkUpdate apk = apkUpdateDao.getApk(map);
        if (null != apk) {
            int tag = CommonTool.comparVersion(versionCode, apk.getVersionCode());
            if (tag >= 0) {
                return vo;
            }

            vo.setAppUrl(apk.getAppUrl().trim());
            vo.setUpdateContent(apk.getUpdateContent());
            vo.setUpdateFlag(apk.getUpdateFlag());
            vo.setUpdateType(apk.getUpdateType());
            vo.setVersion(apk.getVersion());
        }
        return vo;
    }

}
