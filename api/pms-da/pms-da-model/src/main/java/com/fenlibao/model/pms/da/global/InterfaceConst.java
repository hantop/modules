package com.fenlibao.model.pms.da.global;

import java.math.BigDecimal;

/**
 * 版本接口配置常量
 *
 * @author chenzhixuan
 */
public class InterfaceConst {
    public static final String USER_LOGIN = "login";

    /**
     * 用户头像（附件类型）
     */
    public static final int USER_IMAGE_TYPE = 6;

    /**
     * 发送短信验证码类型--绑定手机号
     */
    public static final int BIND_PHONE_TYPE = 201;
    /**
     * 发送短信验证码类型--普通注册
     */
    public static final int REGISTER_TYPE = 208;

    /**
     * 发送短信验证码类型--添加银行卡
     */
    public static final int BIND_BankCard_TYPE = 209;
    /**
     * 发送短信验证码类型:重置交易密码
     */
    public static final int RESET_TRADE_PASSWORD = 202;
    /**
     * 找回密码
     */
    public static final int RETRIEVE_PASSWORD_TYPE = 207;

    /**
     * token延长毫秒数(48小时)
     */
    public static final int TOKEN_PROLONG_HOUR = 48 * 60 * 60 * 1000;

    /**
     * 安全认证-交易密码-已设置
     */
    public static final String AUTH_TRADPASSWORD_SETED = "YSZ";
    /**
     * 安全认证-交易密码-未设置
     */
    public static final String AUTH_TRADPASSWORD_NOTSET = "WSZ";
    /**
     * 资金账户类型-往来账户
     */
    public static final String ACCOUNT_TYPE_WLZH = "WLZH";
    /**
     * 资金账户类型-锁定账户
     */
    public static final String ACCOUNT_TYPE_SDZH = "SDZH";
    /**
     * 新网存管-投资人-往来账户
     */
    public static final String XW_INVESTOR_WLZH = "XW_INVESTOR_WLZH";
    /**
     * 新网存管-投资人-锁定账户
     */
    public static final String XW_INVESTOR_SDZH = "XW_INVESTOR_SDZH";
    /**
     * 新网存管-借款人-往来账户
     */
    public static final String XW_BORROWER_WLZH = "XW_BORROWER_WLZH";
    /**
     * 新网存管-借款人-锁定账户
     */
    public static final String XW_BORROWER_SDZH = "XW_BORROWER_SDZH";
    /**
     * 用户类型-自然人
     */
    public static final String USER_TYPE_ZRR = "ZRR";

    /**
     * 用户类型-非自然人
     */
    public static final String USER_TYPE_FZRR = "FZRR";

    // 还款类
    /**
     * 本金
     */
    public static final int TZ_BJ = 7001;
    /**
     * 利息
     */
    public static final int TZ_LX = 7002;
    /**
     * 提前还款违约金
     */
    public static final int TZ_WYJ = 7005;
    /**
     * 逾期罚息
     */
    public static final int TZ_FX = 7004;

    /**
     * 还款方式---一次性付清
     */
    public static final String YCFQ = "YCFQ";

    /**
     * 标债权持有多少天后可转让
     */
    public static final int ZQZR_CY_DAY = 3;

    /**
     * 债权持有的最后多天前可转让
     */
    public static final int ZQZR_CY_LAST_DAY = 3;

    /**
     * 债权可转让次数
     */
    public static final int ZQZR_CS = 3;

    /**
     * 产品类型--开店宝
     */
    public static final String PRO_TYPE_KDB = "KDB";

    /**
     * 产品类型--薪金宝
     */
    public static final String PRO_TYPE_XJB = "XJB";

    /**
     * 薪金宝起投金额
     */
    public static final int XJB_MIX_AMOUNT = 500;

    /**
     * 开店宝起投金额
     */
    public static final int KDB_MIX_AMOUNT = 100;

    /**
     * 标的起投金额
     */
    public static final int BID_MIX_AMOUNT = 100;

    /**
     * 薪金宝产品期限
     */
    public static final int XJB_TIME_LIMIT = 12;

