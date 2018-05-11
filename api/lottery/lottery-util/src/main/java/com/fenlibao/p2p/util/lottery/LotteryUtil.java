package com.fenlibao.p2p.util.lottery;

import java.util.*;

/**
 * Created by laubrence on 2016/4/27.
 */
public class LotteryUtil {

    /**
     * 抽奖
     * @param orignalRates 原始的概率列表，保证顺序和实际物品对应
     * @return 物品的索引
     */
    public static int lottery(List<Double> orignalRates) {
        if (orignalRates == null || orignalRates.isEmpty()) {
            return -1;
        }
        int size = orignalRates.size();
        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : orignalRates) {
            sumRate += rate;
        }
        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<Double>(size);
        Double tempSumRate = 0d;
        for (double rate : orignalRates) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }
        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        return sortOrignalRates.indexOf(nextDouble);
    }

    public static void main(String[] args){
//        List<Double> orignalRates = new ArrayList<>();
//        orignalRates.add(0.005d);
//        orignalRates.add(0.5d);
//        orignalRates.add(0.25d);
//        orignalRates.add(0.225d);
//        orignalRates.add(0.02d);
//
//        // statistics
//        Map<Integer, Integer> count = new HashMap<Integer, Integer>();
//        double num = 10000000;
//        System.out.println(System.currentTimeMillis());
//        for (int i = 0; i < num; i++) {
//            int orignalIndex = LotteryUtil.lottery(orignalRates);
//
//            Integer value = count.get(orignalIndex);
//            count.put(orignalIndex, value == null ? 1 : value + 1);
//        }
//
//        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
//            System.out.println(entry.getKey() + ", count=" + entry.getValue() + ", probability=" + entry.getValue() / num);
//        }
//        System.out.println(System.currentTimeMillis());

        System.out.println(2<<3);
    }


}
