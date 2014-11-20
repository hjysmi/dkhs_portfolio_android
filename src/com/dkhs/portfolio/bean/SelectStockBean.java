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

/**
 * @ClassName SelectStockBean
 * @Description 添加自选股，用于UI显示的Bean
 * @author zjz
 * @date 2014-9-18 下午2:42:46
 * @version 1.0
 */
public class SelectStockBean implements Serializable {
    private static final long serialVersionUID = 12955478254888L;
    public String name;
    public float currentValue;
    public float percentage;
    public long id;
    public String code;
    public float change;
    public boolean isFollowed;
    public boolean isStop;
    public long index;
    // (0, '其他'),(1, '股票'),(2, '债券'),(3, '基金'),(4, '权证'),(5, '指数'),(6, '集合理财'),(9, '期货'),(10, '期权')
    public String symbol_type;

    public static SelectStockBean copy(ConStockBean stockBean) {

        SelectStockBean bean = new SelectStockBean();
        bean.name = stockBean.getName();
        bean.currentValue = stockBean.getCurrentValue();
        // bean.percentage = stockBean.getPercent();
        bean.id = stockBean.getStockId();
        bean.code = stockBean.getStockCode();
        bean.isStop = stockBean.isStop();
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
        selectBean.index = stockBean.getIndex();
        return selectBean;
    }

    public ConStockBean parseStock() {

        ConStockBean bean = new ConStockBean();
        bean.setName(name);
        bean.setCurrentValue(currentValue);
        // bean.setPercent(percentage);
        bean.setStockId(id);
        bean.setStockCode(code);
        return bean;
    }

    public boolean equals(Object obj) {
        SelectStockBean param = (SelectStockBean) obj;
        if (this.id == param.id) {
            return true;
        } else {
            return false;
        }
    }

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}
    
}
