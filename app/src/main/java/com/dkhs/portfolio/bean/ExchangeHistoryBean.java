package com.dkhs.portfolio.bean;

/**
 * Created by wuyongsen on 2015/9/24.
 */
public class ExchangeHistoryBean {
    public int id;
    public int package_amount;
    public int cost;
    public int status;
    public String status_explain;
    public String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPackage_amount() {
        return package_amount;
    }

    public void setPackage_amount(int package_amount) {
        this.package_amount = package_amount;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_explain() {
        return status_explain;
    }

    public void setStatus_explain(String status_explain) {
        this.status_explain = status_explain;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
