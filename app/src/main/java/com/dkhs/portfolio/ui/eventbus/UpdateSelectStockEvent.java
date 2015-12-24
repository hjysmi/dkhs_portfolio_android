package com.dkhs.portfolio.ui.eventbus;

import com.dkhs.portfolio.bean.SelectStockBean;

/**
 * Created by wuyongsen on 2015/12/24.
 */
public class UpdateSelectStockEvent {
    public static final int ADD_TYPE = 0;
    public static final int DEL_TYPE = 1;
    public SelectStockBean selectStockBean;
    /**
     * 0,添加 1,删除
     */
    public int type;
    public UpdateSelectStockEvent(SelectStockBean bean,int type){
        selectStockBean = bean;
        this.type = type;
    }
}
