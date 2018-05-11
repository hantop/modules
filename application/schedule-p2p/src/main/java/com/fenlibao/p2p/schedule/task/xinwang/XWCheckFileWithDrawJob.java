package com.fenlibao.p2p.schedule.task.xinwang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.fenlibao.p2p.dao.SendSmsRecordDao;
import com.fenlibao.p2p.dao.SendSmsRecordExtDao;
import com.fenlibao.p2p.dao.xinwang.checkfile.XWCheckfileDownloadDao;
import com.fenlibao.p2p.model.entity.SendSmsRecord;
import com.fenlibao.p2p.model.entity.SendSmsRecordExt;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.xinwang.checkfile.CheckfileDateStatus;
import com.fenlibao.p2p.model.xinwang.config.XinWangConfig;
import com.fenlibao.p2p.service.bid.PlanExtService;
import com.fenlibao.p2p.service.xinwang.checkfile.XWCheckfileDownloadService;
import com.fenlibao.p2p.util.loader.Config;
import com.fenlibao.p2p.util.xinwang.HttpUtil;
import com.fenlibao.p2p.util.xinwang.SignatureAlgorithm;
import com.fenlibao.p2p.util.xinwang.SignatureUtil;
import com.fenlibao.p2p.util.xinwang.XinWangUtil;

