package com.fenlibao.p2p.model.api.enums;

/**
 * 
 * @author yangzengcai
 * @date 2016年2月18日
 */
public enum SystemBoolean {

	TRUE(1, "1", true, "S", "是"), FALSE(0, "0", false, "F", "否");
	
	private Integer code;
	private String codeStr;
	private boolean value;
	private String initial;
	private String desc;
	
	private SystemBoolean(Integer code, String codeStr, boolean value, String initial, String desc) {
		this.code = code;
		this.codeStr = codeStr;
		this.value = value;
		this.initial = initial;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public boolean isValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public String getCodeStr() {
		return codeStr;
	}

	public String getInitial() {
		return initial;
	}

	public static SystemBoolean get(String key) {
		for (SystemBoolean sb : SystemBoolean.values()) {
			if (sb.getCodeStr().equals(key)) {
				return sb;
			}
		}
		return null;
	}
	
}
