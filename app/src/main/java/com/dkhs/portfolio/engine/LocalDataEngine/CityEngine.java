package com.dkhs.portfolio.engine.LocalDataEngine;

import com.dkhs.portfolio.app.AppConfig;
import com.dkhs.portfolio.bean.CityBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.eventbus.QueryCityEvent;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuyongsen on 2015/12/8.
 */
public class CityEngine {

    /**
     * 获取城市列表
     */
    public static void getProvice() {
        new Thread() {
            @Override
            public void run() {
                DbUtils dbUtils = AppConfig.getDBUtils();
                List<CityBean> cityBeans = new ArrayList<CityBean>();
                try {

                    cityBeans = dbUtils
                            .findAll(Selector.from(CityBean.class).where(CityBean.TYPE, "=", "1").orderBy(CityBean.PINYIN, false)
                            );
                    BusProvider.getInstance().post(new QueryCityEvent(cityBeans));

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 获取省，直辖市
     * @param parentCode
     */
    public static void getCity(final String parentCode) {
        new Thread() {
            @Override
            public void run() {
                DbUtils dbUtils = AppConfig.getDBUtils();
                List<CityBean> cityBeans = new ArrayList<CityBean>();
                try {

                    cityBeans = dbUtils
                            .findAll(Selector.from(CityBean.class).where(CityBean.TYPE, "=", "2").and(CityBean.PARENT_CODE, "=", parentCode).orderBy(CityBean.PINYIN, false)
                            );

                    BusProvider.getInstance().post(new QueryCityEvent(cityBeans));

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
