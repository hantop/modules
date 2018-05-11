package com.fenlibao.common.pms.util.tool;

import com.fenlibao.common.pms.util.exception.ImportExcelException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 用POI导入导出excel
 */
public class POIUtil {

	public static <T>  void export(HttpServletResponse response, String headers[], String fieldNames[],List<T> dataArray) {
		export(response,headers,fieldNames,dataArray,new Timestamp(System.currentTimeMillis()).getTime() + "");
	}

	/**
	 * @param response
	 * @param headers
	 * @param dataArray
	 */
	public static <T>  void export(HttpServletResponse response, String headers[], String fieldNames[],List<T> dataArray,String fileName) {
		try {
			fileName = new String(((fileName == null ? new Timestamp(System.currentTimeMillis()).getTime()+ "" : fileName)+ ".xlsx").getBytes("UTF-8"), "iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setHeader("Content-disposition",
				"attachment;filename=" + fileName );
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		OutputStream outputStream = null;
		SXSSFWorkbook workbook = new SXSSFWorkbook(10000);
		try {
			outputStream = response.getOutputStream();
			if (outputStream == null || dataArray == null) {
				return;
			}
			// 生成一个表格
			Sheet sheet = workbook.createSheet();
			// 产生表格标题行
			Row row = sheet.createRow(0);
			for (short i = 0; i < headers.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(headers[i]);
			}
			// 导出数据
			int index = 1;
			List<String> fieldNameList= Arrays.asList(fieldNames);
			for (T item:dataArray) {
				if (item == null) {
					continue;
				}
				row = sheet.createRow(index);
				Field[] fs = item.getClass().getDeclaredFields();
				for (int k = 0; k < fs.length; k++) {
					Field f = fs[k];
					if(fieldNameList.contains(f.getName())){
						f.setAccessible(true);
						Object val = f.get(item);
						if(val==null){
							val="";
						}
						if(val.getClass() == Double.class) {
							row.createCell(fieldNameList.indexOf(f.getName())).setCellValue(Double.valueOf(val.toString()));
						} else {
							row.createCell(fieldNameList.indexOf(f.getName())).setCellValue("" + val);
						}
					}
				}
				index++;
			}
			workbook.write(outputStream);
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.dispose();
				workbook.close();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取导入数据
	 * @param file
	 * @param headers
	 * @param headerIndex 从0开始
	 * @return
	 * @throws Throwable
	 */
	public static  List<String[]> getImportedData(MultipartFile file,String headers[], int headerIndex) throws Throwable{
		List list=new ArrayList();
		if (!StringHelper.isEmpty(file.getContentType())
				&& (file.getContentType().equals("application/vnd.ms-excel") || file.getContentType()
				.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
			InputStream inputStream=file.getInputStream();
			Workbook workbook=null;
			if(inputStream!=null){
				if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					workbook = new XSSFWorkbook(inputStream);
				} else {
					workbook = new HSSFWorkbook(inputStream);
				}
			}
			else{
				throw new ImportExcelException("文件里没有数据");
			}
			Sheet sheet = workbook.getSheetAt(0);
			accordWithTemplet(sheet,headers, headerIndex);
			for (int rowNum = 1 + headerIndex; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row != null){
					String [] temp=new String[headers.length];
					for(int i=0;i<headers.length;i++){
						Cell cell=row.getCell(i);
						if(cell!=null){
							cell.setCellType(Cell.CELL_TYPE_STRING);
							temp[i]=getStringNoBlank(cell.getStringCellValue());
						}
					}
					//行中是否包含null
					if(Arrays.asList(temp).contains(null)){
						int j=0;
						for(;j<headers.length;j++){
							if(temp[j]!=null){
								break;
							}
						}
						//如果整行是null,跳过这行
						if(j>=headers.length){
							list.add(null);
							continue;
						}
						//如果不是整行都是null
						else{
							boolean flag=false;
							for(int i=0;i<headers.length;i++){
								if(temp[i]!=null){
									if(StringUtils.isNotBlank(temp[i])){
										flag=true;
										break;
									}
								}
							}
							//如果非null的值中存在不为空串的值
							if(flag){
								throw new ImportExcelException("导入的数据中有空值");
							}
							//如果非null值都是空串，跳过这行
							else {
								list.add(null);
								continue;
							}
						}
					}
					//如果不存在null值，但存在空串
					else if(Arrays.asList(temp).contains("")){
						boolean flag=false;
						for(int i=0;i<headers.length;i++){
							if(!"".equals(temp[i])){
								flag=true;
								break;
							}
						}
						//如果存在不为空串的值
						if(flag){
							throw new ImportExcelException("导入的数据中有空值");
						}
						//如果都是空串，跳过这行
						else {
							list.add(null);
							continue;
						}
					}
					//如果没有null也没有空串
					list.add(temp);
				}
			}
		}
		return list;

	}
	
	//获取导入数据
	public static  List<String[]> getImportedData(MultipartFile file,String headers[]) throws Throwable{
		return getImportedData(file, headers, 0);
	}

	//是否与模板相符
	private static void accordWithTemplet(Sheet sheet, String headers[], int headerIndex) throws Throwable {
		Row headerRow = sheet.getRow(headerIndex);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.getCell(i);
			if (cell == null) {
				throw new ImportExcelException("导入的数据与模板不符");
			}
			cell.setCellType(Cell.CELL_TYPE_STRING);
			String header = getStringNoBlank(cell.getStringCellValue());
			if (!headers[i].equals(header)) {
				throw new ImportExcelException("导入的数据与模板不符");
			}
		}
	}

	//是否与模板相符
	private static void accordWithTemplet(Sheet sheet,String headers[]) throws Throwable {
		accordWithTemplet(sheet, headers, 0);
	}
	
	//替换无效字符
	public static String getStringNoBlank(String str) {
		if (str != null && !"".equals(str)) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			String strNoBlank = m.replaceAll("");
			return strNoBlank;
		} else {
			return str;
		}
	}
}
