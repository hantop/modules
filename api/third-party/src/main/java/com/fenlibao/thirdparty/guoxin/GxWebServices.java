package com.fenlibao.thirdparty.guoxin;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import com.fenlibao.thirdparty.guoxin.util.DES3Util;
import com.fenlibao.thirdparty.guoxin.vo.xsd.IdentityResponseSingle;
import com.fenlibao.thirdparty.guoxin.webservice.QueryServicesPortType;
import com.fenlibao.thirdparty.guoxin.webservice.QueryServicesPortTypeProxy;


/**
 * 国信万源-实名认证 api
 * 
 * @author junda.feng 2016-4-18
 * 
 */
public class GxWebServices {


	/**
	 * @param name     姓名
	 * @param identity 身份证号码
	 * @param KEY      加密
	 * @param URL 调用的webservice接口地址
	 * @param USERNAME 用户名
	 * @param PASSWORD 密码
	 * @throws Exception
	 * @throws RemoteException
	 */
	public String gxRealNameAuthentication(String name, String identity,String KEY,String URL,String USERNAME,String PASSWORD) {
		
		String xml = null;
		try {
			// 1、创建代理
			QueryServicesPortTypeProxy proxy = new QueryServicesPortTypeProxy();
			proxy.setEndpoint(URL);
			// 2、创建服务
			QueryServicesPortType service = proxy.getQueryServicesPortType();
			// 3、调用接口
			String param = name + "," + identity;
			// 单条
			xml = service.querySingle(
					DES3Util.desedeEncoder(USERNAME, KEY),
					DES3Util.desedeEncoder(PASSWORD, KEY),
					DES3Util.desedeEncoder(GxConst.TYPE, KEY),
					DES3Util.desedeEncoder(param, KEY));
			if (xml != null && !"".equals(xml)) {
				// 解密
				xml = DES3Util.desedeDecoder(xml, KEY);
			}
		} catch (Throwable throwable){
			throwable.printStackTrace();
		}
		return xml;
	}
	
	
	/**
	 *  根据xml字符串返回封装对象IdentityResponseSingle
	 */
	public IdentityResponseSingle xmlElements(String xml) {
		IdentityResponseSingle identityResponse = null;
		if(xml!=null){
			identityResponse = new IdentityResponseSingle();
			StringReader read = new StringReader(xml);
			InputSource source = new InputSource(read);
			SAXReader reader = new SAXReader();
			Document doc;
			try {
				doc = reader.read(source);
				Element root = doc.getRootElement();
				Element authmessage;
				Iterator<?> Iauthmessage = root.elementIterator("authmessage");
				while (Iauthmessage.hasNext()) {
					authmessage = (Element) Iauthmessage.next();
					// 判断查询是否成功
					if (!GxConst.authmessageStatus_success.equals(authmessage.elementText("status"))) {
						return null;
					}
					identityResponse.setAuthmessageStatus(authmessage.elementText("status"));
					identityResponse.setAuthmessageValue(authmessage.elementText("value"));
					
				}
				Iterator<?> IidentityInfos = root.elementIterator("identityInfos");
				if (!IidentityInfos.hasNext()) {
					return identityResponse;
				}
				Element identityInfos = (Element) IidentityInfos.next();
				Iterator<?> ii = identityInfos.elementIterator("identityInfo");
				Element identityInfo;
				while (ii.hasNext()) {
					identityInfo = (Element) ii.next();
					Iterator<?> Imessage = identityInfo.elementIterator("message");
					Element message;
					while (Imessage.hasNext()) {
						message = (Element) Imessage.next();
						// 判断认证成功是否成功
						if (!GxConst.messageStatus_success.equals(message.elementText("status"))) {
							return null;
						}
						identityResponse.setMessageStatus(message.elementText("status"));
						identityResponse.setMessageValue(message.elementText("value"));
						
					}
					identityResponse.setName(identityInfo.elementText("name"));
					identityResponse.setBirthday(identityInfo.elementText("birthday"));
					identityResponse.setIdentitycard(identityInfo.elementText("identitycard"));
					identityResponse.setPhoto(identityInfo.elementText("photo"));
					identityResponse.setAuthResult(identityInfo.elementText("authResult"));
					identityResponse.setAuthStatus(Integer.parseInt(identityInfo.elementText("authStatus")));
					identityResponse.setYyfzd(identityInfo.elementText("yyfzd"));
					identityResponse.setSex(identityInfo.elementText("sex"));
				}
			} catch (Throwable throwable){
				throwable.printStackTrace();
			}
		}
		return identityResponse;
	}

	public static void main(String arge[]) throws RemoteException, Exception {
		// final String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		// + "<data> "
		// + "<authmessage> "
		// + "<status>0</status>  "
		// + "<value>查询成功</value>  "
		// + "</authmessage>  "
		// + "<identityInfos>    "
		// + "<identityInfo name=\"丁家喜\" identity=\"110108196708179712\">      "
		// + "<message>        "
		// + "<status>0</status>        "
		// + "<value>信息认证成功</value>      "
		// + "</message>      "
		// + "<name desc=\"姓名\">丁家喜</name>     "
		// +
		// " <identitycard desc=\"身份证号\">110108196708179712</identitycard>      "
		// + "<authStatus desc=\"认证状态\">0</authStatus>      "
		// + "<authResult desc=\"认证结果\">一致</authResult>      "
		// + "<photo desc=\"照片\">null</photo>      "
		// + "<yyfzd desc=\"原始发证地\">北京市海淀区</yyfzd>      "
		// + "<birthday desc=\"出生日期\">19670817</birthday>      "
		// + "<sex desc=\"性别\">男</sex>    " + "</identityInfo>  "
		// + "</identityInfos>" + "</data>";
		//
		//
		// IdentityResponseSingle identityResponse =new
		// GxWebServices().xmlElements(s);

		/****** 【身份】 *********/
		String name = "";// 被认证人的姓名
		String identity = "";// 被认证人的身份证号码
		// 一致
		name = "丁家喜";
		identity = "110108196708179712";

		GxWebServices gxWebServices = new GxWebServices();
//		String xml = gxWebServices.gxRealNameAuthentication(name, identity);
//		IdentityResponseSingle identityResponseSingle = gxWebServices
//				.xmlElements(xml);
//
//		System.out.print(identityResponseSingle.getAuthmessageStatus());
//		System.out.println(identityResponseSingle.getAuthmessageValue());
//		System.out.print(identityResponseSingle.getMessageStatus());
//		System.out.println(identityResponseSingle.getMessageValue());
//		System.out.println(identityResponseSingle.getName());

	}
}
