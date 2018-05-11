package com.fenlibao.p2p.util.api.pdf;

import com.itextpdf.text.BaseColor;

/**
 * 编辑pdf
 * 
 * @author Administrator
 * 
 */
public class EditPDFDetails {
	/** x坐标 */
	private float x;
	/** y坐标 */
	private float y;
	/** 内容 */
	private String contents;
	/** 字体大小 默认 10 */
	private int fontSize = 10;
	/** 字体颜色 默认红色 */
	private BaseColor color = BaseColor.BLACK;

	public EditPDFDetails() {
		super();
	}

	public EditPDFDetails(int x, int y, String contents, int fontSize, BaseColor color) {
		super();
		this.x = x;
		this.y = y;
		this.contents = contents;
		this.fontSize = fontSize;
		this.color = color;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public BaseColor getColor() {
		return color;
	}

	public void setColor(BaseColor color) {
		this.color = color;
	}

}