    /**
     * 投资金额的整数倍
     */
    public static final BigDecimal INTEGER_ROUNDING_LIMIT = new BigDecimal(100);

    /**
     * 订单来源
     */

    public static final String ORDER_SOURCE_XT = "XT"; //系统

    public static final String ORDER_SOURCE_YH = "YH"; //用户

    public static final String ORDER_SOURCE_HT = "HT"; //后台

    /**
     * 开店宝计划类型--12个月
     */
    public static final int KDB_MONTH12_TYPE = 2;

    /**
     * 开店宝计划类型--6个月
     */
    public static final int KDB_MONTH6_TYPE = 1;

    /**
     * 开店宝计划类型--3个月
     */
    public static final int KDB_MONTH3_TYPE = 0;


    /**
     * 平台账户id
     */
    public static final int PT_ACCOUNT_ID = 1; //后台

    /**
     * 开户名类型；1：个人，2：公司
     */
    public static final int ACCOUNT_NAME_TYPE_PERSON = 1;
    /**
     * 开户名类型；1：个人，2：公司
     */
    public static final int ACCOUNT_NAME_TYPE_COMPANY = 2;

    /**
     * 银行卡认证状，未认证：WRZ(未绑定)
     */
    public static final String BANK_CARD_STATUS_WRZ = "WRZ";
    /**
     * 银行卡已认证，已认证:YRZ（提现需要输入支行信息）
     */
    public static final String BANK_CARD_STATUS_YRZ = "YRZ";
    /**
     * 银行卡状态：KTX（提现不需要输入支行信息）
     */
    public static final String BANK_CARD_STATUS_KTX = "KTX";

    /**
     * 是否绑定银行卡，0=否，1=是
     */
    public static final int IS_BIND_BANK_CARD_YES = 1;

    /**
     * 用于校验系统常的值
     * <p>（比如后台配置需要验证手机号，则把常量设置成 true，其他为值则为不需要验证）
     */
    public static final String IS_TRUE = "true";

    /**
     * 状态：启用
     */
    public static final String STATUS_QY = "QY";
    /**
     * 状态：停用
     */
    public static final String STATUS_TY = "TY";

    /**
     * 放款状态：待放款
     */
    public static final String LOAN_STATUS_DFK = "DFK";

    /**
     * 后台自动审核、放款时用的账号ID，用于区分人工审核 :0
     */
    public static final int BACK_AUTO_OPERATION_ACCOUNT_ID = 0;

    /**
     * 订单状态：MJL（在连连没有记录）
     */
    public static final String ORDER_STATUS_MJL = "MJL";
    /**
     * 订单状态：SB（失败）
     */
    public static final String ORDER_STATUS_SB = "SB";
    /**
     * 订单状态：MJL（在连连没有记录）
     */
    public static final String ORDER_STATUS_WAITING = "WAITING";
    /**
     * 订单状态：SB（失败）
     */
    public static final String ORDER_STATUS_REFUND = "REFUND";

    /**
     * 以下银行体现时不需要传支行信息
     * <p>中国01040000，工商01020000，农业01030000，招商03080000，中国光大03030000，浦发03100000
     */
    public static final String[] BANK_CODES = {"03020000", "01040000", "01020000", "01030000", "03080000", "03030000", "03100000", "01050000", "3020000", "03040000", "03050000", "03060000", "03010000", "03090000", "03070000", "04031000", "64135810", "04233310"};

    /**
     * 红包类型：投标分享红包
     */
    public static final int REDPACKET_TENDER_SHARE = 0;
    /**
     * 红包类型：注册返现红包
     */
    public static final int REDPACKET_REGISTERRETURNCACH = 1;
    /**
     * 红包类型：注册现金红包
     */
    public static final int REDPACKET_REGISTERCACH = 2;

    /**
     * 体验金类型：注册体验金
     */
    public static final int REGISTER_EXPERIENCEGOLD = 1;
    /**
     * 体验金类型：投资体验金
     */
    public static final int INVESTMENT_EXPERIENCEGOLD = 2;

