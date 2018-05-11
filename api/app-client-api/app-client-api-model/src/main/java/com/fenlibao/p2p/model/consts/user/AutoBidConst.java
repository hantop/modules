package com.fenlibao.p2p.model.consts.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动投标设置参数
 *
 * @author Mingway.Xu
 * @date 2016/11/14 9:56
 */
public class AutoBidConst {

    /**
     * 年化率
     */
    public static final BigDecimal[] INTEREST_RATE = {
            BigDecimal.valueOf(0.075), BigDecimal.valueOf(0.076), BigDecimal.valueOf(0.077),
            BigDecimal.valueOf(0.078), BigDecimal.valueOf(0.079), BigDecimal.valueOf(0.080)
    };
    /**
     * 年利率列表
     */
    public static List<BigDecimal> interestRates = new ArrayList<>();



    /**
     * 投资期限
     */
    public static final String BID_TIME_INFINITY = "0D";
    public static final String BID_DAYS_TEN = "10D";
    public static final String BID_DAYS_TWENTY = "20D";
    public static final String BID_DAYS_THIRTY = "30D";
    public static final String BID_DAYS_FORTY = "40D";
    public static final String BID_DAYS_FIFTY = "50D";
    public static final String BID_DAYS_SIXTY = "60D";
    public static final String BID_MONTHS_THREE = "3M";
    public static final String BID_MONTHS_FOUR = "4M";
    public static final String BID_MONTHS_FIVE = "5M";
    public static final String BID_MONTHS_SIX = "6M";
    public static final String BID_MONTHS_SEVEN = "7M";
    public static final String BID_MONTHS_EIGHT = "8M";
    public static final String BID_MONTHS_NINE = "9M";
    public static final String BID_MONTHS_TEN = "10M";
    public static final String BID_MONTHS_ELEVEN = "11M";
    public static final String BID_MONTHS_TWELVE = "12M";
    public static final String BID_MONTHS_THIRTEEN = "13M";
    public static final String BID_MONTHS_FOURTEEN = "14M";
    public static final String BID_MONTHS_FIFTEEN = "15M";
    public static final String BID_MONTHS_SIXTEEN = "16M";
    public static final String BID_MONTHS_SEVENTEEN = "17M";
    public static final String BID_MONTHS_EIGHTEEN = "18M";
    public static final String BID_MONTHS_NINETEEN = "19M";
    public static final String BID_MONTHS_TWENTY = "20M";
    public static final String BID_MONTHS_TWENTY_ONE = "21M";
    public static final String BID_MONTHS_TWENTY_TWO = "22M";
    public static final String BID_MONTHS_TWENTY_THREE = "23M";
    public static final String BID_MONTHS_TWENTY_FOUR = "24M";

    public static final String TIME_MARK_DAY = "D";
    public static final String TIME_MARK_MONTHS = "M";


    public static final int AUTO_BID_ACTIVE = 1;
    public static final int AUTO_BID_UNACTIVE = 0;

    public static final int NO_DELETE_FLAG = 1;
    public static final int DELETE_FLAG = 0;

    public static final int HAVE_NEVER_AUTOSET = 0;//完全没有设置过自动投标
    public static final int HAVE_AUTOSET_NO_DELETE = 1;//已经设置
    public static final int HAVE_AUTOSET_DELETE = 2;//设置过已经删除

    public static final String DEFAULT_BID_TYPE = "XFXD";
    public static final String DEFAULT_REPAYMENT_MODE = "YCFQ";

    public static final BigDecimal RESERVE_LIMITED = new BigDecimal("999999999999999999.99");

    public static List<String> getBidTime() {
        List<String> result = new ArrayList<>();
        result.add(BID_TIME_INFINITY);
        result.add(BID_DAYS_TEN);
        result.add(BID_DAYS_TWENTY);
        result.add(BID_DAYS_THIRTY);
        result.add(BID_DAYS_FORTY);
        result.add(BID_DAYS_FIFTY);
        result.add(BID_DAYS_SIXTY);
        result.add(BID_MONTHS_THREE);
        result.add(BID_MONTHS_FOUR);
        result.add(BID_MONTHS_FIVE);
        result.add(BID_MONTHS_SIX);
        result.add(BID_MONTHS_SEVEN);
        result.add(BID_MONTHS_EIGHT);
        result.add(BID_MONTHS_NINE);
        result.add(BID_MONTHS_TEN);
        result.add(BID_MONTHS_ELEVEN);
        result.add(BID_MONTHS_TWELVE);
        result.add(BID_MONTHS_THIRTEEN);
        result.add(BID_MONTHS_FOURTEEN);
        result.add(BID_MONTHS_FIFTEEN);
        result.add(BID_MONTHS_SIXTEEN);
        result.add(BID_MONTHS_SEVENTEEN);
        result.add(BID_MONTHS_EIGHTEEN);
        result.add(BID_MONTHS_NINETEEN);
        result.add(BID_MONTHS_TWENTY);
        result.add(BID_MONTHS_TWENTY_ONE);
        result.add(BID_MONTHS_TWENTY_TWO);
        result.add(BID_MONTHS_TWENTY_THREE);
        result.add(BID_MONTHS_TWENTY_FOUR);

        return result;
    }

    public static List<BigDecimal> getInterestRates(){
        List<BigDecimal> interestRates = new ArrayList<>();
        for (BigDecimal i : AutoBidConst.INTEREST_RATE) {
            interestRates.add(i);
        }
        return interestRates;
    }

    /**
     * 校验年利率是否合法
     * @param rate
     * @return
     */
    public static boolean verifyInterestRate(BigDecimal rate) {
        return getInterestRates().contains(rate);
    }


}
