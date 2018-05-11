package com.fenlibao.p2p.model.user.entity;

import com.fenlibao.p2p.model.user.enums.T6118_F02;
import com.fenlibao.p2p.model.user.enums.T6118_F03;
import com.fenlibao.p2p.model.user.enums.T6118_F04;
import com.fenlibao.p2p.model.user.enums.T6118_F05;

/** 
 * 用户安全认证
 */
public class T6118 {

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 身份认证状态,TG:通过;BTG:不通过;
     */
    public T6118_F02 F02;

    /** 
     * 手机认证状态,TG:通过;BTG:不通过;
     */
    public T6118_F03 F03;

    /** 
     * 邮箱认证状态,TG:通过;BTG:不通过;
     */
    public T6118_F04 F04;

    /** 
     * 交易密码,YSZ:已设置;WSZ:未设置;
     */
    public T6118_F05 F05;

    /** 
     * 手机号码
     */
    public String F06;

    /** 
     * 邮箱地址
     */
    public String F07;

    /** 
     * 交易密码
     */
    public String F08;
    
}
