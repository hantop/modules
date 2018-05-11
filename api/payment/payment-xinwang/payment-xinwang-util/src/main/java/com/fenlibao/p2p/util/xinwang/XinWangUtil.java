package com.fenlibao.p2p.util.xinwang;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.xinwang.config.XinWangConfig;
import com.fenlibao.p2p.model.xinwang.enums.account.SysFundAccountType;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralResponseCode;
import com.fenlibao.p2p.model.xinwang.enums.common.GeneralStatus;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.util.api.DateUtil;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Administrator on 2017/5/4.
 */
public class XinWangUtil {
    private static final Logger LOG = LogManager.getLogger(XinWangUtil.class);

    public static XinWangConfig CONFIG;

    static {
        CONFIG = ConfigFactory.create(XinWangConfig.class);
    }

    private final static String GATEWAY = "/gateway";
    private final static String SERVICE = "/service";
    private final static String DOWNLOAD = "/download";

    public static String serviceRequest(String serviceName, Map<String,Object> reqDataMap) throws Exception {
        String url=CONFIG.url() + SERVICE;
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        BasicNameValuePair param1 = new BasicNameValuePair("platformNo",CONFIG.platformNo());
        BasicNameValuePair param2 = new BasicNameValuePair("keySerial",CONFIG.keySerial());
        BasicNameValuePair param3 = new BasicNameValuePair("serviceName",serviceName);
        String reqData = JSON.toJSONString(reqDataMap);
        BasicNameValuePair param4 = new BasicNameValuePair("reqData",reqData);
        BasicNameValuePair param5 = new BasicNameValuePair("sign",sign(reqData));
        params.add(param1);params.add(param2);params.add(param3);params.add(param4);params.add(param5);
        String result = "";
        CloseableHttpResponse response = null;
        try {
            response = HttpUtil.post(url, params);
            result = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
        finally {
            IOUtils.closeQuietly(response);
        }

        try {
            verifySign(response, result);
        } catch (Exception e) {
            LOG.error("verify sign fail..... " + e.getMessage());
            throw new XWTradeException(XWResponseCode.COMMON_VERIFY_SIGN_FAIL);
        }

        return result;
    }

    public static Map<String,Object> gatewayRequest(String serviceName, Map<String,Object> reqDataMap) throws Exception{
        String url=CONFIG.url() + GATEWAY;
        Map<String,Object> map=new HashMap<>();
        List<Map<String,String>> postParams=new ArrayList<>();
        Map<String,String> param1=new HashMap<>(2);
        param1.put("key","platformNo");
        param1.put("value",CONFIG.platformNo());
        Map<String,String> param2=new HashMap<>(2);
        param2.put("key","keySerial");
        param2.put("value",CONFIG.keySerial());
        Map<String,String> param3=new HashMap<>(2);
        param3.put("key","serviceName");
        param3.put("value",serviceName);
        String reqData = JSON.toJSONString(reqDataMap);
        Map<String,String> param4=new HashMap<>(2);
        param4.put("key","reqData");
        param4.put("value",reqData);
        Map<String,String> param5=new HashMap<>(2);
        param5.put("key","sign");
        param5.put("value",sign(reqData));
        Map<String,String> param6=new HashMap<>(2);
        param6.put("key","userDevice");
        param6.put("value","");
        postParams.add(param1);postParams.add(param2);postParams.add(param3);
        postParams.add(param4);postParams.add(param5);postParams.add(param6);
        map.put("postParams",postParams);
        map.put("postUrl",url);
        return map;
    }

    public static void verifySign(CloseableHttpResponse response, String responseData) throws Exception {
        Map<String, Object> respMap = JSON.parseObject(responseData);
        //接口返回code!=0 || status!=SUCCESS时，不做验签处理
        if( !"0".equals(respMap.get("code")) ||
                !"SUCCESS".equals(respMap.get("status"))) {
            return;
        }
        //校验返回报文
        String returnSign = "";
        Header[] headers = response.getHeaders("sign");
        if (headers != null && headers.length > 0) {
            Header header = headers[0];
            returnSign = header.getValue();
        }
        byte[] keyByte = Base64.decodeBase64(CONFIG.lmPublicKey());
        byte[] signByte = Base64.decodeBase64(returnSign);
        try {
            PublicKey publicKey = SignatureUtil.getRsaX509PublicKey(keyByte);
            boolean b = SignatureUtil.verify(SignatureAlgorithm.SHA1WithRSA, publicKey, responseData, signByte);
            if (!b) {
                throw new Exception("验签失败，sign与respData不匹配");
            }
            //LOG.info("sign success ...");
        } catch (InvalidKeySpecException e) {
            throw new InvalidKeySpecException("验签错误，生成商户公钥失败", e);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("验签错误" + e.getMessage(), e);
        } catch (GeneralSecurityException e) {
            throw new GeneralSecurityException("验签错误" + e.getMessage(), e);
        }
    }

    private static String sign(String reqData) throws APIException{
        String privateStr = CONFIG.privateKey();
        PrivateKey privateKey = null;
        byte[] sign = new byte[0];
        try {
            privateKey = SignatureUtil.getRsaPkcs8PrivateKey(Base64.decodeBase64(privateStr));
            sign = SignatureUtil.sign(SignatureAlgorithm.SHA1WithRSA,privateKey, reqData);
        } catch (GeneralSecurityException e) {
            throw new XWTradeException(XWResponseCode.COMMON_VERIFY_SIGN_FAIL);
        }
        String signBase64=Base64.encodeBase64String(sign);
        return signBase64;
    }


    /**
     * 生成请求流水号
     * */
    public synchronized static String createRequestNo(){
        String uuid = UUID.randomUUID().toString();
        String randomString = uuid.substring(0,10);
        return DateUtil.getYYYYMMDDHHMMSS(new Date()) + randomString;
    }

    public static void downloadCheckFile(InputStream in) throws Exception {
        if(in==null)return;
        String checkFilePath=CONFIG.checkFileSavePath();
        ZipInputStream zipInputStream=null;
        try {
            zipInputStream = new ZipInputStream(in);
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String directoryName = zipEntry.getName().substring(0,zipEntry.getName().indexOf("_"));
                File f= new File(checkFilePath+File.separator+directoryName);
                f.mkdirs();
                int start=zipEntry.getName().indexOf("_",zipEntry.getName().indexOf("_")+1)+1;
                String txtFileName=zipEntry.getName().substring(start);
                File txtFile = new File(checkFilePath +File.separator+directoryName+File.separator+ txtFileName);
                if(zipEntry.getName().endsWith("/")){
                    txtFile.mkdirs();
                }else {
                    if(!txtFile.exists()){
                        FileOutputStream fileOutputStream=null;
                        try {
                            fileOutputStream = new FileOutputStream(txtFile);
                            byte[] buf = new byte[1024];
                            int len = -1;
                            while ((len = zipInputStream.read(buf)) != -1) {  // 直到读到该条目的结尾
                                fileOutputStream.write(buf, 0, len);
                            }
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        catch(Exception e){
                            throw e;
                        }
                        finally {
                            fileOutputStream.close();
                        }
                    }
                }
                zipInputStream.closeEntry();  //关闭该条目
            }
            zipInputStream.close();
        }
        catch(Exception e){
            LOG.error("保存对账文件出错",e);
            throw e;
        }
        finally {
            zipInputStream.close();
        }
    }

    /**
     * 校验返回结果是否正确
     * @param code
     * @param status
     * @return
     */
    public static boolean validate(Integer code, String status) {
        return code.equals(GeneralResponseCode.SUCCESS.getCode()) && status.equals(GeneralStatus.SUCCESS.getStatus());
    }

    public static Map<String, SysFundAccountType> getWLZHMap() {
        Map<String, SysFundAccountType> map = new HashMap<>();
        map.put(CONFIG.generalAccount(), SysFundAccountType.XW_PLATFORM_FUNDS_TRANSFER_WLZH);
        map.put(CONFIG.compensatoryAccount(), SysFundAccountType.XW_PLATFORM_COMPENSATORY_WLZH);
        map.put(CONFIG.marketingAccount(), SysFundAccountType.XW_PLATFORM_MARKETING_WLZH);
        map.put(CONFIG.shareAccount(), SysFundAccountType.XW_PLATFORM_PROFIT_WLZH);
        map.put(CONFIG.incomeAccount(), SysFundAccountType.XW_PLATFORM_INCOME_WLZH);
        map.put(CONFIG.dividendAccount(), SysFundAccountType.XW_PLATFORM_INTEREST_WLZH);
        map.put(CONFIG.replacementRechargeAccount(), SysFundAccountType.XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH);
        map.put(CONFIG.underwrittenAmount(), SysFundAccountType.XW_PLATFORM_URGENT_WLZH);
        map.put(UserRole.INVESTOR.getCode(), SysFundAccountType.XW_INVESTOR_WLZH);
        map.put(UserRole.BORROWERS.getCode(), SysFundAccountType.XW_BORROWERS_WLZH);
        map.put(UserRole.GUARANTEECORP.getCode(), SysFundAccountType.XW_GUARANTEECORP_WLZH);
        return map;
    }
}
