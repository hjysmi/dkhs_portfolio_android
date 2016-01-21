/**
 * @Title SelectStockBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 下午2:42:46
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import android.text.TextUtils;

import com.dkhs.portfolio.ui.widget.ViewBean.SelectStockFundViewBean;
import com.google.gson.annotations.SerializedName;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * @author zjz
 * @version 1.0
 * @ClassName SelectStockBean
 * @Description 添加自选股，用于UI显示的Bean
 * @date 2014-9-18 下午2:42:46
 */
@Parcel
public class SelectStockBean extends DragListItem implements Serializable{

    @SerializedName("abbr_name")
    public String name;
    public float currentValue;
    public float percentage;
    @NoAutoIncrement
    public long id;


    public String code;
    public String symbol;
    public float change;
    public boolean isFollowed;
    //对应数据库的停牌
    public boolean isStop;
     public boolean is_alert;
    //对应网络数据的停牌
    public int is_stop;

    public AlertSetBean alertSetBean;

    public long sortId;
    public int status;
    public float total_capital;

    // (0, '其他'),(1, '股票'),(2, '债券'),(3, '基金'),(4, '权证'),(5, '指数'),(6, '集合理财'),(9, '期货'),(10, '期权')
    public String symbol_type;
    public int symbol_stype;
    // public String symbol;
    // 2,='暂停交易' 3='终止上市'
    public String list_status;


    //万份收益率
    public float tenthou_unit_incm;
    //七日年化收益率
    public float year_yld;
    public String tradeDay;
//
//    public DataEntry<SelectStockBean> entry = null;
//
//    public SelectStockBean() {
//        this.entry = new DataEntry<SelectStockBean>();
//    }
//
//    public DataEntry<SelectStockBean> getEntry() {
//        return entry;
//    }
//
//    public void setEntry(DataEntry<SelectStockBean> entry) {
//        this.entry = entry;
//    }

    public static SelectStockBean copy(ConStockBean stockBean) {

        SelectStockBean bean = new SelectStockBean();
        bean.name = stockBean.getName();
        bean.currentValue = stockBean.getCurrentValue();
        // bean.percentage = stockBean.getPercent();
        bean.symbol = stockBean.getStockSymbol();
        bean.id = Integer.parseInt(stockBean.getStockCode());
        bean.code = stockBean.getStockSymbol();
        bean.isStop = stockBean.isStop();
        bean.list_status = stockBean.getList_status();
        return bean;
    }

    public static SelectStockBean copy(FundManagerInfoBean.AchivementsEntity stockBean) {

        SelectStockBean bean = new SelectStockBean();
        bean.name = stockBean.getFund().getAbbr_name();
        // bean.percentage = stockBean.getPercent();
        bean.symbol = stockBean.getFund().getSymbol();
        bean.id = stockBean.getFund().getId();
        bean.symbol_stype = stockBean.getFund().getSymbol_stype();
//        bean.isStop = stockBean.isStop();
        bean.list_status = stockBean.getFund().getList_status() + "";
        return bean;
    }

    public static SelectStockBean copy(FundQuoteBean symbolsBean) {
        SelectStockBean bean = new SelectStockBean();
        bean.name = symbolsBean.getAbbrName();
        bean.id = symbolsBean.id;
        bean.code = symbolsBean.code;
        bean.symbol = symbolsBean.symbol;
        bean.symbol_stype = symbolsBean.symbol_stype;
        bean.symbol_type = symbolsBean.symbol_type;
        bean.tradeDay = symbolsBean.getTradedate();
        bean.list_status = String.valueOf(symbolsBean.getList_status());
        bean.isFollowed = symbolsBean.isFollowed();
        return bean;
    }

