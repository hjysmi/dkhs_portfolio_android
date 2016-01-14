package com.dkhs.portfolio.engine;

import org.parceler.Parcel;

@Parcel
public class NewsforModel {
    /**
     *
     */
    String symboName;
    String symbolId;
    String symbol;
    String userid;
    String portfolioId;
    String contentType;
    String contentSubType;
    String page;
    String pageSize;
    String pageTitle;
//    @ParcelClass(LibraryParcel.class)
//    LinearLayout layout;

//    public LinearLayout getLayout() {
//        return layout;
//    }
//
//    public void setLayout(LinearLayout layout) {
//        this.layout = layout;
//    }

    public String getSymboName() {
        return symboName;
    }

    public void setSymboName(String symboName) {
        this.symboName = symboName;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getSymbolId() {
        return symbolId;
    }

    public void setSymbolId(String symbolId) {
        this.symbolId = symbolId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentSubType() {
        return contentSubType;
    }

    public void setContentSubType(String contentSubType) {
        this.contentSubType = contentSubType;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

}
