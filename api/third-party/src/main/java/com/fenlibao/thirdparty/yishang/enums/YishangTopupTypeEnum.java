package com.fenlibao.thirdparty.yishang.enums;

/**
* 易赏手机充值面额枚举
* @author junda.feng
* @date:2016-4-25
* @version :1.0
*
*/
public enum YishangTopupTypeEnum
{

	Topup_1("1", "1元话费"),//测试
    Topup_10("10", "全国三网10元话费"),
    Topup_30("30", "全国三网30元话费"),
	Topup_50("50", "全国三网50元话费"),
	Topup_100("100", "全国三网100元话费");

    private final String code;
    private final String msg;

    YishangTopupTypeEnum(String code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public String getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }
    public static boolean isSignType(String code)
    {
        for (YishangTopupTypeEnum s : YishangTopupTypeEnum.values())
        {
            if (s.getCode().equals(code))
            {
                return true;
            }
        }
        return false;
    }
}
