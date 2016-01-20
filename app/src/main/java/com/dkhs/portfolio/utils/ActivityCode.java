package com.dkhs.portfolio.utils;

/**
 * Created by zhangcm on 2016/1/13.
 */
public enum ActivityCode {
    //交易密码设置
    TRADE_PASSWORD_SETTING_REQUEST(1),
    TRADE_PASSWORD_SETTING_RESULT(2),
    //选择银行
    CHOOSE_BANK_REQUEST(3),
    CHOOSE_BANK_RESULT(4),
    //银行卡号
    BANK_CARD_NO_REQUEST(5),
    BANK_CARD_NO_RESULT(6),
    //忘记交易密码
    FORGET_TRADE_PASSWORD_REQUEST(7),
    FORGET_TRADE_PASSWORD_RESULT(8),
    //银行卡信息
    BANK_CARD_INFO_REQUEST(9),
    BANK_CARD_INFO_RESULT(10),
    //话题详情
    TOPIC_DETAIL_REQUEST(9),
    TOPIC_DETAIL_RESULT(10),
    ;
    private int code;

    private ActivityCode(int var) {
        code = var;
    }

    public static ActivityCode valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
}
