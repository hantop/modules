package com.fenlibao.p2p.common.util.repayment;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 根据还款方式计算本息
 * @author laubrence
 * @date 2016-3-28 13:41:15
 */
public class CalCapitalInterestUtil {

    protected static final int DECIMAL_SCALE = 9;

    protected  static final int REPAY_DAYS_OF_YEAR = 365;

    protected  static final int REPAY_MONTH_OF_YEAR = 12;

    /**
     * 计算总利息
     */
    public static BigDecimal calTotalInterest(BigDecimal capitalAmount, BigDecimal yearYield, int loanMonths, String replaymentMode) throws Exception {

        if (!StringUtils.isNotEmpty(replaymentMode)){
            throw new Exception("不支持的还款方式");
        }
        switch (replaymentMode) {
            case "YCFQ":{
                return calTotalInterestYCFQ_MONTH(capitalAmount, yearYield,loanMonths);
            }
            case "DEBX": {
                return calTotalCapitalAndInterestDEBX(capitalAmount,yearYield,loanMonths).subtract(capitalAmount);
            }
            case "MYFX": {
                return calTotalInterestMYFX(capitalAmount,yearYield,loanMonths);
            }
            default:
                throw new Exception("不支持的还款方式");
        }
    }

    /**
     * 计算一次付清的总利息(按月)
     * @param capitalAmount 借款本金
     * @param yearYield 年华收益率
     * @param loanMonths 借款总月数
     * @return
     */
    private static BigDecimal calTotalInterestYCFQ_MONTH(BigDecimal capitalAmount, BigDecimal yearYield, int loanMonths) {
        return capitalAmount.multiply(yearYield)
                .multiply(new BigDecimal(loanMonths))
                .divide(new BigDecimal(REPAY_MONTH_OF_YEAR), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    /**
     * 计算一次付清的总利息(按天)
     * @param capitalAmount 借款本金
     * @param yearYield 年华收益率
     * @param loanDays 借款总天数
     * @return
     */
    public static BigDecimal calTotalInterestYCFQ_DAY(BigDecimal capitalAmount, BigDecimal yearYield, int loanDays){
        return   capitalAmount.multiply(yearYield)
                .multiply(new BigDecimal(loanDays))
                .divide(new BigDecimal(REPAY_DAYS_OF_YEAR), DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算每月付息的总利息
     * @param capitalAmount
     * @param yearYield
     * @param loanMonths
     * @return
     */
    private static BigDecimal calTotalInterestMYFX(BigDecimal capitalAmount, BigDecimal yearYield, int loanMonths){
        return (capitalAmount.multiply(yearYield)
                                .divide(new BigDecimal(REPAY_MONTH_OF_YEAR), DECIMAL_SCALE, BigDecimal.ROUND_DOWN)
                                .multiply(new BigDecimal(loanMonths))).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    /*
    public static BigDecimal calTotalInterestDEBX(BigDecimal capitalAmount, BigDecimal yearYield, int loanMonths){
        BigDecimal monthRate = yearYield.setScale(DECIMAL_SCALE,
                BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(12),
                DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        BigDecimal monthPayTotal = debx(capitalAmount, monthRate, loanMonths);
        return (monthPayTotal.multiply(new BigDecimal(loanMonths))
                                .subtract(capitalAmount)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    private static BigDecimal debx(BigDecimal total, BigDecimal monthRate, int terms) {
        BigDecimal tmp = monthRate.add(new BigDecimal(1)).pow(terms).setScale(DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        return total
                .multiply(monthRate)
                .multiply(tmp)
                .divide(tmp.subtract(new BigDecimal(1)), 2,
                        BigDecimal.ROUND_HALF_UP);
    }*/

    /**
     * 计算等额本息的所有本金和利息和
     * @param capitalAmount 本金
     * @param yearYield 年华收益率
     * @param loanMonths 借款总月数
     * @return
     */
    private static BigDecimal calTotalCapitalAndInterestDEBX(BigDecimal capitalAmount, BigDecimal yearYield, int loanMonths){
        // 每月本息金额  = (本金×月利率×(1＋月利率)＾还款月数)÷ ((1＋月利率)＾还款月数-1)
        BigDecimal monthRate = yearYield.divide(new BigDecimal(REPAY_MONTH_OF_YEAR),
                DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        BigDecimal tmp = monthRate.add(new BigDecimal(1)).pow(loanMonths);
        BigDecimal monthIncome = capitalAmount.multiply(monthRate)
                                                .multiply(tmp)
                                                .divide(tmp.subtract(new BigDecimal(1)), 2, BigDecimal.ROUND_HALF_UP);
        //System.out.println("每月本息金额 : " + monthIncome);
        return monthIncome.multiply(new BigDecimal(loanMonths));
    }

    /**
     * 计算等额本息第N个月的本金
     * @param capitalAmount 本金
     * @param yearYield 年华收益率
     * @param loanMonths 借款总月数
     * @param month  第N个月
     * @return
     * @throws Exception
     */
    private static BigDecimal calEveryMonthCapitalDEBX(BigDecimal capitalAmount, BigDecimal yearYield, int loanMonths, int month) throws Exception{
        if(loanMonths<1 || month<1 || loanMonths<month){
            throw new Exception("当前月数不正确");
        }
        // 每月本金 = 本金×月利率×(1+月利率)^(还款月序号-1)÷((1+月利率)^还款月数-1)
        BigDecimal monthCapital;
        BigDecimal monthRate = yearYield.divide(new BigDecimal(REPAY_MONTH_OF_YEAR),
                DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        monthCapital = capitalAmount.multiply(monthRate).multiply(new BigDecimal(Math.pow((1 + monthRate.doubleValue()),
                month - 1))).divide(new BigDecimal(Math.pow(1 + monthRate.doubleValue(), loanMonths) - 1), 2, BigDecimal.ROUND_HALF_UP);
        //System.out.println("第" + month + "月本金： " + monthCapital);
        return monthCapital;
    }

    /**
     * 计算等额本息第N个月的利息
     * @param capitalAmount 本金
     * @param yearYield 年华收益率
     * @param loanMonths 借款总月数
     * @param month 第N个月
     * @return
     * @throws Exception
     */
    private static BigDecimal calEveryMonthInterestDEBX(BigDecimal capitalAmount, BigDecimal yearYield, int loanMonths, int month) throws Exception {
        if(loanMonths<1 || month<1 || loanMonths<month){
            throw new Exception("当前月数不正确");
        }
        // 每月利息  = 剩余本金 x 贷款月利率
        BigDecimal monthInterest = BigDecimal.ZERO;
        BigDecimal capital = capitalAmount;
        BigDecimal tmpCapital = BigDecimal.ZERO;

        BigDecimal monthRate = yearYield.divide(new BigDecimal(12),
                DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        for (int i = 1; i < month + 1; i++) {
            capital = capital.subtract(tmpCapital);
            monthInterest = capital.multiply(monthRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            tmpCapital = calEveryMonthCapitalDEBX(capitalAmount,yearYield,loanMonths,i);
        }
        //System.out.println("第" + month + "月利息： " + monthInterest);
        return monthInterest;
    }

    /**
     * 计算等额本息所有月数的总本金
     * @param capitalAmount 本金
     * @param yearYield 年华收益率
     * @param loanMonths 借款总月数
     * @return
     * @throws Exception
     */
    private static BigDecimal calTotalMonthCapitalDEBX(BigDecimal capitalAmount, BigDecimal yearYield, int loanMonths) throws Exception {
        BigDecimal monthCapital;
        BigDecimal sumInterest = BigDecimal.ZERO;

        BigDecimal monthRate = yearYield.divide(new BigDecimal(REPAY_MONTH_OF_YEAR),
                DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        for (int i=1; i<loanMonths+1; i++) {
            monthCapital = calEveryMonthCapitalDEBX(capitalAmount,yearYield,loanMonths,i);
            sumInterest = sumInterest.add(monthCapital);
        }
        return sumInterest;
    }

    /**
     * 计算等额本息所有月数的总利息
     * @param capitalAmount 本金
     * @param yearYield 年华收益率
     * @param loanMonths 借款总月数
     * @return
     * @throws Exception
     */
    private static BigDecimal calTotalMonthInterestDEBX(BigDecimal capitalAmount, BigDecimal yearYield, int loanMonths) throws Exception {
        BigDecimal monthInterest;
        BigDecimal capital = capitalAmount;
        BigDecimal tmpCapital = BigDecimal.ZERO;
        BigDecimal sumInterest = BigDecimal.ZERO;

        BigDecimal monthRate = yearYield.divide(new BigDecimal(12),
                DECIMAL_SCALE, BigDecimal.ROUND_HALF_UP);
        for (int i=1; i<loanMonths+1; i++) {
            capital = capital.subtract(tmpCapital);
            monthInterest = capital.multiply(monthRate).setScale(2, BigDecimal.ROUND_DOWN);
            tmpCapital = calEveryMonthCapitalDEBX(capitalAmount,yearYield,loanMonths,i);

            sumInterest = sumInterest.add(monthInterest);
        }
        return sumInterest;
    }

    public static void main( String[] args){
        //System.out.println(new BigDecimal(1).divide(new BigDecimal(1).divide(new BigDecimal(3))));
        //System.out.println(new BigDecimal(1).divide(new BigDecimal(12)));
        BigDecimal capitalAmount = new BigDecimal(991);
        BigDecimal yearYield = new BigDecimal(0.09);
        int loanMonths = 12;
        try {
            System.out.println(CalCapitalInterestUtil.calTotalInterestYCFQ_DAY(capitalAmount,yearYield,13));
            System.out.println("-------------一次付清：利息和-------------------");
            System.out.println(CalCapitalInterestUtil.calTotalInterest(capitalAmount, yearYield, loanMonths, "YCFQ"));

            System.out.println("-------------等额本息：利息和-------------------");
            System.out.println(CalCapitalInterestUtil.calTotalInterest(capitalAmount, yearYield, loanMonths, "DEBX"));

            System.out.println("-------------每月付息：利息和-------------------");
            System.out.println(CalCapitalInterestUtil.calTotalInterest(capitalAmount, yearYield, loanMonths, "MYFX"));

            System.out.println("-------------等额本息：本息和-------------------");
            System.out.println(CalCapitalInterestUtil.calTotalCapitalAndInterestDEBX(capitalAmount, yearYield, loanMonths));

            System.out.println("-------------等额本息：每月本金-------------------");
            for(int i =1 ;i< loanMonths+1;i++) {
                System.out.println(CalCapitalInterestUtil.calEveryMonthCapitalDEBX(capitalAmount, yearYield, loanMonths,i));
            }

            System.out.println("-------------等额本息：每月利息------------------");
            for(int i =1 ;i< loanMonths+1;i++) {
                System.out.println(CalCapitalInterestUtil.calEveryMonthInterestDEBX(capitalAmount, yearYield, loanMonths, i));
            }

            System.out.println("-------------等额本息：总本金和-------------------");

            System.out.println(CalCapitalInterestUtil.calTotalMonthCapitalDEBX(capitalAmount, yearYield, loanMonths));

            System.out.println("-------------等额本息：总利息和-------------------");

            System.out.println(CalCapitalInterestUtil.calTotalMonthInterestDEBX(capitalAmount, yearYield, loanMonths));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}