    public static SelectStockBean copy(SearchStockBean stockBean) {

        SelectStockBean bean = new SelectStockBean();
        bean.name = stockBean.getStockName();
        // bean.currentValue = stockBean.getCurrentValue();
        // bean.percentage = stockBean.getPercent();
        bean.id = stockBean.getId();
        bean.code = stockBean.getStockCode();
        bean.symbol = stockBean.getSymbol();
        bean.symbol_type = stockBean.getSymbol_type();
        if (!TextUtils.isEmpty(stockBean.getSymbol_stype())) {
            try {
                bean.symbol_stype = Integer.parseInt(stockBean.getSymbol_stype().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bean.isStop = stockBean.isStop();
        bean.list_status = stockBean.getList_status();
        return bean;
    }


    public static SelectStockBean copy(StockPriceBean stockBean) {

        SelectStockBean selectBean = new SelectStockBean();
        selectBean.id = stockBean.getId();
        selectBean.name = stockBean.getAbbrname();
        selectBean.currentValue = stockBean.getCurrent();
        selectBean.code = stockBean.getCode();
        selectBean.symbol = stockBean.getSymbol();
        selectBean.percentage = stockBean.getPercentage();
        selectBean.isFollowed = stockBean.isFollowed();
        selectBean.symbol_type = stockBean.getSymbol_type();
        selectBean.isStop = stockBean.isStop();
        selectBean.sortId = stockBean.getIndex();
        selectBean.change = stockBean.getChange();
        selectBean.list_status = stockBean.getList_status();
        selectBean.total_capital = stockBean.getTotal_capital();
        selectBean.alertSetBean = stockBean.getAlertBean();
        selectBean.is_alert = stockBean.is_alert;
        // selectBean.symbol = stockBean.getSymbol();
        return selectBean;
    }

    public static SelectStockBean copy(FundPriceBean stockBean) {

        SelectStockBean selectBean = new SelectStockBean();
        selectBean.id = stockBean.getId();
        selectBean.name = stockBean.getAbbrname();
        selectBean.currentValue = stockBean.getNet_value();
        selectBean.code = stockBean.getCode();
        selectBean.symbol = stockBean.getSymbol();
        selectBean.percentage = stockBean.getPercent_day();
        selectBean.isFollowed = stockBean.isFollowed();
        selectBean.symbol_type = stockBean.getSymbol_type();
        selectBean.symbol_stype = stockBean.getSymbol_stype();
        selectBean.isStop = stockBean.isStop();
        selectBean.sortId = stockBean.getIndex();
        selectBean.change = stockBean.getPercent_month();
        selectBean.list_status = stockBean.getList_status();
        selectBean.total_capital = stockBean.getPercent_tyear();
        selectBean.alertSetBean = stockBean.getAlertBean();
        selectBean.tenthou_unit_incm = stockBean.getTenthou_unit_incm();
        selectBean.year_yld = stockBean.getYear_yld();
        selectBean.tradeDay = stockBean.getTradedate();
        selectBean.is_alert = stockBean.is_alert;
        return selectBean;
    }

    public static SelectStockBean copy(QuotesBean stockBean) {

        SelectStockBean selectBean = new SelectStockBean();
        selectBean.id = stockBean.getId();
        selectBean.name = stockBean.getAbbrName();
        selectBean.currentValue = stockBean.getCurrent();
        selectBean.code = stockBean.getCode();
        selectBean.symbol = stockBean.getSymbol();
        selectBean.percentage = stockBean.getPercentage();
        selectBean.isFollowed = stockBean.isFollowed();
        selectBean.symbol_type = stockBean.getSymbol_type();
        // selectBean.isStop = stockBean.;
        // selectBean.sortId = stockBean.getIndex();
        selectBean.alertSetBean = stockBean.getAlertSetBean();
        // selectBean.list_status = stockBean.get;
        return selectBean;
    }

    public static SelectStockBean copy(StockQuotesBean stockBean) {

        SelectStockBean selectBean = new SelectStockBean();
        selectBean.id = stockBean.getId();
        selectBean.name = stockBean.getAbbrName();
        selectBean.currentValue = stockBean.getCurrent();
        selectBean.code = stockBean.getCode();
        selectBean.symbol = stockBean.getSymbol();
        selectBean.percentage = stockBean.getPercentage();
        selectBean.isFollowed = stockBean.isFollowed();
        selectBean.symbol_type = stockBean.getSymbol_type();
        selectBean.isStop = stockBean.getIs_stop() == 1;
//        selectBean.sortId = stockBean.getso;
        selectBean.change = stockBean.getChange();
        selectBean.list_status = stockBean.getList_status() + "";
        selectBean.total_capital = stockBean.getTotal_capital();
        selectBean.alertSetBean = stockBean.getAlertSetBean();
        return selectBean;
    }


    public ConStockBean parseStock() {

        ConStockBean bean = new ConStockBean();
        bean.setName(name);
        bean.setCurrentValue(currentValue);
        // bean.setPercent(percentage);
        bean.setStockId(id);
        bean.setStockSymbol(symbol);
        bean.setIsStop(isStop ? 1 : 0);
        bean.setStockCode(id + "");
        bean.setList_status(list_status);
        return bean;
    }

    public SearchHistoryBean parseHistoryBean() {

        SearchHistoryBean bean = new SearchHistoryBean();
        bean.setStockName(this.name);
        bean.setId(this.id);
        bean.setStockCode(this.code);
        bean.setSymbol(this.symbol);
        bean.setSymbol_type(this.symbol_type);
        bean.setSymbol_stype(this.symbol_stype + "");
        bean.setList_status(this.list_status);
        return bean;
    }


    public boolean equals(Object obj) {
        if (obj instanceof SelectStockBean) {
            SelectStockBean param = (SelectStockBean) obj;
            return this.id == param.id || this.code.equals(param.code);
        } else if (obj instanceof SelectStockFundViewBean) {
            QuotesBean quotesBean = ((SelectStockFundViewBean) obj).getmQuotesBean();
            return this.id == quotesBean.id || this.code == quotesBean.code;
        }
        return super.equals(obj);
    }

    public long getSortId() {
        return sortId;
    }

    public void setSortId(int index) {
        this.sortId = index;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getTotal_capital() {
        return total_capital;
    }

    public void setTotal_capital(float total_capital) {
        this.total_capital = total_capital;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public String getSymbol_type() {
        return symbol_type;
    }

    public void setSymbol_type(String symbol_type) {
        this.symbol_type = symbol_type;
    }

    public String getList_status() {
        return list_status;
    }

    public void setList_status(String list_status) {
        this.list_status = list_status;
    }


    @Override
    public String getItemDesc() {
        // TODO Auto-generated method stub
        return this.symbol;
    }

    @Override
    public String getItemId() {
        // TODO Auto-generated method stub
        return this.id + "";
    }

    @Override
    public String getItemName() {
        // TODO Auto-generated method stub
        return this.name;
    }

    @Override
    public long getItemSortId() {
        // TODO Auto-generated method stub
        return this.sortId;
    }

    @Override
    public boolean isItemTixing() {
//        if (this.alertSetBean != null) {
//            if (StockUitls.isFundType(symbol_type)) {
//                return this.alertSetBean.isFundNetvalueRemind();
//            } else {
//                this.alertSetBean.isFund7dayRemind();
//                this.alertSetBean.isFundNetvalueRemind();
//                this.alertSetBean.isNoticeRemind();
//                this.alertSetBean.isYanbaoRemind();
//                return this.alertSetBean.isNoticeRemind()||this.alertSetBean.isYanbaoRemind()||this.alertSetBean.isFund7dayRemind()||this.alertSetBean.isFundNetvalueRemind();
//            }
//        }
        return is_alert;
    }

    public boolean equals(SelectStockBean obj) {
        if (obj instanceof SelectStockBean) {
            SelectStockBean stock = (SelectStockBean) obj;
            return this.id == stock.id;
        }
        return super.equals(obj);
    }

}