    /**
     * 红包状态：未使用
     */
    public static final int REDPACKET_STATUS_NOUSED = 1;
    /**
     * 红包状态：已使用
     */
    public static final int REDPACKET_STATUS_USED = 2;

    /**
     * 体验金状态：未使用
     */
    public static final int EXPERIENCEGOLD_NO_TYPE = 1;
    public static final int EXPERIENCEGOLD_YES_TYPE = 2;

    /**
     * 体验金使用状态 WJX未计息 JXZ计息中 YWC已完成
     */
    public static final String EXPERIENCEGOLD_WJX_STATUS = "WJX";
    public static final String EXPERIENCEGOLD_JXZ_STATUS = "JXZ";
    public static final String EXPERIENCEGOLD_YWC_STATUS = "YWC";

    /**
     * 1收入 和 2支出
     */
    public static final String EXPERIENCEGOLD_INCOME_STATUS = "1";
    public static final String EXPERIENCEGOLD_OVERAGE_STATUS = "2";

    /**
     * 红包业务类型 注册
     */
    public static final int REDPACKETBIZTYPE_REGISTER = 1;

    /**
     * 站内信标题
     */
    public static final String PRIVATEMESSAGE_TITLE = "系统消息";
    /**
     * 站内信标题
     */
    public static final String PRIVATEMESSAGE_TITL_TZTZ = "投资通知";

    /**
     * @fieldName: CREDIT_PREFIX_NAME
     * @fieldType: String
     * @Description: 转入债权名称前缀
     */
    public static final String CREDIT_NAME_PREFIX = "债权转让";

    /**
     * 计划退出标题
     */
    public static final String PLAN_QUIT_PREFIX = "计划退出";
    /**
     * 债权转让费率
     */
    public static final double ASSIGNMENT_RATE = 0.01;

    /**
     * 随时退出标的-债权转让费率
     */
    public static final double ASSIGNMENT_RATE_ANNYTIME_QUIT = 0;

    /**
     * 红包业务类型 交易类型 2使用红包
     */
    public static final String FXHB_TRADE_STATUS = "2";

    /**
     * 红包投资类型 1:标的 2:消费信贷计划 3:(新)计划
     */
    public static final String FXHB_INVEST_TYPE_BID = "1";

    /**
     * 红包投资类型 1:标的 2:消费信贷计划 3:(新)计划
     */
    public static final String FXHB_INVEST_TYPE_CUSPLAN = "2";

    /**
     * 红包投资类型 1:标的 2:消费信贷计划 3:(新)计划
     */
    public static final String FXHB_INVEST_TYPE_INVESTPLAN = "3";


    /**
     * 投标： 是否自己可投自己的标,true:可以,false,不可以
     */
    public static final String BID_SFZJKT = "false";

    /**
     * 借款本金
     */
    public static final int JK = 6001;

    /**
     * 投标
     */
    public static final int TZ = 3001;

    /**
     * 成交服务费
     */
    public static final int CJFWF = 1201;

    /**
     * 手动投标：最低起投金额
     */
    public static final String MIN_BIDING_AMOUNT = "100.00";

    /**
     * 还款计算:年表示的天数
     */
    public static final String REPAY_DAYS_OF_YEAR = "360";

    public static final int DECIMAL_SCALE = 9;

    /**
     * 判断是否启用实名认证
     */
    public final static String NCIICVARIABLE = "true";

    /**
     * 实名认证允许次数
     */
    public final static int ALLWO_LOGIN_ERROR_TIMES = 12;

    /**
     * LUHN验证不通过的银行卡号
     */
    public static final String ALLOW_BANKCARDS = "966666393740195612,4019335762667420";

    /**
     * 连连支付API 接口通用前缀
     */
    public final static String SERVER = "https://traderapi.lianlianpay.com/";

    /**
     * 返回状态码
     */
    public final static String RETCODE = "0000";
    /**
     * 每页条数（分页 ）
     */
    public static final int PAGING_NUMBER = 10;

    /**
     * web自动登录时间（单位：秒）
     */
    public static final long accessTime = 60;

    /**
     * 逻辑标识，是:1
     */
    public final static Integer LOGIC_YES = 1;
    /**
     * 逻辑标识，否:0
     */
    public final static Integer LOGIC_NO = 0;

