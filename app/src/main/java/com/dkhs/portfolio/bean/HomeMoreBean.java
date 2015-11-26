package com.dkhs.portfolio.bean;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class HomeMoreBean {
    public static final int TYPE_FUND = 0;
    public static final int TYPE_FUND_MANAGER = 1;
    public static final int TYPE_PORTFOLIO = 2;
    public int type;
    public boolean hide;
    public HomeMoreBean(int type){
        this.type = type;
    }

    public HomeMoreBean(int type,boolean hide){
        this.type = type;
        this.hide = hide;
    }

    public String getTitle(){
        String title = "";
        switch (type){
            case TYPE_FUND_MANAGER:
                title = "基金经理";
                break;
            case TYPE_FUND:
                title = "推荐基金";
                break;
            case TYPE_PORTFOLIO:
                title = "推荐组合";
                break;
            default:
                break;
        }
        return title;
    }


}
