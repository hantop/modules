package com.fenlibao.p2p.util.api.file;

import cn.bestsign.sdk.integration.utils.http.HttpSender;
import com.fenlibao.p2p.util.api.load.ApiUtilConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.io.*;
import java.util.*;

/**
 * 头像上传相关
 *
 */
public class FileUtils {

	private static final Logger logger=LogManager.getLogger(FileUtils.class);

	public static void main(String[] args) {
		File file = new File("F:\\fileEnpromers.pdf");
		System.out.println(file);
//		String str = saveFile(file, "http://192.168.40.236/resources", 12);
//		System.out.println(str);
//		byte[] da = downloadFile("7e2-1-3-c-7e9bf02cb0e44c038c2598bcad8a612f.pdf", "http://192.168.40.236/resources");
//		System.out.println(da.length);
	}

	public static String saveFile(InputStream in, String suffix, String servicePath, int type) {
		return saveFile(in, suffix, servicePath, null, type);
	}

	public static String saveFile(InputStream in, String suffix, String servicePath, String rename, int type) {
		String sealCode;
		try {
			sealCode = getSealCode(rename, suffix, type);
			String fileName = decryptSealCode(sealCode);
			HttpUpload httpUpload = new HttpUpload(servicePath, "/".concat(fileName));
			boolean isSuc = httpUpload.upladFile(in);
			if (isSuc) {
				return sealCode;
			}
		} catch (Exception e) {
			logger.error("保存文件异常：{}", e);
		}
		return null;
	}

	/**
	 * 保存文件到服务器
	 * @param file 文件
	 * @param servicePath 文件服务器路径
	 * @param type 文件所属类型 如：6为用户头像， 12为上上签
	 * @return 文件sealCode
	 */
	public static String saveFile(File file, String servicePath, int type) {
		return saveFile(file, servicePath, null, type);
	}

	/**
	 * 保存文件，自定义重命名
	 * @param file 文件
	 * @param servicePath 服务器路径
	 * @param rename 重名命名称
	 * @param type 文件所属类型 如：6为用户头像， 12为上上签
	 * @return 文件sealCode
	 */
	public static String saveFile(File file, String servicePath, String rename, int type) {
		String sealCode;
		// 该inputStream 由最后使用的人关闭
		InputStream inputStream;
		try {
			String orgFileName = file.getName();
			String suffix = orgFileName.substring(orgFileName.lastIndexOf("."));
			sealCode = getSealCode(rename, suffix, type);
			String fileName = decryptSealCode(sealCode);
			inputStream = new FileInputStream(file);
			HttpUpload httpUpload = new HttpUpload(servicePath, "/".concat(fileName));
			boolean isSuc = httpUpload.upladFile(inputStream);
			if (isSuc) {
				return sealCode;
			}
		} catch (Exception e) {
			logger.error("保存文件异常：{}", e);
		}
		return null;
	}

	/**
	 * 保存文件（若sealCode的文件事先存在则被覆盖）
	 * @param fileData 文件字节
	 * @param servicePath 服务器路径
	 * @param sealCode 文件code
	 * @return true: 保存成功 false:保存失败
	 */
	public static boolean saveFile(byte[] fileData, String servicePath, String sealCode) {
		try {
			String fileName = decryptSealCode(sealCode);
			// 该inputStream 由最后使用的人关闭
			InputStream inputStream = new ByteArrayInputStream(fileData);
			HttpUpload httpUpload = new HttpUpload(servicePath, "/".concat(fileName));
			return httpUpload.upladFile(inputStream);
		} catch (Exception e) {
			logger.error("保存文件异常，sealCode:{}, servicePath:{}, {}", sealCode, servicePath, e);
		}
		return false;
	}

