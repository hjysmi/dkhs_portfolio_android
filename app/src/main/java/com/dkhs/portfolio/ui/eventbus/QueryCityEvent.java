package com.dkhs.portfolio.ui.eventbus;

import com.dkhs.portfolio.bean.CityBean;

import java.util.List;

/**
 * Created by wuyongsen on 2015/12/8.
 */
public class QueryCityEvent {
    public List<CityBean> beans;
    public QueryCityEvent(List<CityBean> benas){
        this.beans = benas;
    }
}
