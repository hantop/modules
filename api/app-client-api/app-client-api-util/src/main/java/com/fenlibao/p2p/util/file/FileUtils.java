package com.fenlibao.p2p.util.file;

import com.fenlibao.p2p.util.loader.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 头像上传相关 迁移到 common-api模块进行维护
 *
 */
@Deprecated
public class FileUtils {

	private static final Logger logger=LogManager.getLogger(FileUtils.class);

    /**
     * 上传图片
     * @param input
     * @param fileInformation
     * @return
     */
	public static String savepic(InputStream input, FileInformation fileInformation) {
        try {
        	String fileName=getFile(fileInformation);
        	String fileUrl=fileInformation.getBasepath()+fileName;
        	//InputStream input = new FileInputStream(file);
        	//InputStream input = new ByteArrayInputStream(userPic);
            HttpUpload upload=new HttpUpload(Config.get("resources.server.path"), Config.get("user.avatar.path")+fileName);
            upload.upladFile(input);
            return fileUrl;
        } catch (Exception e) {
        	logger.error("Save Pic Error: " + e.getMessage(), e);
        }
        return null;
    }

	/**
	 * 生成图片名称
	 * @return
	 */
	public static String newCode(FileInformation information) {
        if(information.getFileType() < 0) {
        	information.setFileType(0);
        }

        StringBuilder builder = new StringBuilder();
        builder.append(Integer.toHexString(information.getYear())).append('-').append(Integer.toHexString(information.getMonth())).append('-').append(Integer.toHexString(information.getDay())).append('-').append(Integer.toHexString(information.getFileType() < 0 ? 0 : information.getFileType())).append('-').append(Integer.toHexString(information.getId()));
        if (information.getSuffix().charAt(0) != '.') {
        	builder.append('.');
        }
        builder.append(information.getSuffix());
        return builder.toString();
    }

	/**
	 * 生成file
	 * @param fileInformation
	 * @return
	 * @throws IOException
	 */
	public static String getFile(FileInformation fileInformation) throws IOException {
	     StringBuilder uri = new StringBuilder();
	     uri.append(fileInformation.getFileType()).append("/").append(fileInformation.getYear()).append("/").append(fileInformation.getMonth()).append("/").append(fileInformation.getDay()).append("/");
	     uri.append(fileInformation.getId());
	     String subffix = fileInformation.getSuffix();
	     if (StringUtils.isNotEmpty(subffix)) {
	         uri.append('.').append(subffix);
	     }
	    return uri.toString();
	}

	/**
	 * 根据文件名生成文件信息
	 * @param fileCode
	 * @return
	 */
	private static FileInformation getFileInformation(String fileCode,String picUrl){
		if(StringUtils.isEmpty(fileCode)){
			return null;
		}
	     int end = fileCode.lastIndexOf('.');
	     String suffix="";
	     if (end != -1) {
	       if (end + 1 < fileCode.length()) {
	         suffix = fileCode.substring(end + 1);
	       } else {
	         suffix = null;
	       }
	       fileCode = fileCode.substring(0, end);
	     } else {
	       suffix = null;
	     }
	     String[] items = fileCode.split("-");
	     if (items.length != 5) {
	       return null;
	     }
	     try {
	       final int year = Integer.parseInt(items[0], 16);
	       final int month = Integer.parseInt(items[1], 16);
	       final int day = Integer.parseInt(items[2], 16);
	       final int fileType = Integer.parseInt(items[3], 16);
	       final int id = Integer.parseInt(items[4], 16);

	       FileInformation fileInformation=new FileInformation();
	       fileInformation.setYear(year);
	       fileInformation.setMonth(month);
	       fileInformation.setDay(day);
	       fileInformation.setId(id);
	       fileInformation.setFileType(fileType);
	       fileInformation.setSuffix(suffix);
	       fileInformation.setPicurl(picUrl);
	       return fileInformation;
	    } catch (Throwable t) {}
	     return null;
	}

	/**
	 * 根据日期生成文件信息
	 * @param date
	 * @return
	 */
	public static FileInformation getByDate(Date date){
		Calendar ca=Calendar.getInstance();
		ca.setTime(date);
		FileInformation information=new FileInformation();
		information.setYear(Calendar.YEAR);
		information.setMonth(Calendar.MONTH + 1);
		information.setDay(Calendar.DATE);
		return information;
	}

    /**
     * 获取图片URL
     * @param fileCode
     * @return
     */
	public static String getPicURL(String fileCode,String picUrl)   {
		FileInformation fileInformation=getFileInformation(fileCode,picUrl);
	     if (fileInformation == null) {
	       return "";
	     }
	     StringBuilder url = new StringBuilder();
	     url.append(fileInformation.getPicurl()).append('/').append(fileInformation.getFileType()).append('/').append(fileInformation.getYear()).append('/').append(fileInformation.getMonth()).append('/').append(fileInformation.getDay()).append('/').append(fileInformation.getId());
	      
	     String subffix = fileInformation.getSuffix();
	     if (StringUtils.isNotEmpty(subffix)) {
	       url.append('.').append(subffix);
	     }
	     return url.toString();
    }
	
	/**
     * 读取文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static List<String> txt2String(File file){
        List<String> resultList = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            	resultList.add(s);
            }
            br.close();    
        }catch(Exception e){
        	logger.error("FileUtils txt2String error:"+e.getMessage(),e);
        }
        return resultList;
    }
    
}