	/**
	 * 下载文件
	 * @param sealCode 上传文件时生成的code
	 * @param servicePath 服务器路径
	 * @return 文件字节数组
	 */
	public static byte[] downloadFile(String sealCode, String servicePath) {
		try {
			String url = getFileURL(sealCode, servicePath);
			return downloadFile(url);
		} catch (Exception e) {
			logger.error("下载文件时异常：文件code:{}, 服务器根路径：{}， 异常信息：{}", sealCode, servicePath, e);
		}
		return null;
	}

	/**
	 * 下载文件
	 * @param url 文件路径（譬如：http://localhost:8013/console/fileStore/11/2018/1/3/6043.png）
	 * @return 文件字节数组
	 */
	public static byte[] downloadFile(String url) {
		try {
			HttpSender httpSender = new HttpSender();
			Map executorResultMap = httpSender.get(url, true);
			byte[] response = (byte[])executorResultMap.get("response");
			return response;
		} catch (Exception e) {
			logger.error("下载文件：{} 时异常{}", url, e);
		}
		return null;
	}

	/**
	 * 获取文件在服务器的路径
	 * @param sealCode 文件code
	 * @param servicePath 服务器根路径
	 * @return 文件路径 如：http://localhost:8013/console/fileStore/11/2018/1/3/6043.png
	 */
	public static String getFileURL(String sealCode, String servicePath) {
		try {
			String str = decryptSealCode(sealCode);
			if (StringUtils.isNotEmpty(str) && StringUtils.isNotEmpty(servicePath)) {
				return servicePath.concat("/").concat(str); //
			}
		} catch (Exception e) {
			logger.error("获取文件url异常：sealCode:{}, servicePath:{}, {}", sealCode, servicePath, e);
		}
		return null;
	}

	/**
	 * 获取文件code
	 * @param rename 重名命 不可重复(如果为空则随机uuid)
	 * @param suffix 文件后缀 不可为空
	 * @param type 文件所属类型，如：用户头像为 6 ，上上签为：12
	 * @return
	 */
	public static String getSealCode(String rename, String suffix, int type) {
		StringBuilder builder = new StringBuilder();
		try {
			DateTime dateTime = DateTime.now();
			if (StringUtils.isEmpty(rename)) {
				rename = UUID.randomUUID().toString().replace("-", "");
			}
			builder.append(Integer.toHexString(dateTime.getYear()))
					.append('-')
					.append(Integer.toHexString(dateTime.getMonthOfYear()))
					.append('-')
					.append(Integer.toHexString(dateTime.getDayOfMonth()))
					.append('-')
					.append(Integer.toHexString(type == 0 ? 0 : type))
					.append('-')
					.append(rename);
			if (suffix.charAt(0) != '.') {
				builder.append('.');
			}
			builder.append(suffix);

			return builder.toString();
		} catch (Exception e) {
			logger.error("获取文件code时异常：{}", e);
		}
		return null;
	}

	/**
	 * 将sealCode解密
	 * @param sealCode
	 * @return 解密后拼接成的字符串
	 */
	public static String decryptSealCode(String sealCode) {

		if(StringUtils.isEmpty(sealCode)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		try {
			int end = sealCode.lastIndexOf('.');
			String suffix = sealCode.substring(end + 1);
			sealCode = sealCode.substring(0, end);
			String[] items = sealCode.split("-");
			int year = Integer.parseInt(items[0], 16);
			int month = Integer.parseInt(items[1], 16);
			int day = Integer.parseInt(items[2], 16);
			int fileType = Integer.parseInt(items[3], 16);
			String fileName = items[4];
			sb.append(fileType).append("/")
					.append(year).append("/")
					.append(month).append("/")
					.append(day).append("/")
					.append(fileName)
					.append(".")
					.append(suffix);
			return sb.toString();
		} catch (Exception e) {
			logger.error("解密文件code:{} 异常：{}", sealCode, e);
		}
		return null;
	}

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

            HttpUpload upload=new HttpUpload(ApiUtilConfig.get("resources.server.path"), ApiUtilConfig.get("user.avatar.path")+fileName);
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
