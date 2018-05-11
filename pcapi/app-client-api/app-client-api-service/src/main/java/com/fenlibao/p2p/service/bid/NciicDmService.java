package com.fenlibao.p2p.service.bid;

/**
 * 实名认证查询接口.
 *
 */
public interface NciicDmService {
    /**
     * 实名认证.
     *
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    boolean check(String id, String name) throws Exception;

    /**
     * 实名认证.
     *
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @param status
     *            练练支付验证的结果状态
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    boolean check(String id, String name, boolean status)
            throws Exception;

    /**
     * 校验身份证号是否合法.
     *
     * @param id
     *            身份证号码
     * @return {@code boolean} 是否合法
     * @throws Throwable
     */
    //boolean isValidId(String id) throws Throwable;

    /**
     * 判断身份证是否存在
     * @param idCard
     * @return
     * @throws Throwable
     */
    public abstract boolean isIdcard(String idCard) throws Exception;
}
