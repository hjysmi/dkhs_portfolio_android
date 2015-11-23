package com.dkhs.portfolio.bean;

import java.util.List;

/**
 * Created by xuetong on 2015/11/16.
 * 综合搜索
 */
public class SearchGeneralBean {
    //股票基金
    private List<QuotesBean> symbols;
    //基金经理
    private List<FundManagerBean> fund_managers;
    //用户
    private List<UserEntity> users;
    //组合
    private List<CombinationBean> portfolios;

    public List<FundManagerBean> getFund_managers() {
        return fund_managers;
    }

    public void setFund_managers(List<FundManagerBean> fund_managers) {
        this.fund_managers = fund_managers;
    }

    public List<CombinationBean> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<CombinationBean> portfolios) {
        this.portfolios = portfolios;
    }

    public List<QuotesBean> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<QuotesBean> symbols) {
        this.symbols = symbols;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "SearchGeneralBean{" +
                "fund_managers=" + fund_managers +
                ", symbols=" + symbols +
                ", users=" + users +
                ", portfolios=" + portfolios +
                '}';
    }
}
