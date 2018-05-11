package com.fenlibao.p2p.util;

/**
 * 产品周期类别
 * Created by chen on 2017/5/25.
 */
public class LoanTypeUtil {
    public static int type(int loanDay) {

        if (loanDay<=30) {
           return 1;
        }else if(loanDay>30&&loanDay<=90){
            return 2;
        }else if(loanDay>90&&loanDay<=180){
            return 3;
        }else if(loanDay>180&&loanDay<=360){
            return 4;
        }else {
            return 5;
        }
    }

    public static int[] dayArry(int type){
        int arry[] = new int[2];
        if(type == 1){
            arry[0]=0;
            arry[1]=30;
        }else if(type == 2){
            arry[0]=30;
            arry[1]=90;
        }else if(type == 3){
            arry[0] = 90;
            arry[1] = 180;
        }else if(type == 4){
            arry[0] = 180;
            arry[1] = 360;
        }else if(type == 0){
            arry[0] = 1200;
            arry[1] = 10000;
        }else if(type == 5){
            arry[0] = 360;
            arry[1] = 720;
        }
        return arry;
    }

}
