package com.fenlibao.p2p.controller.v_4.v_4_2_4.user;

import com.fenlibao.p2p.common.util.encrypt.AES;
import com.fenlibao.p2p.model.entity.GraphValidateCode;
import com.fenlibao.p2p.model.entity.SmsValidcode;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.enums.CaptchaType;
import com.fenlibao.p2p.model.exception.BusinessException;
import com.fenlibao.p2p.model.global.*;
import com.fenlibao.p2p.service.GraphCodeService;
import com.fenlibao.p2p.service.UserInfoService;
import com.fenlibao.p2p.service.UserTokenService;
import com.fenlibao.p2p.service.sms.SmsExtracterService;
import com.fenlibao.p2p.util.CommonTool;
import com.fenlibao.p2p.util.DateUtil;
import com.fenlibao.p2p.util.api.redis.RedisFactory;
import com.fenlibao.p2p.util.loader.Sender;
import com.fenlibao.p2p.util.redis.RedisConst;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证码
 */
@RestController("v_4_2_4/VerifyCodeController")
@RequestMapping(value = "vcode", headers = APIVersion.v_4_2_4)
public class VerifyCodeController {

	private static final Logger logger=LogManager.getLogger(VerifyCodeController.class);
	
	@Resource
	private GraphCodeService graphCodeService;
	@Resource
	private UserTokenService userTokenService;
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private SmsExtracterService smsService;

	@RequestMapping(value = "image", method = RequestMethod.GET)
	public HttpResponse image(@ModelAttribute BaseRequestForm  paramForm,
			@RequestParam(required = false, value="key") String key) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		
		HttpResponse response = new HttpResponse();
		if (!paramForm.validate() || StringUtils.isBlank(key)) {
			response.setCodeMessage(ResponseCode.EMPTY_PARAM);
            return response;
		}
		
		
				 
				try {
					// 验证码图片的宽度。
					final int width = 90;
					// 验证码图片的高度。
					final int height = 35;
					// 验证码字符个数
					final int codeCount = 4;

					int xPerFont = 0;
					// 字体高度
					final int fontHeight;
					final int codeY;

					xPerFont = width / (codeCount + 1);
					fontHeight = height - 2;
					codeY = height - 6;

					// 定义图像buffer
					final BufferedImage buffImg = new BufferedImage(width, height,
							BufferedImage.TYPE_INT_RGB);
					final Graphics2D g = buffImg.createGraphics();

					// 创建一个随机数生成器类
					final Random random = new Random();

					// 将图像填充为Color(0xF0,0xF3,0xF8)
					g.setColor(new Color(0xF0, 0xF3, 0xF8));
					g.fillRect(0, 0, width, height);

					// 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
					g.setColor(Color.gray);
					g.setStroke(new BasicStroke(1f));
					for (int i = 0; i < 3; i++) {
						final int x = random.nextInt(width);
						final int y = random.nextInt(height);
						final int xl = random.nextInt(112);
						final int yl = random.nextInt(112);
						g.drawLine(x, y, x + xl, y + yl);
					}

					// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
					final StringBuffer randomCode = new StringBuffer();

					g.setColor(CommonTool.randomColor(100, 100, 100));
					// 随机产生codeCount数字的验证码。
					for (int i = 0; i < codeCount; i++) {
						// 得到随机产生的验证码数字。
						final String strRand = CommonTool.randomNumber();

						final AffineTransform origXform = g.getTransform();
						final AffineTransform newXform = (AffineTransform) (origXform.clone());
						newXform.rotate(CommonTool.randomAngle(30), (i + 1) * xPerFont - 5,
								codeY);
						g.setTransform(newXform);

						// 用随机产生的颜色将验证码绘制到图像中。
						g.setFont(CommonTool.randomFont(26, 10));
						g.drawString(strRand, (i + 1) * xPerFont - 5, codeY);
						g.setTransform(origXform);

						// 将产生的四个随机数组合在一起。
						randomCode.append(strRand);
					}
					
					Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 10 * 60 * 1000);//验证码过期时间10分钟
					
					GraphValidateCode code = new GraphValidateCode();
					code.setVcode(randomCode.toString());
					code.setVkey(key);
					code.setCreateTime(new Date());
					code.setOutTime(outTime);
					this.graphCodeService.addGraphCode(code);
					
