package com.fenlibao.p2p.util.file;

public class FileInformation {

	/**
	 * 文件附件信息中的ID
	 */
	private int id;
	
	private int year;
	
	private int month;
	
	private int day;
	
	/**
	 * 附件类型
	 */
	private int fileType;
	
	/**
	 * 附件后缀名
	 */
	private String suffix;
	
	/**
	 * 上传路径
	 */
	private String basepath;
	
	/**
	 * URL路径
	 */
	private String picurl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getBasepath() {
		return basepath;
	}

	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	
}
