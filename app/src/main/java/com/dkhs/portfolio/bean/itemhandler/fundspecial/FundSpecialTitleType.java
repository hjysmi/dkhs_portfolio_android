package com.dkhs.portfolio.bean.itemhandler.fundspecial;

/**
 * Created by zhangcm on 2015/11/20.
 */
public enum FundSpecialTitleType {
    //基金超市,查看全部
    TITLE_FUND_MARKET(0),
    //基金经理,查看更多
    TITLE_FUND_MANAGER(1),
    //专题理财
    TITLE_SPECIAL(2);
    private int value;
    private FundSpecialTitleType(int var){
        value=var;
    }
    public static FundSpecialTitleType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
}
