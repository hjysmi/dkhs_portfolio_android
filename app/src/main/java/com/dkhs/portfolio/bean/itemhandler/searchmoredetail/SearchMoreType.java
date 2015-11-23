package com.dkhs.portfolio.bean.itemhandler.searchmoredetail;

/**
 * Created by zhangcm on 2015/11/20.
 */
public enum SearchMoreType {
    //更多股票
    MORE_STOCK(0),
    //更多基金
    MORE_FUND(1),
    //更多基金经理
    MORE_FUND_MANAGER(2),
    //更多用户
    MORE_USER(3),
    //更多悬赏
    MORE_REWARD(4),
    //更多话题
    MORE_TOPIC(5),
    //更多组合
    MORE_COMBINATION(6);
    private int value;
    private SearchMoreType(int var){
        value=var;
    }
    public static SearchMoreType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
}