/**
 * Created by Administrator on 2017/8/29.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class XWCheckFileWithDrawJob  extends QuartzJobBean {

    private static int flag=0;
    private final static Logger logger = LoggerFactory.getLogger(XWCheckFileWithDrawJob.class);
    /**
     * 网关接口后缀
     */
    private final static String DOWNLOAD = "/download";
    public static XinWangConfig CONFIG;

    @Resource
    XWCheckfileDownloadService checkfileDownloadService;

    @Resource
    PlanExtService planExtService;

    @Resource
    XWCheckfileDownloadDao checkfileDownloadDao;
    static {
        CONFIG = ConfigFactory.create(XinWangConfig.class);
    }
    @Resource
    private SendSmsRecordDao sendSmsRecordDao;
    @Resource
    private SendSmsRecordExtDao sendSmsRecordExtDao;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        try {
			logger.info("===============执行XWCheckFileWithDrawJob============");

			Calendar cal=Calendar.getInstance();

			cal.add(Calendar.DATE,-1);
			Date date=cal.getTime();

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateString = formatter.format(cal.getTime());

			dateString=formatter.format(date);

			
//        dateString="20170814";
//        flag++;
//        if(flag!=1){
//            logger.info("---------------执行定时任务了-----"+flag);
//            return;
//
//        }
			CheckfileDateStatus cfs = new CheckfileDateStatus();
			try {
			    cfs.setCheckfileDate(formatter.parse(dateString));
			} catch (ParseException e) {
			    e.printStackTrace();
			}
			CheckfileDateStatus cfss =checkfileDownloadService.getCheckFileStatus(cfs);

			if(cfss!=null&&cfss.getStatus()==3){
			   logger.info("==============日期："+dateString+"的对账数据已完成导入，无需重复导入========");
			   return;
			}
			HttpResponse httpResponse = new HttpResponse();
			// 定义reqData参数集合
			Map<String, Object> reqData = new HashMap<String, Object>();
			reqData.put("fileDate", dateString);
			// 必须添加的参数
			reqData.put("timestamp", format.format(new Date()));
			String url = CONFIG.url() + this.DOWNLOAD;

			CloseableHttpResponse response = null;
			try {
			    List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();
			    BasicNameValuePair bn1 = new BasicNameValuePair("serviceName", "DOWNLOAD_CHECKFILE");
			    BasicNameValuePair bn2 = new BasicNameValuePair("platformNo", CONFIG.platformNo());
			    BasicNameValuePair bn3 = new BasicNameValuePair("reqData", JSON.toJSONString(reqData));
			    BasicNameValuePair bn4 = new BasicNameValuePair("keySerial", CONFIG.keySerial());
			    BasicNameValuePair bn5 = new BasicNameValuePair("sign", sign(JSON.toJSONString(reqData)));
			    formparams.add(bn1);
			    formparams.add(bn2);
			    formparams.add(bn3);
			    formparams.add(bn4);
			    formparams.add(bn5);
			        response = HttpUtil.post(url, formparams);
			        InputStream in=response.getEntity().getContent();
			        XinWangUtil.downloadCheckFile(in);
			}
			catch(Exception e){
			    e.printStackTrace();
			    logger.error("下载对账文件出错", e);
			}

			try {
			        readfile(CONFIG.checkFileSavePath() , dateString);

			}catch (Exception e){
			    logger.error("对账文件入库出错", e);
			}

			try {
			    compareData(dateString);
			} catch (Exception e) {
			    e.printStackTrace();
			    logger.info("=========匹配数据库数据进行对账时报错==========");
			}

			logger.info("===============执行XWCheckFileWithDrawJob完成============"+dateString);
		} catch (Exception e) {
			logger.info("===============执行XWCheckFileWithDrawJob异常============"+e.getMessage());
			e.printStackTrace();
		}

    }




    /**
     * 读取某个文件夹下的所有文件
     */
    private void readfile(String filepath,String dateString) throws FileNotFoundException, IOException {
        filepath = filepath+File.separator+dateString;
        CheckfileDateStatus cfs = new CheckfileDateStatus();
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {
                logger.info("文件");
                logger.info("path=" + file.getPath());
                logger.info("absolutepath=" + file.getAbsolutePath());
                logger.info("name=" + file.getName());

            } else if (file.isDirectory()) {
                logger.info("文件夹");
                String[] filelist = file.list();


                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                try {
                    cfs.setCheckfileDate(sdf.parse(dateString));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    checkfileDownloadService.insertCheckFileStatus(cfs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath +File.separator+ filelist[i]);
                    if (!readfile.isDirectory()) {
                        logger.info("path=" + readfile.getPath());
                        logger.info("absolutepath="+ readfile.getAbsolutePath());
                        logger.info("name=" + readfile.getName());
                        readFileByLines(readfile,dateString);
                    } else if (readfile.isDirectory()) {
                        readfile(filepath +File.separator+ filelist[i],dateString);
                    }

                }
                cfs.setWithDrawStatus(3);
//                cfs.setUserStatus(3);
//                cfs.setTransactionStatus(3);
                cfs.setRechargeStatus(3);
//                cfs.setCommissionStatus(3);
//                cfs.setBackRollRechargeStatus(3);
                CheckfileDateStatus  update = checkfileDownloadService.getCheckFileStatus(cfs);
                if(update!=null){
                    update.setStatus(3);
                    checkfileDownloadService.updateCheckFileStatus(update);
                }

            }

        } catch (FileNotFoundException e) {
            logger.info("readfile()   Exception:" + e.getMessage());
        }
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    @Transactional
    private void readFileByLines(File file,String dateString) {
        CheckfileDateStatus cfs = new CheckfileDateStatus();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            cfs.setCheckfileDate(sdf.parse(dateString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            logger.info("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                logger.info("line " + line + ": " + tempString);
                if(line >1) {
                    checkfileDownloadService.insertCheckfile(tempString, file.getName());
                }
                line++;
            }
//            if(file.getName().contains("BACKROLL_RECHARGE.txt")){
//                cfs.setBackRollRechargeStatus(3);
//            }
//            if(file.getName().contains("COMMISSION.txt")){
//                cfs.setCommissionStatus(3);
//            }
            if(file.getName().contains("RECHARGE.txt")){
                cfs.setRechargeStatus(3);
                checkfileDownloadService.updateCheckFileStatus(cfs);
            }
//            if(file.getName().contains("TRANSACTION.txt")){
//                cfs.setTransactionStatus(3);
//            }if(file.getName().contains("USER.txt")){
//                cfs.setUserStatus(3);
//            }
            if(file.getName().contains("WITHDRAW.txt")){
                cfs.setWithDrawStatus(3);
                checkfileDownloadService.updateCheckFileStatus(cfs);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error("对账文件,插入数据出错");
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 比较数据,有差异则发送短信
     * @param
     */
    public void compareData(String dateString){
        List<Map<String,Object>> diffData = checkfileDownloadService.getCheckWithDrawDiffData(dateString);
        logger.info("提现异常对账列表diffData:"+diffData);
        if(CollectionUtils.isNotEmpty(diffData)){
        	this.prepareToSendMsg(dateString, "提现", diffData);
        }

        
        List<Map<String,Object>> diffRechargeData = checkfileDownloadService.getCheckRechargeDiffData(dateString);
        logger.info("充值异常对账列表diffRechargeData:"+diffRechargeData);
        if(CollectionUtils.isNotEmpty(diffRechargeData)){
        	this.prepareToSendMsg(dateString, "充值", diffData);
        }



    }

    /**
     * 发送短信前准备
     * @param dateStr
     * @param content
     */
    private void prepareToSendMsg(String dateString,String type,List<Map<String,Object>> diffData){
    	Map<String,Object> data =diffData.get(0);
    	String content ="【"+dateString+"】"+type +"对账异常,流水号【"+data.get("f_request_no")+"】等，总共"+diffData.size()+"条数据异常";
    	String mobiles = Config.get("auto.xinwang.checkfile.mobile");//收短信的人
    	logger.info("=======对账收信手机号：====="+mobiles);
    	if(mobiles!=null){
    		String[] mobileList = mobiles.split(",");
        	for (String phoneNum : mobileList) {
        		this.sendMsg(phoneNum,content);
    		}
    	}
    	
    }
    
    /**
     * 发送短信
     *
     * @param phoneNum
     * @param content
     */
    private void sendMsg(String phoneNum, String content) {
        Timestamp outTime = new Timestamp(System.currentTimeMillis() / 1000 * 1000 + 30 * 60 * 1000);//过期时间30分钟
        SendSmsRecord record = new SendSmsRecord();
        record.setContent(content);
        record.setCreateTime(new Date());
        record.setStatus("W");
        record.setType(0);
        record.setOutTime(outTime);
        sendSmsRecordDao.insertSendSmsRecord(record);

        SendSmsRecordExt recordExt = new SendSmsRecordExt();
        recordExt.setId(record.getId());
        recordExt.setPhoneNum(phoneNum);
        sendSmsRecordExtDao.insertSendSmsRecordExt(recordExt);
    }

    private static String sign(String reqData) throws Exception{
        String privateStr = CONFIG.privateKey();
        PrivateKey privateKey = SignatureUtil.getRsaPkcs8PrivateKey(Base64.decodeBase64(privateStr));
        byte[] sign = SignatureUtil.sign(SignatureAlgorithm.SHA1WithRSA,privateKey, reqData);
        String signBase64=Base64.encodeBase64String(sign);
        return signBase64;
    }


    public static void main(String[] args) {
    	Calendar cal=Calendar.getInstance();

		cal.add(Calendar.DATE,-1);
		Date date=cal.getTime();

		Config config = new Config();
		config.load();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(cal.getTime());

		String aaa=Config.get("redpacket.grantAccount");
		String privateStr = CONFIG.privateKey();
		
		dateString=formatter.format(date);
		System.out.println(config.get("auto.xinwang.checkfile.mobile"));
		System.out.println(1111);
		
		
	}
}
