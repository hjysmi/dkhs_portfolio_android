package com.dkhs.portfolio.bean;

/**
 * Created by xuetong on 2015/12/10.
 */
public class PersonalEventBean {
    //0, '审核中' 1, '已认证'
    public int verified_status = 0;
    public PersonalEventBean(){

    }
    public PersonalEventBean(int verified_status){
        this.verified_status = verified_status;
    }
}
