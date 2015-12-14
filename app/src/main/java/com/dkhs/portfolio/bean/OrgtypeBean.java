package com.dkhs.portfolio.bean;

/**
 * Created by xuetong on 2015/12/11.
 */
public class OrgtypeBean {
    private int type;
    private String orgName;

    public OrgtypeBean() {
    }

    public OrgtypeBean(String orgName, int type) {
        this.orgName = orgName;
        this.type = type;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
