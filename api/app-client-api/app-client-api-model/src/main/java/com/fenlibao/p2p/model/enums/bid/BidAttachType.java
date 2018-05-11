package com.fenlibao.p2p.model.enums.bid;

/**
 *	标附件类型
 * @author: junda.feng
 */

public enum BidAttachType {

	cooperation(26, "三方合作协议 "),
	distribution(25, "经销对账单 "),
	order(24, "名创优品自营进货定单"),
	goodsreceipt(23, "名创优品收货确认凭证"),
	shareholders(22, "股东会决议"),
	supplycommodity(21, "与名创优品的供货合同"),
	
	housereport(20, "房产评估报告"),
	fairbook(19, "公证书"),
	houseproperty(18, "房产证"),
	survey(17, "亲属调查"),
	vehicleassessment(16, "车辆评估报告"),
	
	vehiclerelated(15, "车辆相关证照"),
	employment(14, "工作证明"),
	residencepermit(13, "居住证"),
	achievement(12, "业绩流水报表"),
	bankinfo(11, "银行流水"),
	
	licence(10, "公司相关证照"),
	credit(9, "个人征信"),
	personalasset(8, "个人资产凭证"),
	guarantor(7, "担保方相关资料"),
	cashdeposit(6, "品牌公司收取加盟保证金凭证"),
	
	brandauthorization(5, "品牌特许经营授权书"),
	brandagreements(4, "品牌特许经营合同"),
	agreements(3, "租赁合同"),
	business(2, "营业执照"),
	identification(1, "身份证"),
    ;

    private Integer code;
    private String desc;

    BidAttachType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BidAttachType getByCode(Integer code) {
    	BidAttachType[] types = BidAttachType.values();
        for (BidAttachType t : types) {
            if (t.getCode().equals(code)) {
                return t;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
