package com.fenlibao.p2p.model.enums.bid;

/**
 * 还款方式
 */
public enum PaymentTypeEnum {
	ZRY("ZRY", "按月计息"),
	GDR("GDR", "按天计息")
    ;

    private String code;
    private String name;

    PaymentTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String code) {
		for (PaymentTypeEnum pt : PaymentTypeEnum.values()) {
			if (pt.getCode().equals(pt.getCode())) {
				return pt.getName();
			}
		}
		return null;
	}
}
