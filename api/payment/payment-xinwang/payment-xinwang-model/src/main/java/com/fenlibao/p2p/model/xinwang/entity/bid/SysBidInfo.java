package com.fenlibao.p2p.model.xinwang.entity.bid;

import com.fenlibao.p2p.model.xinwang.enums.bid.*;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @date 2017/6/1 9:56
 */
public class SysBidInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 借款用户ID,参考T6110.F01
     */
    public int F02;

    /**
     * 借款标题
     */
    public String F03;

    /**
     * 借款标类型ID,参考T6211.F01
     */
    public int F04;

    /**
     * 借款金额
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /**
     * 年化利率
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /**
     * 可投金额
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /**
     * 筹款期限,单位:天
     */
    public int F08;

    /**
     * 借款周期,单位:月
     */
    public int F09;

    /**
     * 还款方式,DEBX:等额本息;MYFX:每月付息,到期还本;YCFQ:本息到期一次付清;DEBJ:等额本金;
     */
    public RepaymentType F10;

    /**
     * 是否有担保,S:是;F:否;
     */
    public IS_DB F11;

    /**
     * 担保方案,BXQEDB:本息全额担保;BJQEDB:本金全额担保;
     */
    public GuaranteeType F12;

    /**
     * 是否有抵押,S:是;F:否;
     */
    public IS_Mortgage F13;

    /**
     * 是否实地认证,S:是;F:否;
     */
    public IS_Indeed F14;

    /**
     * 是否自动放款,S:是;F:否;
     */
    public IS_AutoLoan F15;

    /**
     * 是否允许流标,S:是;F:否;
     */
    public IS_LB F16;

    /**
     * 付息方式,ZRY:自然月;GDR:固定日;
     */
    public InterestType F17;

    /**
     * 付息日,自然月在满标后设置为满标日+起息日,固定日则必须小于等于28
     */
    public int F18;

    /**
     * 起息天数,T+N,默认为0
     */
    public int F19;

    /**
     * 状态,SQZ:申请中;DSH:待审核;DFB:待发布;YFB:预发布;TBZ:投标中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:
     * 已流标;YDF:已垫付;YZF:已作废;
     */
    public PTProjectState F20;

    /**
     * 封面图片编码
     */
    public String F21;

    /**
     * 发布时间,预发布状态有效
     */
    public Timestamp F22;

    /**
     * 信用等级,参考T5124.F01
     */
    public int F23;

    /**
     * 申请时间
     */
    public Timestamp F24;

    /**
     * 标编号
     */
    public String F25;

    /**
     * 计息金额
     */
    public BigDecimal F26 = BigDecimal.ZERO;

    /**
     * 是否线下债权转让,S:是;F:否;
     */
    public IS_Tranfer F27;

    /**
     * 标扩展信息
     */
    public String F28;

    /**
     * 是否新手标
     */
    public IS_Novice xsb;

    /**
     * 筹款到期时间
     */
    public Timestamp F31;

    /**
     * 借款期限：天
     */
    public int F32;

    /**
     * 资产类型
     */
    public String F33;

    /**
     * 是否正在还款中 0:还款中 1:正在还款
     */
    public int F34;

    /**
     * 上架时间,预发布后标出现在投资列表但未开投的时间
     */
    public Timestamp F35;

    /**
     * 标来源
     */
    public String F36;

    /**
     * 1：普通标，2：存管标
     */
    public int F38;

    /**
     * 1:可随时退出 0：不可以随时退出
     */
    public int F39;
}
