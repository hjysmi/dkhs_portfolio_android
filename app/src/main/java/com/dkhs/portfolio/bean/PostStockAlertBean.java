/**
 * @Title AlertSetBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-14 下午4:07:40
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import org.parceler.Parcel;

/**
 * @author zjz
 * @version 1.0
 * @ClassName AlertSetBean
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-4-14 下午4:07:40
 */
@Parcel
public class PostStockAlertBean {
    float stock_price_up;
    float stock_price_down;
    float stock_percentage;
    int stock_company_report;
    int stock_company_research;


    public PostStockAlertBean() { /*Required empty bean constructor*/ }

    public PostStockAlertBean(float priceUp, float priceDown, float percent, boolean setNotice, boolean setYanbao) {
        this.stock_price_up = priceUp;
        this.stock_price_down = priceDown;
        this.stock_percentage = percent;
        setNotice(setNotice);
        setYanbao(setYanbao);
    }

    public float getStock_price_up() {
        return stock_price_up;
    }

    public void setStock_price_up(float stock_price_up) {
        this.stock_price_up = stock_price_up;
    }

    public float getStock_price_down() {
        return stock_price_down;
    }

    public void setStock_price_down(float stock_price_down) {
        this.stock_price_down = stock_price_down;
    }

    public float getStock_percentage() {
        return stock_percentage;
    }

    public void setStock_percentage(float stock_percentage) {
        this.stock_percentage = stock_percentage;
    }

    public int getStock_company_report() {
        return stock_company_report;
    }

    public void setStock_company_report(int stock_company_report) {
        this.stock_company_report = stock_company_report;
    }

    public int getStock_company_research() {
        return stock_company_research;
    }

    public void setStock_company_research(int stock_company_research) {
        this.stock_company_research = stock_company_research;
    }

    public void setNotice(boolean isNotice) {
        if (isNotice) {
            setStock_company_report(1);
        } else {

            setStock_company_report(0);
        }
    }

    public void setYanbao(boolean isyanbao) {
        if (isyanbao) {
            setStock_company_research(1);
        } else {
            setStock_company_research(0);
        }
    }

    public boolean isNoticeRemind() {
        return getStock_company_report() == 1;
    }

    public boolean isYanbaoRemind() {
        return getStock_company_research() == 1;
    }


}