    /**
     * 实名认证返回码
     */
    public final static String SMRZ_CORRECT_RETCODE = "000000";

    public final static String SMRZ_ERROR_RETCODE = "9999";

    public final static String SMRZ_ZERO_SCORE = "0";
    public final static String SMRZ_ONE_SCORE = "1";
    public final static String SMRZ_TWO_SCORE = "2";
    public final static String SMRZ_THREE_SCORE = "3";

    public final static String SMRZ_ORDER_FEE = "1.5";

    /**
     * 新手标上线金额为10000元
     */
    public static final int XSB_MAX_AMOUNT = 10000;

    /**
     * 债权转让金额比例范围(80%~100%)
     */
    public static final double MAX_CREDIT_ASSIGNMENT_RATE = 1;

    public static final double MIN_CREDIT_ASSIGNMENT_RATE = 0.8;

    /**
     * 默认广告类型
     */
    public static final int DEFAULT_ADVERT_TYPE = 0;

    /**
     * 标扩展信息标动态显示分组
     **/
    public static final String BID_EXTEND_GROUP_TYPE_CODE = "group_info";

    /**
     * 标扩展信息服务协议分组
     **/
    public static final String BID_EXTEND_ASSIGNMENT_CODE = "assignment";

    /**
     * 排序方式
     */
    public static final String ORDER_TYPE_DESC = "DESC";

    public static final String ORDER_TYPE_ASC = "ASC";

    public static final String CELL_TAIL_NUMBER_PREFIX = "***";

    /**
     * 返现券投资期限提示
     */
    public static final String INVESTMENT_DEADLINE_TIPS_ALL = "不限期限";
    /**
     * 返现券投资期限提示
     */
    public static final String INVESTMENT_DEADLINE_TIPS = "限期限≥%s天使用";

    /**
     * 加息券投资期限提示
     */
    public static final String COUPON_JXJ_DEADLINE_TIPS = "限期限≤%s天使用";

    /**
     * 加息券投资期限提示
     */
    public static final String COUPON_JXJ_DEADLINE_TIPS_AND = "限期限%s~%s天使用";


    /**
     * 返现券投资标类型提示
     */
    public static final String INVESTMENT_BIDTYPE_TIPS = "限%s使用";
    /**
     * 返现券投资标类型提示
     */
    public static final String INVESTMENT_BIDTYPE_TIPS_ALL = "新手标、债权转让不可用";

    /**
     * 默认分页大小
     */
    public static final int PAGESIZE = 20;

    /**
     * 积分抽奖活动code
     */
    public static final String POINTS_LOTTERY_TYPE = "POINTS_LOTTERY";

    /**
     * 积分收入
     */
    public static final int PCT_INCOME = 1;

    /**
     * 积分支出
     */
    public static final int PCT_OUTGOINGS = 2;

    /**
     * 积分抽奖分数
     */
    public static final int POINTS_LOTTERY_CONSUME_NUM = 10;

    /**
     * BigDecimal 100
     */
    public static final BigDecimal BIGDECIMAL_HUNDRED = new BigDecimal(100);

    public static final String ACTIVITY_CODE_AUGUST_OLYMPIC = "AUGUST_OLYMPIC";

    /**
     * 交易密码最大错误次数
     */
    public static final int TRADE_PWD_WRONG_COUNT_MAX = 5;

    /**
     * 加息卷过期提示
     */
    public static final int OVERDUE = 7;

    /**
     * 抢购标误差秒数
     */
    public static final Long EEROR_SECOND = 8L;

    /**
     * 投资分享活动编码
     */
    public static final String TENDER_SHARE_CODE = "tzfxhd1";
    /**
     * 计划到期过期天数算逾期
     */
    public static final int PLAN_EXPIRE_DAYS = 5;
    /**
     * 用户申请退出 过期天数算逾期
     */
    public static final int PLAN_USER_QUIT_DAYS = 5;

    /**
     * 计划债权持有多少天后可转让
     */
    public static final int PLAN_ZQZR_CY_DAY = 40;

}
