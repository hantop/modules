package com.fenlibao.p2p.model.trade.entity.order;

import com.fenlibao.p2p.model.trade.enums.order.T6501_F03;
import com.fenlibao.p2p.model.trade.enums.order.T6501_F07;

import java.sql.Timestamp;

/**
 * s61.t6501
 */
public class T6501 {

    /**
     * 主键
     */
    public int F01;
    /**
     * 类型编码
     */
    public int F02;
    /**
     * 状态,DTJ:待提交;YTJ:已提交;DQR:待确认;CG:成功;SB:失败;MJL:在连连没记录;WAITING等待支付(连连);REFUND退款(连连)
     */
    public T6501_F03 F03;
    /**
     * 创建时间
     */
    public Timestamp F04;
    /**
     * 提交时间
     */
    public Timestamp F05;
    /**
     * 完成时间
     */
    public Timestamp F06;
    /**
     * 订单来源,XT:系统;YH:用户;HT:后台;
     */
    public T6501_F07 F07;
    /**
     * 用户ID,参考T6110.F01
     */
    public int F08;
    /**
     * 后台帐号ID,参考T7110.F01
     */
    public int F09;
    /**
     * 流水号
     */
    public String F10;

}
