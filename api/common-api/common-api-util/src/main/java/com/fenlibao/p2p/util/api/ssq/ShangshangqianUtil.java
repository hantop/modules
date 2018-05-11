package com.fenlibao.p2p.util.api.ssq;

import cn.bestsign.sdk.BestSignSDK;
import cn.bestsign.sdk.domain.vo.params.ReceiveUser;
import cn.bestsign.sdk.domain.vo.params.SendUser;
import cn.bestsign.sdk.integration.Constants;
import cn.bestsign.sdk.integration.exceptions.BizException;
import cn.bestsign.sdk.integration.exceptions.ExecuteException;
import cn.bestsign.sdk.integration.utils.http.HttpSender;
import cn.bestsign.sdk.libs.BestSignLibs;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.util.api.load.ApiUtilConfig;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**上上签工具类
 * create by zeronx 2017-12-20 23:48
 */
public class ShangshangqianUtil {

	public static BestSignSDK sdk= ShangshangqianSDK.INSTANCE.getInstance();

	/**
	 * 为何在这里也要这些配置参数： 为了后来者添加的团签
	 */
	private static boolean develop_mode= Boolean.parseBoolean(ApiUtilConfig.get("ssq.develop.mode"));//true开发模式,false生产模式

	//测试环境参数
	private static  final String mid_test = ApiUtilConfig.get("ssq.sign.mid_test");
	private static  final String pem_test =ApiUtilConfig.get("ssq.sign.pem_test");
	private static String host_test = ApiUtilConfig.get("ssq.sign.host_test");

	//正式环境参数
	private static final String mid = ApiUtilConfig.get("ssq.sign.mid");
	private static final String pem = ApiUtilConfig.get("ssq.sign.pem");
	private static String host = ApiUtilConfig.get("ssq.sign.host");
	private static BestSignLibs bestSignLibs = null;
	static {
		if (develop_mode) {
			bestSignLibs = new BestSignLibs(mid_test, pem_test, host_test);
		} else {
			bestSignLibs = new BestSignLibs(mid, pem, host);
		}
	}


	/**
	 *  团签接口测试
	 */
	public static JSONObject autoTeamSign(final String signId, final String fileMd5, final String fileType, final String fileContent, final Map<String, String> presentMap)
			throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, IOException, BizException, ExecuteException {
		String method = "teamSign.json";
		String path = "/open/teamSign.json";
		HashMap data = new HashMap() {
			{
				this.put("request", new HashMap() {
					{
						this.put("content", new HashMap() {
							{
								this.put("representativeCredentialsType", presentMap.get("representativeCredentialsType")); // "0" 身份证
								this.put("memberCount", presentMap.get("memberCount")); // presentMap.get("memberCount")
								this.put("contractId", signId);
								this.put("fileMd5", fileMd5);
								this.put("title", "团签");
								this.put("representativeCredentials", presentMap.get("representativeCredentials")); // 640223194901010429 presentMap.get("representativeCredentials")
								this.put("representative", presentMap.get("representative")); // "{\"description\":\"代表\",\"value\":\"代表姓名\"}" //presentMap.get("representative")
								this.put("fileType", fileType);
								this.put("fileContent", fileContent);
								this.put("representativeType", presentMap.get("representativeType")); // presentMap.get("representativeType") "{\"description\":\"代表类型\",\"value\":\"1\"}"
							}
						});
					}
				});
			}
		};
		String requestBody = JSONObject.toJSONString(data);
		String signData = bestSignLibs.getSignData(new String[]{method, mid, bestSignLibs.getRequestMd5(requestBody)});
		Object headerData = null;
		Map<String, Object> result = bestSignLibs.execute("POST", path, requestBody, signData, (Map)headerData);
		JSONObject jsonObject = parseExecutorResult2(result);
		return jsonObject;
	}


	public static JSONObject endContract(final String signId) throws Exception {
		return sdk.endContract(signId);
	}

