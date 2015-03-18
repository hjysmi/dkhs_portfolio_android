/**
 * @Title AddStockItemAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-29 下午1:33:35
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.CompoundButton;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;

/**
 * @ClassName AddStockItemAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-29 下午1:33:35
 * @version 1.0
 */
public class AddStockItemAdapter extends SelectStockAdatper {

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param context
     * @param datas
     */
    public AddStockItemAdapter(Context context, List<SelectStockBean> datas) {
        super(context, datas);
        setAddNewStock(true);

    }

    public AddStockItemAdapter(Context context, List<SelectStockBean> datas, boolean isDefColor) {
        super(context, datas, isDefColor);
        setAddNewStock(true);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        SelectStockBean csBean = (SelectStockBean) buttonView.getTag();

        if (!PortfolioApplication.hasUserLogin()) {// 如果当前是游客模式，添加自选股到本地数据库
            if (null != csBean) {
                if (isChecked) {
                    csBean.isFollowed = true;
                    csBean.sortId = 0;
                    mVisitorDataEngine.saveOptionalStock(csBean);
                } else {
                    mVisitorDataEngine.delOptionalStock(csBean);
                }
            }

        } else if (NetUtil.checkNetWork()) {// 如果当前有网络，添加到自选股

            if (null != csBean) {

                if (isChecked) {
                    new QuotesEngineImpl().symbolfollow(csBean.id, followListener);
                } else {
                    new QuotesEngineImpl().delfollow(csBean.id, delFollowListener);
                }
            }

        } else {
            buttonView.setChecked(!isChecked);
            PromptManager.showNoNetWork();
        }

    }

    ParseHttpListener delFollowListener = new ParseHttpListener<SelectStockBean>() {

        @Override
        protected SelectStockBean parseDateTask(String jsonData) {
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
            // SelectAddOptionalActivity.mFollowList.remove(selectBean);
            return selectBean;
        }

        @Override
        protected void afterParseData(SelectStockBean object) {
            // mDataList.remove(location)
            int index = mDataList.indexOf(object);
            mDataList.get(index).isFollowed = false;

        }

    };
    ParseHttpListener followListener = new ParseHttpListener<SelectStockBean>() {

        @Override
        protected SelectStockBean parseDateTask(String jsonData) {
            List<StockPriceBean> stockBeanList = DataParse.parseArrayJson(StockPriceBean.class, jsonData);
            SelectStockBean selectBean = new SelectStockBean();
            for (StockPriceBean stockBean : stockBeanList) {

                selectBean.id = stockBean.getId();
                selectBean.name = stockBean.getAbbrname();
                selectBean.currentValue = stockBean.getCurrent();
                selectBean.code = stockBean.getSymbol();
                selectBean.percentage = stockBean.getPercentage();
                selectBean.percentage = stockBean.getPercentage();
                selectBean.change = stockBean.getChange();
                selectBean.isStop = stockBean.isStop();
                // SelectAddOptionalActivity.mFollowList.add(selectBean);
            }
            return selectBean;
        }

        @Override
        protected void afterParseData(SelectStockBean object) {
            try {
                int index = mDataList.indexOf(object);
                mDataList.get(index).isFollowed = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    };

}
