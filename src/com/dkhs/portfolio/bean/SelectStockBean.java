/**
 * @Title SelectStockBean.java
 * @Package com.dkhs.portfolio.bean
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-18 下午2:42:46
 * @version V1.0
 */
package com.dkhs.portfolio.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.lidroid.xutils.db.annotation.Check;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Unique;

/**
 * @ClassName SelectStockBean
 * @Description 添加自选股，用于UI显示的Bean
 * @author zjz
 * @date 2014-9-18 下午2:42:46
 * @version 1.0
 */
public class SelectStockBean extends DragListItem implements Serializable {

    private static final long serialVersionUID = 12955478254888L;
    public String name;
    public float currentValue;
    public float percentage;
    @NoAutoIncrement
    public long id;
    public String code;
    public float change;
    public boolean isFollowed;
    public boolean isStop;
    public boolean is_alert;

    public long sortId;
    public int status;
    public float total_capital;

    // (0, '其他'),(1, '股票'),(2, '债券'),(3, '基金'),(4, '权证'),(5, '指数'),(6, '集合理财'),(9, '期货'),(10, '期权')
    public String symbol_type;
    // 2,='暂停交易' 3='终止上市'
    public String list_status;

    public DataEntry<SelectStockBean> entry = null;

    public SelectStockBean() {
        this.entry = new DataEntry<SelectStockBean>();
    }

    public DataEntry<SelectStockBean> getEntry() {
        return entry;
    }

    public void setEntry(DataEntry<SelectStockBean> entry) {
        this.entry = entry;
    }

    public static SelectStockBean copy(ConStockBean stockBean) {

        SelectStockBean bean = new SelectStockBean();
        bean.name = stockBean.getName();
        bean.currentValue = stockBean.getCurrentValue();
        // bean.percentage = stockBean.getPercent();
        bean.id = stockBean.getStockId();
        bean.code = stockBean.getStockCode();
        bean.isStop = stockBean.isStop();
        bean.list_status = stockBean.getList_status();
        return bean;
    }

    public static SelectStockBean copy(SearchStockBean stockBean) {

        SelectStockBean bean = new SelectStockBean();
        bean.name = stockBean.getStockName();
        // bean.currentValue = stockBean.getCurrentValue();
        // bean.percentage = stockBean.getPercent();
        bean.id = stockBean.getId();
        bean.code = stockBean.getStockCode();
        bean.symbol_type = stockBean.getSymbol_type();
        bean.isStop = stockBean.isStop();
        bean.list_status = stockBean.getList_status();
        return bean;
    }

    public static SelectStockBean copy(SearchFundsBean stockBean) {

        SelectStockBean bean = new SelectStockBean();
        bean.name = stockBean.getStockName();
        // bean.currentValue = stockBean.getCurrentValue();
        // bean.percentage = stockBean.getPercent();
        bean.id = stockBean.getId();
        bean.code = stockBean.getStockCode();
        return bean;
    }

    public static SelectStockBean copy(StockPriceBean stockBean) {

        SelectStockBean selectBean = new SelectStockBean();
        selectBean.id = stockBean.getId();
        selectBean.name = stockBean.getAbbrname();
        selectBean.currentValue = stockBean.getCurrent();
        selectBean.code = stockBean.getSymbol();
        selectBean.percentage = stockBean.getPercentage();
        selectBean.isFollowed = stockBean.isFollowed();
        selectBean.symbol_type = stockBean.getSymbol_type();
        selectBean.isStop = stockBean.isStop();
        selectBean.sortId = stockBean.getIndex();
        selectBean.change = stockBean.getChange();
        selectBean.list_status = stockBean.getList_status();
        selectBean.total_capital = stockBean.getTotal_capital();
        selectBean.is_alert = stockBean.isIs_alert();
        return selectBean;
    }

    public static SelectStockBean copy(StockQuotesBean stockBean) {

        SelectStockBean selectBean = new SelectStockBean();
        selectBean.id = stockBean.getId();
        selectBean.name = stockBean.getName();
        selectBean.currentValue = stockBean.getCurrent();
        selectBean.code = stockBean.getSymbol();
        selectBean.percentage = stockBean.getPercentage();
        selectBean.isFollowed = stockBean.isFollowed();
        selectBean.symbol_type = stockBean.getSymbol_type();
        // selectBean.isStop = stockBean.;
        // selectBean.sortId = stockBean.getIndex();
        selectBean.change = stockBean.getChange();
        // selectBean.list_status = stockBean.get;
        selectBean.total_capital = stockBean.getTotal_capital();
        return selectBean;
    }

    public ConStockBean parseStock() {

        ConStockBean bean = new ConStockBean();
        bean.setName(name);
        bean.setCurrentValue(currentValue);
        // bean.setPercent(percentage);
        bean.setStockId(id);
        bean.setIsStop(isStop ? 1 : 0);
        bean.setStockCode(code);
        bean.setList_status(list_status);
        return bean;
    }

    public boolean equals(Object obj) {
        SelectStockBean param = (SelectStockBean) obj;
        if (this.id == param.id || this.code.equals(param.code)) {
            // if (this.code.equals(param.code)) {
            return true;
        } else {
            return false;
        }
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String getItemDesc() {
        // TODO Auto-generated method stub
        return this.code;
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
        return this.is_alert;
    }
}