	private static JSONObject parseExecutorResult2(Map<String, Object> executorResult) throws ExecuteException, BizException {
		Object result = parseExecutorResult(executorResult);
		if (result == null) {
			return null;
		} else if (result instanceof JSONObject) {
			return (JSONObject)result;
		} else {
			throw new BizException(-1, "not a json");
		}
	}
	private static Object parseExecutorResult(Map<String, Object> executorResult) throws ExecuteException, BizException {
		if (!executorResult.containsKey("result")) {
			throw new ExecuteException("executorResult format wrong: no result field", executorResult);
		} else if (executorResult.get("result") instanceof String) {
			return (String)executorResult.get("result");
		} else {
			Map<String, Object> result = (Map)executorResult.get("result");
			if (result == null) {
				return null;
			} else if (!result.containsKey("response")) {
				throw new ExecuteException("result format wrong: no response field", executorResult);
			} else {
				Map<String, Object> response = (Map)result.get("response");
				if (!response.containsKey("info")) {
					throw new ExecuteException("response format wrong: no info field", executorResult);
				} else {
					Map<String, Object> info = (Map)response.get("info");
					if (!info.containsKey("code")) {
						throw new ExecuteException("info format wrong: no code field", executorResult);
					} else {
						int code;
						if (info.get("code") instanceof String) {
							code = Integer.parseInt((String)info.get("code"));
						} else {
							code = ((Integer)info.get("code")).intValue();
						}

						Object content = null;
						if (response.containsKey("content")) {
							content = response.get("content");
						}

						if (code != 100000) {
							throw new BizException(code, content);
						} else {
							return result;
						}
					}
				}
			}
		}
	}
	//注册
	public static JSONObject regUser(Constants.USER_TYPE userType, String email, String mobile, String name) throws Exception {
		JSONObject result = sdk.regUser(userType, email, mobile, name);
		return result;
	}
	//CFCA证书申请
	public static JSONObject certificateApply(Constants.CA_TYPE caType, String name, String password, String linkMobile, String email, String address,
		String province, String city, String linkIdCode)throws Exception {
		JSONObject result = sdk.certificateApply(caType, name, password, linkMobile, email, address, province, city, linkIdCode);
		return result;
	}

	//CFCA证书申请
	public static JSONObject certificateApply(Map<String, Object> map) throws Exception {
		Constants.CA_TYPE caType = (Constants.CA_TYPE)map.get("caType");
		JSONObject result = sdk.certificateApply(caType, (String)map.get("name"), (String)map.get("password"), (String)map.get("linkMobile"), (String)map.get("email"), (String)map.get("address"),
				(String)map.get("province"), (String)map.get("city"), (String)map.get("linkIdCode"));
		return result;
	}

	//ZJCA证书申请
	public static JSONObject certificateApplyEnterprise(Constants.CA_TYPE caType, String name, String password, String linkMan, String linkMobile, String email, String address,
											  String province, String city, String linkIdCode, String icCode, String orgCode, String taxCode)throws Exception {
		JSONObject result = sdk.certificateApply(caType, name, password, linkMan, linkMobile, email, address, province, city, linkIdCode, icCode, orgCode, taxCode);
		return result;
	}

	//ZJCA证书申请
	public static JSONObject certificateApplyEnterprise(Map<String, Object> map) throws Exception {
		Constants.CA_TYPE caType = (Constants.CA_TYPE)map.get("caType");
		JSONObject result = sdk.certificateApply(caType, (String)map.get("name"), (String)map.get("password"), (String)map.get("linkMan"), (String)map.get("linkMobile"), (String)map.get("email"), (String)map.get("address"),
				(String)map.get("province"), (String)map.get("city"), (String)map.get("linkIdCode"), (String)map.get("icCode"), (String)map.get("orgCode"), (String)map.get("taxCode"));
		return result;
	}

	//追加签署人
	public static JSONObject sjdsendcontract(Map map)throws Exception {
		String signid=(String)map.get("signid");
		ReceiveUser[] userlist =(ReceiveUser[])map.get("userlist");
		JSONObject result = sdk.sjdsendcontract(signid, userlist);
		return result;
	}
	//自动签
	public static JSONObject AutoSignbyCA(Map map)throws Exception {
		String signid =(String)map.get("signid");
		String phone =(String)map.get("phone");
		int pagenum = (int)map.get("pagenum");
		float signx = (float)map.get("signx");
		float signy = (float)map.get("signy");
		String sealname = (String)map.get("sealname");
		boolean openflag =  (boolean)map.get("openflag");//true不可以再追加签署人，false可以追加签署人
		JSONObject result = sdk.AutoSignbyCA(signid, phone, pagenum, signx, signy, openflag,sealname);
		return result;
	}

