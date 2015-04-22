package com.dkhs.portfolio.ui.widget.kline;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PageOHLCEntity {
    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("current_page")
    private int page;
    private List<OHLCEntity> results;
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public List<OHLCEntity> getResults() {
        return results;
    }
    public void setResults(List<OHLCEntity> results) {
        this.results = results;
    }
    
}
