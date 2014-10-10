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

    public static SelectStockBean copy(ConStockBean stockBean) {

        SelectStockBean bean = new SelectStockBean();
        bean.name = stockBean.getName();
        bean.currentValue = stockBean.getCurrentValue();
        // bean.percentage = stockBean.getPercent();
        bean.id = stockBean.getStockId();
        bean.code = stockBean.getStockCode();
        return bean;
    }

    public static SelectStockBean copy(SearchStockBean stockBean) {

        SelectStockBean bean = new SelectStockBean();
        bean.name = stockBean.getStockName();
        // bean.currentValue = stockBean.getCurrentValue();
        // bean.percentage = stockBean.getPercent();
        bean.id = stockBean.getId();
        bean.code = stockBean.getStockCode();
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

}
