/**
 * @Title MoreDataBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-23 下午1:00:18
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName MoreDataBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-23 下午1:00:18
 * @version 1.0
 */
public class MoreDataBean<T> {
    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("total_page")
    private int totalPage;
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("trade_status")
    private int statu;
    @SerializedName("results")
    private List<T> results;



    @SerializedName("user_portfolios_count")
    private int portfoliosCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public int getPortfoliosCount() {
        return portfoliosCount;
    }

    public void setPortfoliosCount(int portfoliosCount) {
        this.portfoliosCount = portfoliosCount;
    }

    public static class EmptyMoreBean extends MoreDataBean {
        public EmptyMoreBean() {
            setTotalCount(0);
            setCurrentPage(0);
            setTotalPage(0);
            setStatu(-1);
            setResults(Collections.EMPTY_LIST);
        }
    }

    public void copyMoreDataBean(MoreDataBean moreBean) {
        this.currentPage = moreBean.getCurrentPage();
        // this.results = new ArrayList();
        this.statu = moreBean.getStatu();
        this.totalCount = moreBean.getTotalCount();
        this.totalPage = moreBean.getTotalPage();

    }

}
