package com.dkhs.portfolio.bean;

import org.parceler.Parcel;

/**
 * Created by zjz on 2015/6/8.
 */
@Parcel
public class ManagersEntity {
    /**
     * id : 302001636
     * cp_rate : 30.38
     * name : 张少华
     * start_date : 2010-10-22
     */
    int id;
    float cp_rate;
    String name;
    String start_date;


    public void setId(int id) {
        this.id = id;
    }

    public void setCp_rate(float cp_rate) {
        this.cp_rate = cp_rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public int getId() {
        return id;
    }

    public float getCp_rate() {
        return cp_rate;
    }

    public String getName() {
        return name;
    }

    public String getStart_date() {
        return start_date;
    }
}
