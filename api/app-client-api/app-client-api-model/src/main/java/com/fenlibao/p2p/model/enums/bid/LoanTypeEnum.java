package com.fenlibao.p2p.model.enums.bid;

/**
 *
 * by：chen
 */
public enum LoanTypeEnum {
    ONE(1,"类别1"),
    TWO(2,"类别2"),
    THREE(3,"类别3"),
    FOUR(4,"类别4"),
    FIVE(5,"类别5")
    ;

    private int code;

    private String message;

    LoanTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }



    public String getMessage() {
        return message;
    }

    public static int length() {
        return LoanTypeEnum.values().length;
    }
}