					ByteArrayOutputStream  sos = new ByteArrayOutputStream();
					ImageIO.write(buffImg, "jpeg", sos);
					 byte[] bytes = sos.toByteArray();
					BASE64Encoder encoder = new BASE64Encoder();
					String imageBase64 = encoder.encodeBuffer(bytes);
					response.setMessage(imageBase64);
					sos.close();
				} catch (IOException e) {
					response.setCodeMessage(ResponseCode.FAILURE);
					logger.error("[VerifyCodeController.image]输出图形验证码异常:"+e.getMessage(),e);
				}
				
				return response;
	}
	
	@RequestMapping(value = "verify", method = RequestMethod.POST)
	public HttpResponse verify(@ModelAttribute BaseRequestForm  paramForm,
			@RequestParam(required = false, value="key") String key,
			@RequestParam(required = false, value="vcode") String vcode) {
		
		HttpResponse response = new HttpResponse();
		
		try {
			if (!paramForm.validate() || StringUtils.isBlank(key)||StringUtils.isBlank(vcode)) {
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
	            return response;
			}
			
			GraphValidateCode code = this.graphCodeService.getGraphValidateCode(key);
			if(null == code || !vcode.equalsIgnoreCase(code.getVcode())){
				response.setCodeMessage(ResponseCode.COMMON_GRAPHICS_CAPTCHA_INVALID);
				return response;
			}
			
			if(code.getOutTime().getTime()<DateUtil.nowDate().getTime()||code.getStatus()==1){
				response.setCodeMessage(ResponseCode.COMMON_GRAPHICS_CAPTCHA_TIMEOUT);
				return response;
			}
			this.graphCodeService.updateGraphCode(key,1);
			
			//生成KEY存入REDIS并返回客户端
			String uuid =UUID.randomUUID().toString().replaceAll("-","");
			String redisKey = RedisConst.$CAPTCHA_KEY.concat(uuid);
			try (Jedis jedis = RedisFactory.getResource();) {
				jedis.set(redisKey,uuid);
				jedis.expire(redisKey, RedisConst.$VERIFY_CODE_KEY_TIMEOUT);
			}
			
			response.setMessage(uuid);
		} catch (Exception ex) {
			response.setCodeMessage(ResponseCode.FAILURE);
            logger.error("[VerifyCodeController.verify]"+ex.getMessage(), ex);
		}
		return response;
	}
	
	/**
	 * 获取验证码
	 * @param request
	 * @param paramForm
	 * @param token
	 * @param phoneNum
	 * @param type
	 * @param userIp
	 * @param key
	 * @return
	 */
	@RequestMapping(value = "get", method = RequestMethod.POST)
	public HttpResponse getVerifyCode(HttpServletRequest request, 
			@ModelAttribute BaseRequestForm  paramForm, String token,
			String phoneNum, String type, String userIp, String key) {
		logger.debug("request paramter[phoneNum:{},userIp:{},clientType:{}]",new Object[]{phoneNum,userIp,paramForm.getClientType()});
		HttpResponse response = new HttpResponse();
		try{
			if(!paramForm.validate() || StringUtils.isBlank(key)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM.getCode(),ResponseCode.EMPTY_PARAM.getMessage());
                return response;
			}
			
			if (!validateKey(key)) {
				response.setCodeMessage(ResponseCode.COMMON_CAPTCHA_KEY_WRONG.getCode(),
						ResponseCode.COMMON_CAPTCHA_KEY_WRONG.getMessage());
				return response;
			}

			//暂时先不校验token，token校验要和userId一起
//			if(StringUtils.isNotEmpty(token)){
//				if(!userTokenService.checkToken(token)){
//					response.setCodeMessage(ResponseCode.NOT_VALID_TOKEN.getCode(),ResponseCode.NOT_VALID_TOKEN.getMessage());
//					return response;
//				}
//			}
			
			if(StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(type)){
				response.setCodeMessage(ResponseCode.EMPTY_PARAM);
                return response;
			}
			
			String phone=AES.getInstace().decrypt(phoneNum);//解密后的手机号
			
			int codeType = 0;

			if(StringUtils.isNumeric(replaceBlank(type))){
				codeType = Integer.valueOf(replaceBlank(type));
			}
			
			/**
			 * 注册：手机号不能已注册
			 * 找回密码：手机号要已注册
			 */
			boolean isExist = userInfoService.checkPhone(phone);
			if(codeType == InterfaceConst.REGISTER_TYPE){
				if(isExist){
					response.setCodeMessage(ResponseCode.USER_PHONE_REGISTERED);
					return response;
				}
			}
			if(codeType == InterfaceConst.RETRIEVE_PASSWORD_TYPE){
				if(!isExist){
					response.setCodeMessage(ResponseCode.USER_NOT_EXIST.getCode(),ResponseCode.USER_NOT_EXIST.getMessage());
					return response;
				}
			}
			
			/**
			 * 手机号：1小时 2次       一天5次
			 *   IP：半小时5次        一天20次
			 */
			Date halfhour = DateUtil.minuteAdd(new Date(), -30);
			Date hour = DateUtil.minuteAdd(new Date(), -60);
			Date curDate = DateUtil.getDateFromDate(DateUtil.nowDate());
			int phoneSendCountByHour = this.userInfoService.getSendSmsCount(null, phone, hour, null);
			int ipSendCountByHalfour = this.userInfoService.getSendSmsCount(userIp, null, halfhour, null);
			int phoneSendCountByDay = this.userInfoService.getSendSmsCount(null, phone, null, curDate);
			int ipSendCountByDay = this.userInfoService.getSendSmsCount(userIp, null,null,curDate);
			
			if(phoneSendCountByDay >= Integer.parseInt(Sender.get("phone.send.day.maxcount"))||
				      ipSendCountByDay >= Integer.parseInt(Sender.get("ip.send.day.maxcount"))){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_MAXTIME);
				return response;
			}
			
			if(phoneSendCountByHour >= Integer.parseInt(Sender.get("phone.send.hour.maxcount"))){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_HOUR_MAXTIME);
				return response;
			}
			
			if(ipSendCountByHalfour >= Integer.parseInt(Sender.get("ip.send.halfhour.maxcount"))){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_HALFOUR_MAXTIME);
				return response;
			}
			
			int count=userInfoService.getSendPhoneCount(phone, codeType);
			if(count>Integer.parseInt(Sender.get("user.send.maxcount"))){
				response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_MAXTIME);
				return response;
			}
			
			//所有验证码发送间隔时间60秒，否则容易造成短信通道被封
			SmsValidcode smsValidCode = userInfoService.getLastSmsCode(phone,String.valueOf(codeType));
			if(smsValidCode!= null){
				long curTime = System.currentTimeMillis();
				if(curTime < (smsValidCode.getOutTime().getTime()-(30*60*1000)+60000)){
					response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_FREQUENTLY);
					return response;
				}
			}
			
			UserInfo userInfo=this.userInfoService.getUser(phone, null, null);
			
			if(!paramForm.getClientType().equals(String.valueOf(Constant.CLIENTTYPE_WAP))&&!paramForm.getClientType().equals(String.valueOf(Constant.CLIENTTYPE_WEIXIN))){
				userIp = CommonTool.getIpAddr(request);
			}
			if(userInfo!=null){
				userInfoService.sendVerifySms(phone, codeType,userInfo.getUserId(),userIp);
			}else{
				userInfoService.sendVerifySms(phone, codeType,null,userIp);
			}

