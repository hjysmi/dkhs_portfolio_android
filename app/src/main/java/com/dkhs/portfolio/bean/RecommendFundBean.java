package com.dkhs.portfolio.bean;

import java.util.ArrayList;

/**
 * Created by wuyongsen on 2015/11/25.
 */
public class RecommendFundBean {
    public ArrayList<FundPriceBean> data;
    public RecommendFundBean(ArrayList<FundPriceBean> data){
        this.data = data;
    }
}
