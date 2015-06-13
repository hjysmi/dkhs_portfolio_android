/**
 * @Title AddSearchItemAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-8 下午2:02:32
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.widget.CompoundButton;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockPriceBean;
import com.dkhs.portfolio.net.DataParse;
import com.dkhs.portfolio.net.ParseHttpListener;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.ui.widget.ChangeFollowView;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName AddSearchItemAdapter
 * @Description 搜索添加自选
 * @date 2014-10-8 下午2:02:32
 */
public class AddSearchItemAdapter extends SearchStockAdatper {


    private ChangeFollowView changeFollowView;

    /**
     * @param context
     * @param datas
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    public AddSearchItemAdapter(Context context, List<SelectStockBean> datas) {
        super(context, datas, false);
        init(context);
    }


    private void init(Context context) {
        changeFollowView = new ChangeFollowView(context);
        changeFollowView.setmChangeListener(changeSuccessListener);
    }

    private ChangeFollowView.IChangeSuccessListener changeSuccessListener = new ChangeFollowView.IChangeSuccessListener() {
        @Override
        public void onChange(SelectStockBean stockBean) {
            if (null != stockBean) {
//                if (!PortfolioApplication.hasUserLogin()) {// 如果当前是游客模式，添加自选股到本地数据库

                if (stockBean.isFollowed()) {
                    SelectAddOptionalActivity.mFollowList.add(stockBean);
                } else {
                    SelectAddOptionalActivity.mFollowList.remove(stockBean);
                }
//                }

//
//            } else {
//
//
////                if (stockBean.isFollowed()) {
////                    int index = mDataList.indexOf(stockBean);
////                    mDataList.get(index).isFollowed = true;
////                } else {
////                    int index = mDataList.indexOf(stockBean);
////                    mDataList.get(index).isFollowed = false;
////                }
//
//            }
            }
        }
    };


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        SelectStockBean csBean = (SelectStockBean) buttonView.getTag();


        if (!NetUtil.checkNetWork() && PortfolioApplication.hasUserLogin()) {
            buttonView.setChecked(!isChecked);
            PromptManager.showNoNetWork();
        } else if (changeFollowView != null) {
            changeFollowView.changeFollowNoDialog(csBean);
        }
    }

//        if (!PortfolioApplication.hasUserLogin()) {// 如果当前是游客模式，添加自选股到本地数据库
//            if (null != csBean) {
//                if (isChecked) {
//                    csBean.isFollowed = true;
//                    csBean.sortId = 0;
//                    mVisitorDataEngine.saveOptionalStock(csBean);
//                    SelectAddOptionalActivity.mFollowList.add(csBean);
//                    PromptManager.showFollowToast();
//
//                } else {
//                    mVisitorDataEngine.delOptionalStock(csBean);
//                    SelectAddOptionalActivity.mFollowList.remove(csBean);
//                    PromptManager.showDelFollowToast();
//                }
//            }
//
//        } else if (NetUtil.checkNetWork()) {
//            if (null != csBean) {
//
//                if (isChecked) {
//                    new QuotesEngineImpl().symbolfollow(csBean.id, followListener);
//                } else {
//                    new QuotesEngineImpl().delfollow(csBean.id, delFollowListener);
//                }
//            }
//        } else {
//            buttonView.setChecked(!isChecked);
//            PromptManager.showNoNetWork();
//        }
    // System.out.println("remove mSelectIdList lenght:" + BaseSelectActivity.mSelectList.size());


    ParseHttpListener delFollowListener = new ParseHttpListener<Object>() {

        @Override
        protected Object parseDateTask(String jsonData) {
            StockPriceBean stockBean = DataParse.parseObjectJson(StockPriceBean.class, jsonData);
            SelectStockBean selectBean = new SelectStockBean();
            selectBean.id = stockBean.getId();
            selectBean.name = stockBean.getAbbrname();
            selectBean.currentValue = stockBean.getCurrent();
            selectBean.code = stockBean.getCode();
            selectBean.symbol = stockBean.getSymbol();
            selectBean.percentage = stockBean.getPercentage();
            selectBean.percentage = stockBean.getPercentage();
            selectBean.change = stockBean.getChange();
            selectBean.isStop = stockBean.isStop();
            SelectAddOptionalActivity.mFollowList.remove(selectBean);
            return selectBean;
        }

        @Override
        protected void afterParseData(Object object) {
            notifyDataSetChanged();
            PromptManager.showDelFollowToast();

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
            notifyDataSetChanged();
            PromptManager.showFollowToast();

        }

    };

}