//			UserInfo userInfo=this.userInfoService.getUser(phone, null, null);
//			
//			if(null!=userInfo){
//				//发送手机认证短信的次数
//				int count=userInfoService.getSendPhoneCount(Integer.parseInt(userInfo.getUserId()), Integer.parseInt(type));
//				if(count>Integer.parseInt(Sender.get("user.send.maxcount"))){
//					response.setCodeMessage(ResponseCode.COMMON_PHONE_CAPTCHA_MAXTIME);
//                    return response;
//				}
//				userInfoService.sendVerifySms(phone, Integer.parseInt(type),userInfo.getUserId());
//			}else{
//				userInfoService.sendVerifySms(phone, Integer.parseInt(type),null);
//			}
		}catch(Exception ex){
			response.setCodeMessage(ResponseCode.FAILURE);
            logger.error("[UserApiController.getVerifyCode]"+ex.getMessage(), ex);
		}
		return response;
	}
	
	public String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
	
	public boolean validateKey(String key) throws Exception {
		boolean isExist = false;
		key = RedisConst.$CAPTCHA_KEY.concat(key);
		try (Jedis jedis = RedisFactory.getResource()) {
			isExist = jedis.exists(key);
			if (isExist) {
				jedis.del(key);
			}
		}
		return isExist;
	}

	/**
	 * 手机验证码校验
	 * @param params
	 * @param phoneNum
	 * @param captcha
	 * @param type
     * @return
     */
	@RequestMapping(value = "validate", method = RequestMethod.PUT)
	public HttpResponse validate(BaseRequestForm params,
								 String phoneNum, String captcha, String type) {
		HttpResponse response = new HttpResponse();
		try {
			if (!params.validate() || !StringUtils.isNoneBlank(phoneNum, captcha, type)) {
				throw new BusinessException(ResponseCode.EMPTY_PARAM);
			}
			phoneNum = AES.getInstace().decrypt(phoneNum);
			CaptchaType captchaType = CaptchaType.getByCode(Integer.valueOf(type));
			if (captchaType != null) {
				smsService.captchaValidate(phoneNum, captcha, type);
			} else {
				response.setCodeMessage(ResponseCode.COMMON_CAPTCHA_TYPE_ERROR);
			}
		} catch (BusinessException busi) {
			logger.debug("手机验证码校验失败，phoneNum=[{}],captcha=[{}],type=[]", phoneNum, captcha, type);
			response.setCodeMessage(busi);
		} catch (Exception e) {
			logger.error("手机验证码校验失败，phoneNum=[{}],captcha=[{}],type=[]", phoneNum, captcha, type);
			response.setCodeMessage(ResponseCode.FAILURE);
		}
		return response;
	}
}
