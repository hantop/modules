package com.fenlibao.p2p.util.api.pdf;

import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class PDFUtil {
	public static void main(String[] args) throws Exception {
		//String positeStr = "[{\"signX\":0.5885,\"signY\":0.0875,\"page\":1,\"name\":\"合同编号：\"},{\"signX\":0.1512,\"signY\":0.2171,\"page\":1,\"name\":\"乙方（借款人）：\"},{\"signX\":0.1512,\"signY\":0.2449,\"page\":1,\"name\":\"住所地：\"},{\"signX\":0.1915,\"signY\":0.4391,\"page\":1,\"name\":\"住所地：\"},{\"signX\":0.1512,\"signY\":0.5776,\"page\":1,\"name\":\"住所地：\"},{\"signX\":0.1512,\"signY\":0.744,\"page\":1,\"name\":\"住所地：\"},{\"signX\":0.1512,\"signY\":0.2726,\"page\":1,\"name\":\"法定代表人/负责人：\"},{\"signX\":0.1512,\"signY\":0.4667,\"page\":1,\"name\":\"法定代表人/负责人：\"},{\"signX\":0.1512,\"signY\":0.6054,\"page\":1,\"name\":\"法定代表人/负责人：\"},{\"signX\":0.1512,\"signY\":0.7718,\"page\":1,\"name\":\"法定代表人/负责人：\"},{\"signX\":0.1512,\"signY\":0.3004,\"page\":1,\"name\":\"身份证号/注册号：\"},{\"signX\":0.1512,\"signY\":0.6331,\"page\":1,\"name\":\"身份证号/注册号：\"},{\"signX\":0.1512,\"signY\":0.7996,\"page\":1,\"name\":\"身份证号/注册号：\"},{\"signX\":0.1512,\"signY\":0.328,\"page\":1,\"name\":\"联系电话：\"},{\"signX\":0.1512,\"signY\":0.4945,\"page\":1,\"name\":\"联系电话：\"},{\"signX\":0.1512,\"signY\":0.6609,\"page\":1,\"name\":\"联系电话：\"},{\"signX\":0.1512,\"signY\":0.8272,\"page\":1,\"name\":\"联系电话：\"},{\"signX\":0.1512,\"signY\":0.3558,\"page\":1,\"name\":\"分利宝平台用户名:\"},{\"signX\":0.1512,\"signY\":0.55,\"page\":1,\"name\":\"丁方（连带责任担保）：\"},{\"signX\":0.1512,\"signY\":0.7163,\"page\":1,\"name\":\"戊方（一般保证）：\"},{\"signX\":0.3397,\"signY\":0.1661,\"page\":4,\"name\":\"金额（小写）\"},{\"signX\":0.5817,\"signY\":0.1661,\"page\":4,\"name\":\"(大写)：\"},{\"signX\":0.3397,\"signY\":0.2261,\"page\":4,\"name\":\"年利率      %\"},{\"signX\":0.3397,\"signY\":0.2526,\"page\":4,\"name\":\"共    个月\"},{\"signX\":0.1903,\"signY\":0.5242,\"page\":4,\"name\":\"乙方虚拟账户：\"},{\"signX\":0.6704,\"signY\":0.7293,\"page\":4,\"name\":\"每月的       日\"},{\"signX\":0.3629,\"signY\":0.7822,\"page\":4,\"name\":\"每月还本息之和        元\"},{\"signX\":0.4032,\"signY\":0.8643,\"page\":4,\"name\":\"每月还利息       元\"},{\"signX\":0.5242,\"signY\":0.0917,\"page\":5,\"name\":\"一次性偿还本息共计       元。\"},{\"signX\":0.1512,\"signY\":0.6673,\"page\":7,\"name\":\"邮寄地址：\"},{\"signX\":0.2016,\"signY\":0.2448,\"page\":10,\"name\":\"乙方（签章）：\"},{\"signX\":0.2016,\"signY\":0.3557,\"page\":10,\"name\":\"丙方（签章）：广东分利宝金服科技有限公司\"},{\"signX\":0.2016,\"signY\":0.4666,\"page\":10,\"name\":\"丁方（签章）：\"},{\"signX\":0.2016,\"signY\":0.5775,\"page\":10,\"name\":\"戊方（签章）：\"},{\"signX\":0.2016,\"signY\":0.6607,\"page\":10,\"name\":\"签署日期：    年   月   日 \"}]\n";
		//JSON posJson = JSON.parseObject(positeStr);
		//[{"value":{"signY":758.3031,"page":1,"signX":421.67355},"key":"param0"},{"value":{"signY":649.1475,"page":1,"signX":186.5106},"key":"param1"},{"value":{"signY":625.733,"page":1,"signX":138.0369},"key":"param2"},{"value":{"signY":602.4869,"page":1,"signX":204.07785},"key":"param3"},{"value":{"signY":579.0723,"page":1,"signX":192.04875},"key":"param4"},{"value":{"signY":555.742,"page":1,"signX":150.06601},"key":"param5"},{"value":{"signY":532.32745,"page":1,"signX":192.04875},"key":"param6"},{"value":{"signY":368.84674,"page":1,"signX":222.65744},"key":"param7"},{"value":{"signY":345.5164,"page":1,"signX":138.0369},"key":"param8"},{"value":{"signY":322.18607,"page":1,"signX":204.07785},"key":"param9"},{"value":{"signY":298.85574,"page":1,"signX":192.04875},"key":"param10"},{"value":{"signY":275.44122,"page":1,"signX":150.06601},"key":"param11"},{"value":{"signY":228.78055,"page":1,"signX":198.65881},"key":"param12"},{"value":{"signY":205.45023,"page":1,"signX":138.0369},"key":"param13"},{"value":{"signY":182.03566,"page":1,"signX":204.07785},"key":"param14"},{"value":{"signY":158.62114,"page":1,"signX":192.04875},"key":"param15"},{"value":{"signY":135.37505,"page":1,"signX":150.06601},"key":"param16"},{"value":{"signY":692.1023,"page":4,"signX":274.2873},"key":"param17"},{"value":{"signY":692.1023,"page":4,"signX":394.3401},"key":"param18"},{"value":{"signY":641.56726,"page":4,"signX":238.25955},"key":"param19"},{"value":{"signY":619.2476,"page":4,"signX":214.26091},"key":"param20"},{"value":{"signY":619.2476,"page":4,"signX":286.25684},"key":"param21"},{"value":{"signY":619.2476,"page":4,"signX":334.3137},"key":"param22"},{"value":{"signY":619.2476,"page":4,"signX":358.31235},"key":"param23"},{"value":{"signY":619.2476,"page":4,"signX":394.3401},"key":"param24"},{"value":{"signY":619.2476,"page":4,"signX":430.3083},"key":"param25"},{"value":{"signY":619.2476,"page":4,"signX":460.3215},"key":"param26"},{"value":{"signY":390.57678,"page":4,"signX":197.34871},"key":"param27"},{"value":{"signY":217.8313,"page":4,"signX":447.16092},"key":"param28"},{"value":{"signY":173.27626,"page":4,"signX":324.0711},"key":"param29"},{"value":{"signY":104.127556,"page":4,"signX":318.11612},"key":"param30"},{"value":{"signY":754.7657,"page":5,"signX":438.10934},"key":"param31"},{"value":{"signY":270.05078,"page":7,"signX":150.06601},"key":"param32"},{"value":{"signY":246.63626,"page":7,"signX":150.06601},"key":"param33"},{"value":{"signY":223.22168,"page":7,"signX":174.06464},"key":"param34"},{"value":{"signY":625.9014,"page":10,"signX":198.0633},"key":"param35"},{"value":{"signY":532.4959,"page":10,"signX":198.0633},"key":"param36"},{"value":{"signY":439.09036,"page":10,"signX":198.0633},"key":"param37"},{"value":{"signY":345.6848,"page":10,"signX":198.0633},"key":"param38"},{"value":{"signY":275.60965,"page":10,"signX":177.63765},"key":"param39"},{"value":{"signY":275.60965,"page":10,"signX":225.6945},"key":"param40"},{"value":{"signY":275.60965,"page":10,"signX":261.6627},"key":"param41"},{"value":{"signY":671.6356,"page":1,"signX":90.0396},"key":"param42"},{"value":{"signY":671.6356,"page":1,"signX":211.6407},"key":"param43"},{"value":{"signY":671.6356,"page":1,"signX":298.64325},"key":"param44"},{"value":{"signY":671.6356,"page":1,"signX":429.2364},"key":"param45"},{"value":{"signY":647.7157,"page":1,"signX":90.0396},"key":"param46"},{"value":{"signY":689.99664,"page":1,"signX":90.0396},"key":"param47"},{"value":{"signY":689.99664,"page":1,"signX":211.6407},"key":"param48"},{"value":{"signY":689.99664,"page":1,"signX":298.64325},"key":"param49"},{"value":{"signY":689.99664,"page":1,"signX":429.2364},"key":"param50"}]

//		File file = new File("/home/sleepy/download/document/担保借款合同.pdf");
//		byte[] byteArray = FileUtils.readFileToByteArray(file);
//
//		Map<Integer, List<EditPDFDetails>> map = new HashMap<Integer, List<EditPDFDetails>>();
//
//		List<EditPDFDetails> list = new ArrayList<EditPDFDetails>();
//		for (int i = 0; i < 2; i++) {
//			EditPDFDetails d = new EditPDFDetails();
//			d.setContents("00001111");
//			d.setX(421.67355f);
//			d.setY(758.3031f);
//			d.setColor(BaseColor.BLACK);
//			d.setFontSize(9);
//			list.add(d);
//		}
//
//		map.put(1, list);
//
//
//		byte[] bytes = PDFUtil.editPDF(byteArray, map);
//		File fss = new File("/home/sleepy/hub/workplace/ideaB/ssq/src/main/resources/担保借款合同_test.pdf");
//		FileUtils.writeByteArrayToFile(fss, bytes);
	}

	/**
	 * 获取文件字节码
	 * @param path
	 * @return
	 */
	public static byte[] getPdfBytes(String path) throws Exception {
		if (StringUtils.isEmpty(path)) {
			InputStream s = PDFUtil.class.getClassLoader().getResourceAsStream(path);
			ArrayList<byte[]> bufferList = new ArrayList<byte[]> ();
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

		} else {
			File file = new File(path);
			byte[] byteArray = FileUtils.readFileToByteArray(file);
			return byteArray;
		}
	}

	/**
	 *
	 * @param metadata
	 *            byte文件
	 * @param map
	 *            Map<Integer, List<EditPDFDetails>> 编辑的页码和添加的内容
	 * @throws Exception
	 */
	public static byte[] editPDF(byte[] metadata, Map<Integer, List<EditPDFDetails>> map) throws Exception {
		// 创建一个pdf读入流
		PdfReader reader = new PdfReader(metadata);
		// 根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(reader, o);
		byte[] result;
		try {
			// 这个字体是itext-asian.jar中自带的 所以不用考虑操作系统环境问题.
			BaseFont bf = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
					BaseFont.EMBEDDED); // set
			// font
			// baseFont不支持字体样式设定.但是font字体要求操作系统支持此字体会带来移植问题.
			Font font = new Font(bf, 10);
			font.setStyle(Font.BOLD);
			font.getBaseFont();
			// 页数是从1开始的
			for (int page = 1; page <= reader.getNumberOfPages(); page++) {

				// 获得pdfstamper在当前页的上层打印内容.也就是说 这些内容会覆盖在原先的pdf内容之上.
				PdfContentByte over = stamper.getOverContent(page);
				List<EditPDFDetails> list = map.get(page);
				if (list == null) {
					continue;
				}
				if (!list.isEmpty()) {
					for (EditPDFDetails details : list) {
						// 开始写入文本
						over.beginText();
						// 设置字体和大小
						over.setFontAndSize(font.getBaseFont(), details.getFontSize());
						// 设置字体颜色
						over.setColorFill(details.getColor());
						// 设置字体的输出位置
						over.setTextMatrix((float) details.getX(), (float)details.getY());
						// 要输出的text
						over.showText(details.getContents());
						over.endText();
					}
				}
			}
		} finally {
			//stamper关闭之后才会把输出流刷新
			stamper.close();
			result = o.toByteArray();
			reader.close();
			o.close();
		}
		return result;

	}

	/**
	 * 添加到最后一页
	 * @param src
	 * @param dest
	 * @param COVER
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void addEndPage(String src, String dest,
						   String COVER)
			throws IOException, DocumentException {
		PdfReader cover = new PdfReader(COVER);
		PdfReader reader = new PdfReader(src);
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, new FileOutputStream(dest));
		document.open();
		copy.addDocument(reader);
		copy.addDocument(cover);
		document.close();
		cover.close();
		reader.close();
	}

	/**
	 * 合并pdf文件
	 * @param destName
	 * @param source
	 * @param endPage
	 * @param tailName
	 */
	public static void mergePdfFiles(File destName, byte[] source, Integer endPage, byte[] signPage, byte[] tailName) throws Exception {
		PdfReader tail = new PdfReader(tailName);
		PdfReader reader = new PdfReader(source);
		PdfReader signPageReader = new PdfReader(signPage);
		Document document = new Document();
		try {
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(destName));
			document.open();
			reader.selectPages("1-" + endPage);
			copy.addDocument(reader);
			copy.addDocument(signPageReader);
			copy.addDocument(tail);
		} finally {
			document.close();
			tail.close();
			reader.close();
		}
	}

	/**
	 * 剪辑出某一页的内容
	 * @param dest
	 * @param source
	 * @param page
	 * @throws Exception
	 */
	public static void cutPage(File dest, byte[] source, int page) throws Exception {
		PdfReader reader = new PdfReader(source);
		Document document = new Document();
		try {
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(dest));
			document.open();
			reader.selectPages(String.valueOf(page));
			copy.addDocument(reader);
		} finally {
			document.close();
			reader.close();
		}
	}

	/**
	 * add by zeronx bufferImage 太耗内存，不能使用
	 * 将pdf文件转为一张图片
	 * @param pdfBytes pdf文件字节数组
	 * @return 转换成功返回图片字节数组 失败返回null
	 */
	public static byte[] convertPdfToImage(byte[] pdfBytes) {

		try (InputStream inputStream = new ByteArrayInputStream(pdfBytes);
			 PDDocument doc = PDDocument.load(inputStream)) {

			List pages = doc.getDocumentCatalog().getAllPages();
			List<BufferedImage> list = new ArrayList<>();
			for (int i = 0; i < pages.size(); i++) {
				PDPage page = (PDPage) pages.get(i);
				BufferedImage image = page.convertToImage();
				int WIDTH = image.getWidth();
				int HEIGHT = image.getHeight();
				Graphics g = image.getGraphics();
				// 设置边框颜色
				g.setColor(Color.GRAY);
				// 设置边框这样合并图片时有线条效果
				g.drawRect(1, 0, WIDTH - 2, HEIGHT - 2);
				list.add(image);
//				将pdf 按每页图片保存
//				Iterator iter = ImageIO.getImageWritersBySuffix("png");
//				ImageWriter writer = (ImageWriter) iter.next();
//				File outFile = new File("E:\\ssq\\img\\"+ i + ".png");
//				FileOutputStream out = new FileOutputStream(outFile);
//				ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
//				writer.setOutput(outImage);
//				writer.write(new IIOImage(image, null, null));
			}
			return mergeImage(list, "png"); // 将图片合并成一张
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * add by zeronx bufferImage 太耗内存，不能使用
	 * 将 n 张图片合成一张图片
	 * @param picList 需要组合的图片列表
	 * @param suffix 合成图片的后缀 png、jpg 等图片格式
	 * @return 成功返回图片字节数组 失败返回null
	 */
	public static byte[] mergeImage(List<BufferedImage> picList, String suffix) {// 纵向处理图片
		if (picList == null || picList.size() <= 0) {
			return null;
		}
		ByteArrayOutputStream out = null;
		try {
			int height = 0, // 总高度
					width = 0, // 总宽度
					_height = 0, // 临时的高度 , 或保存偏移高度
					__height = 0, // 临时的高度，主要保存每个高度
					picNum = picList.size();// 图片的数量
			int[] heightArray = new int[picNum]; // 保存每个文件的高度
			BufferedImage buffer = null; // 保存图片流
			List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
			int[] _imgRGB; // 保存一张图片中的RGB数据
			int originWidth = 0; // 保存第一张图片的宽度，以后的每一张都按这个宽度
			for (int i = 0; i < picNum; i++) {
				buffer = picList.get(i);
				heightArray[i] = _height = buffer.getHeight();// 图片高度
				if (i == 0) {
					originWidth = buffer.getWidth();// 图片宽度
				}
				width = buffer.getWidth();
				if (width < originWidth) {
					// 处理宽度不够的图片
					buffer = dealWithImageWidth(buffer, originWidth);
					width = originWidth;
				}
				height += _height; // 获取总高度
				_imgRGB = new int[width * _height];// 从图片中读取RGB
				_imgRGB = buffer.getRGB(0, 0, width, _height, _imgRGB, 0, width);
				imgRGB.add(_imgRGB);
			}
			_height = 0; // 设置偏移高度为0
			// 生成新图片
			BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < picNum; i++) {
				__height = heightArray[i];
				if (i != 0) _height += __height; // 计算偏移高度
				imageResult.setRGB(0, _height, width, __height, imgRGB.get(i), 0, width); // 写入流中
			}
//			File outFile = new File("E:\\temp.png");
//			ImageIO.write(imageResult, "png", outFile);// 写图片
			out = new ByteArrayOutputStream();
			boolean flag = ImageIO.write(imageResult, suffix, out);
			if (flag) {
				byte[] b = out.toByteArray();
				return b;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private static BufferedImage dealWithImageWidth(BufferedImage buffer, int originWidth) {
		try {
			BufferedImage modify = new BufferedImage(originWidth, buffer.getHeight(), BufferedImage.TYPE_INT_BGR);
			Graphics2D g = (Graphics2D) modify.getGraphics();
			g.drawImage(buffer, 0, 0, originWidth, buffer.getHeight(),null); //画图
			g.dispose();
			modify.flush();
			return modify;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param result 原文档
	 * @param tempPath 保存目录路径
	 * @return
	 * @throws Exception
	 */
	public static byte[] dealWithResultPdf(byte[] result, String tempPath, int positionPage) throws Exception {
		File newFile = new File(tempPath + "tempSign.pdf");
		PdfWriter pdfWr = null;
		OutputStream outputStream = null;
		com.itextpdf.text.Document document = null;
		int maxPage = 0;
		int currentPage = positionPage;
		try(ByteArrayInputStream inputStream = new ByteArrayInputStream(result)) {
			PDDocument doc = PDDocument.load(inputStream);
			List pages = doc.getDocumentCatalog().getAllPages();
			document = new com.itextpdf.text.Document(new com.itextpdf.text.Rectangle(PageSize.A4));
			outputStream = new FileOutputStream(newFile);
			pdfWr = PdfWriter.getInstance(document, outputStream);
			document.open();
			maxPage = pages.size();
			for (int i = 0; i < maxPage; i++) {
				if (i == currentPage) {
					PDPage page = (PDPage) pages.get(i);
					BufferedImage image = page.convertToImage();
					BufferedImage buffImg;
					buffImg = new BufferedImage(595, 842, BufferedImage.TYPE_INT_RGB);
					buffImg.getGraphics().drawImage(image.getScaledInstance(595, 842, Image.SCALE_SMOOTH), 0,0, null);
					ByteArrayOutputStream out1 = new ByteArrayOutputStream();
					ImageIO.write(buffImg, "png", out1);
					byte[] bytes1 = out1.toByteArray();
					out1.close();
					com.itextpdf.text.Image imagePdf1 = com.itextpdf.text.Image.getInstance(bytes1);
					imagePdf1.setAbsolutePosition(0.0f, 0.0f);
					document.add(imagePdf1);
				}
			}
			pdfWr.flush();
			doc.close();
		} finally {
			if (document != null) {
				document.close();
			}
			if (pdfWr != null) {
				pdfWr.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
		return mergePdfFiles1(result, currentPage, maxPage, new FileInputStream(newFile), tempPath);
	}

	/**
	 * 返回插入后的文档byte[]
	 * @param result 原文件
	 * @param currentPage 插入位置页数
	 * @param maxPage 最后一页数
	 * @param fileInputStream 需要插入的文件
	 * @return 返回插入后的文档byte[]
	 */
	private static byte[] mergePdfFiles1(byte[] result, int currentPage, int maxPage, FileInputStream fileInputStream, String tempPath) throws Exception {
		PdfReader tail = new PdfReader(fileInputStream);
		PdfReader reader = new PdfReader(result);
		PdfReader reader1 = new PdfReader(result);
		Document document = new Document();
		OutputStream outputStream = null;
		InputStream inputStream = null;
		ByteArrayOutputStream out = null;
		byte[] bytes = null;
		File file = null;
		try {
			file = new File(tempPath + "newSignSensitive.pdf");
			outputStream = new FileOutputStream(file);
			PdfCopy copy = new PdfCopy(document, outputStream);
			document.open();
			reader.selectPages("1-" + currentPage);
			copy.addDocument(reader);
			copy.addDocument(tail);
			reader1.selectPages((currentPage + 2) + "-" + maxPage);
			copy.addDocument(reader1);
		} finally {
			document.close();
			tail.close();
			reader.close();
			if (outputStream != null) {
				outputStream.close();
			}
			fileInputStream.close();
		}
		try {
			inputStream = new FileInputStream(file);
			out = new ByteArrayOutputStream();
			int ch;
			while ((ch = inputStream.read()) != -1) {
				out.write(ch);
			}
			bytes = out.toByteArray();
		} finally {
			if (out != null) {
				out.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return bytes;
	}

	public static byte[] addInvestorsSignPage(byte[] bytes1, int needAddPages, int removePageNum) throws Exception {
		byte[] bytes = null;
		try(ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes1);
			PDDocument doc = PDDocument.load(inputStream);
			ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			int pdfCountPages = doc.getNumberOfPages();
			for (int j = removePageNum; j < pdfCountPages; j++) {
				doc.removePage(removePageNum);
			}
			for (int i = 0; i < needAddPages; i++) {
				PDPage pdPage = new PDPage(new PDRectangle(595, 842));
				doc.addPage(pdPage);
			}
			doc.save(out);
			bytes = out.toByteArray();
		}
		return bytes;
	}
}
