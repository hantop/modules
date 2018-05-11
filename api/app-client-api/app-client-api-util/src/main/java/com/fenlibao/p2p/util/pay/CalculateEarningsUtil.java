/**   
 * Copyright © 2015 公司名. All rights reserved.
 * 
 * @Title: CalculateEarningsUtil.java 
 * @Prject: app-client-api-util
 * @Package: com.fenlibao.p2p.util.pay 
 * @Description: TODO
 * @author: Administrator   
 * @date: 2015-9-16 下午3:41:45 
 * @version: V1.0.0   
 */
package com.fenlibao.p2p.util.pay;

import java.math.BigDecimal;

/** 
 * @ClassName: CalculateEarningsUtil 
 * @Description: TODO
 * @author: Administrator
 * @date: 2015-9-16 下午3:41:45  
 */
public class CalculateEarningsUtil {

    protected  static final int REPAY_DAYS_OF_YEAR = 365;

    protected  static final int REPAY_MONTH_OF_YEAR = 12;
	
	public static BigDecimal calEarnings(String paymentType, BigDecimal money, double rate, int month){
		
		BigDecimal earnings = new BigDecimal(0);
		switch (paymentType)
            {
                case "DEBX":
                {
                    break;
                }
                case "MYFX":
                {
                    break;
                }
                case "YCFQ":
                {
                	earnings = money.multiply(new BigDecimal(rate)).multiply(new BigDecimal(month)).divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP);
                    break;
                }
                case "DEBJ":
                {
                    break;
                }
                default:
                	earnings = new BigDecimal(0);
            }
		return earnings;
	}
	
	public static BigDecimal calEarningsByDays(String paymentType, BigDecimal money, double rate, int days){
		
		BigDecimal earnings = new BigDecimal(0);
		switch (paymentType)
            {
                case "DEBX":
                {
                    break;
                }
                case "MYFX":
                {
                    break;
                }
                case "YCFQ":
                {
                	earnings = money.multiply(new BigDecimal(rate)).multiply(new BigDecimal(days)).divide(new BigDecimal(365), 2, BigDecimal.ROUND_HALF_UP);
                    break;
                }
                case "DEBJ":
                {
                    break;
                }
                default:
                	earnings = new BigDecimal(0);
            }
		return earnings;
	}

    //天标的计算公式：债权本金 + 原债权预期收益 - 债权价格) / 债权价格* 365 / 剩余持有天数
    public static double transferYieldForDay(double collectInterest,double transferOutPriceint ,int surplusDays){
        double transferYield = (collectInterest - transferOutPriceint)/transferOutPriceint*REPAY_DAYS_OF_YEAR/surplusDays;
        BigDecimal b = new BigDecimal(transferYield);
        return b.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

	public static void main(String[] args){
		System.out.println(calEarningsByDays("YCFQ",new BigDecimal(3000),0.08,10));
	}
}
