package com.dkhs.portfolio.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by zjz on 2015/7/15.
 */
@Table(name = "SearchHistoryBean")
public class SearchHistoryBean extends SearchStockBean {


    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    @Column(column = "saveTime")
    private long saveTime;

}
