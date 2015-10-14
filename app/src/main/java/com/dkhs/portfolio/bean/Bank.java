package com.dkhs.portfolio.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Bank implements Serializable{

    private String id;
    private String name;
    private String logo;
    private String bank_card_no_tail;
    private String single_limit;
    private String single_day_limit;

    public String getSingle_limit() {
        return single_limit;
    }

    public void setSingle_limit(String single_limit) {
        this.single_limit = single_limit;
    }

    public String getSingle_day_limit() {
        return single_day_limit;
    }

    public void setSingle_day_limit(String single_day_limit) {
        this.single_day_limit = single_day_limit;
    }

    public String getBank_card_no_tail() {
        return bank_card_no_tail;
    }

    public void setBank_card_no_tail(String bank_card_no_tail) {
        this.bank_card_no_tail = bank_card_no_tail;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
}
