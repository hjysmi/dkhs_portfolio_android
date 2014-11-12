/**
 * @Title AddSearchItemAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-8 下午2:02:32
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.CompoundButton;

import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.BasicHttpListener;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;

/**
 * @ClassName AddSearchItemAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-8 下午2:02:32
 * @version 1.0
 */
public class AddSearchItemAdapter extends SearchStockAdatper {

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param context
     * @param datas
     */
    public AddSearchItemAdapter(Context context, List<SelectStockBean> datas) {
        super(context, datas, false);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        SelectStockBean csBean = (SelectStockBean) buttonView.getTag();
        if (null != csBean) {

            if (isChecked) {
                new QuotesEngineImpl().symbolfollow(csBean.id, followListener);
            } else {
                new QuotesEngineImpl().delfollow(csBean.id, delFollowListener);
                System.out.println("remove optional :" + csBean.name + " id:" + csBean.id);
            }
        }

        // System.out.println("remove mSelectIdList lenght:" + BaseSelectActivity.mSelectList.size());

    }

    ParseHttpListener delFollowListener = new ParseHttpListener<Object>() {

        @Override
        protected Object parseDateTask(String jsonData) {
            StockPriceBean stockBean = DataParse.parseObjectJson(StockPriceBean.class, jsonData);
            SelectStockBean selectBean = new SelectStockBean();
            selectBean.id = stockBean.getId();
            selectBean.name = stockBean.getAbbrname();
            selectBean.currentValue = stockBean.getCurrent();
            selectBean.code = stockBean.getSymbol();
            selectBean.percentage = stockBean.getPercentage();
            selectBean.percentage = stockBean.getPercentage();
            selectBean.change = stockBean.getChange();
            selectBean.isStop = stockBean.isStop();
            SelectAddOptionalActivity.mFollowList.remove(selectBean);
            return selectBean;
        }

        @Override
        protected void afterParseData(Object object) {
            // TODO Auto-generated method stub

        }

    };
    ParseHttpListener followListener = new ParseHttpListener<Object>() {

        @Override
        protected Object parseDateTask(String jsonData) {
            List<StockPriceBean> stockBeanList = DataParse.parseArrayJson(StockPriceBean.class, jsonData);
            for (StockPriceBean stockBean : stockBeanList) {

                SelectStockBean selectBean = new SelectStockBean();
                selectBean.id = stockBean.getId();
                selectBean.name = stockBean.getAbbrname();
                selectBean.currentValue = stockBean.getCurrent();
                selectBean.code = stockBean.getSymbol();
                selectBean.percentage = stockBean.getPercentage();
                selectBean.percentage = stockBean.getPercentage();
                selectBean.change = stockBean.getChange();
                selectBean.isStop = stockBean.isStop();
                SelectAddOptionalActivity.mFollowList.add(selectBean);
            }
            return stockBeanList;
        }

        @Override
        protected void afterParseData(Object object) {
            // TODO Auto-generated method stub

        }

    };

}