	//合同详情,返回签名用户集合
	public static Map getContractInfo(String signid) throws Exception {
		Map userSignMap=new HashMap();
		JSONObject result = sdk.contractInfo(signid);
		JSONObject response = result.getJSONObject("response");
		JSONObject content = response.getJSONObject("content");
		JSONArray  signUserlist = content.getJSONArray("userlist");
		for (int i=0;i<signUserlist.size();i++){
			JSONObject userinfo = signUserlist.getJSONObject(i);
			JSONObject user = userinfo.getJSONObject("userinfo");
			String signtime1 = JSONObject.toJSONString(user.get("signtime"));
			JSONObject signtime =null;
			if(signtime1.length()>2){
				signtime =user.getJSONObject("signtime");
			}
			long time = (null==signtime?0l:(long) signtime.get("time"));
			userSignMap.put(user.get("mobile"),time);
		}
		return userSignMap;
	}

	//创建一份新合同用来进行测试
	private static JSONObject _createContract(ReceiveUser[] userlist) throws Exception {
		byte[] fileData = getResource("/demo.pdf");
//		ReceiveUser[] userlist = {new ReceiveUser(email, "Test1", phone, Constants.USER_TYPE.PERSONAL, Constants.CONTRACT_NEEDVIDEO.NONE, false)};
		SendUser senduser = new SendUser("22345678@163.com", "Test2", "13912345678", 3, false, Constants.USER_TYPE.PERSONAL, false, "title", "");
		JSONObject lastContinfoList = sdk.sjdsendcontractdocUpload(userlist, senduser, fileData);
		return lastContinfoList;
	}

	//提取lastContinfoList的signid
	private static String _getLastContractId(JSONObject lastContinfoList) {
		if (lastContinfoList == null) {
			return "";
		}
		JSONObject response = lastContinfoList.getJSONObject("response");
		JSONObject content = response.getJSONObject("content");
		JSONArray continfoList = content.getJSONArray("contlist");
		JSONObject contractInfo = continfoList.getJSONObject(0);
		contractInfo = contractInfo.getJSONObject("continfo");
		String signid = contractInfo.getString("signid");
		return signid;
	}

	private static byte[] getResource(String path) throws IOException {
		InputStream s = ShangshangqianUtil.class.getResourceAsStream(path);
		ArrayList<byte[]> bufferList = new ArrayList<byte[]>();
		byte[] buffer = new byte[4096];
		int read = 0;
		int total = 0;
		while ((read = s.read(buffer)) > 0) {
			byte[] b = new byte[read];
			System.arraycopy(buffer, 0, b, 0, read);
			bufferList.add(b);
			total += read;
		}
		s.close();
		byte[] result = new byte[total];
		int pos = 0;
		for (int i = 0; i < bufferList.size(); i++) {
			byte[] b = bufferList.get(i);
			System.arraycopy(b, 0, result, pos, b.length);
			pos += b.length;
		}
		return result;
	}
	/**
	 * 下载pdf
	 * @param signId
	 * @return
	 */
	public static  byte[] dowloadPdf(String signId) throws Exception {
		byte[] result;
		result = sdk.contractDownloadMobile(signId);
		return result;
	}

	public static JSONObject sjdsendcontractdocUpload(ReceiveUser[] userlist, SendUser senduser, byte[] fileData)  throws Exception {
		JSONObject lastContinfoList = sdk.sjdsendcontractdocUpload(userlist, senduser, fileData);
		return lastContinfoList;
	}

	/**
	 *
	 * @return
	 */
	public static  byte[] dowloadImg(String url) throws Exception  {
		byte[] result;
		HttpSender httpSender = new HttpSender();
		Map executorResult = httpSender.get(url, true);
		byte[] response = (byte[])executorResult.get("response");
		return response;
	}

	/**
	 * 上传图片
	 */
	public static JSONObject uploaduserimage(String useracount,String usermobile, String imgtype, String image, String imgName, String username, Constants.USER_TYPE usertype) throws InvalidKeyException, KeyManagementException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, IOException, BizException, ExecuteException {
		byte[] imageFile = null;
		try {
			imageFile = dowloadImg(image);
		}catch (Exception e){
		}
		String[] strs=image.split("\\.");
		imgtype =strs[1];

		String[] fileNames=image.split("/");
		imgName = fileNames[fileNames.length-1];

	/*	String aa = "13632415719";
		JSONObject bb = sdk.queryuserimage(usermobile, "");
		JSONObject a = (JSONObject) bb.get("response");
		JSONObject b = (JSONObject) a.get("content");
		JSONObject c = (JSONObject) b.get("companyuser");
		String d =  (String) c.get("image");
		byte[] r = Base64.decodeBase64(d);

		File fss = new File("D:\\aa.png");
		FileUtils.writeByteArrayToFile(fss, r);*/
		JSONObject result = sdk.uploaduserimage(useracount,usermobile,imgtype,imageFile,imgName,username,usertype);
		return result;
	}


}
