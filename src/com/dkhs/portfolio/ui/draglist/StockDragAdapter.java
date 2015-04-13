/**
 * @Title StockDragAdapter.java
 * @Package com.dkhs.portfolio.ui.draglist
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-9 下午3:23:45
 * @version V1.0
 */
package com.dkhs.portfolio.ui.draglist;

import java.util.ArrayList;
import java.util.List;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.DataEntry;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.utils.PromptManager;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @ClassName StockDragAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-9 下午3:23:45
 * @version 1.0
 */
public class StockDragAdapter extends DragListAdapter {

    private QuotesEngineImpl mQuotesEngine;

    public StockDragAdapter(Context context, DragListView mDragListView) {
        super(context, mDragListView);
        mQuotesEngine = new QuotesEngineImpl();

    }

    public void setAdapterData(List<SelectStockBean> stockList) {
        parseToDragItem(stockList);
        notifyDataSetChanged();
        this.stockList = stockList;
        setDataList(dataList);

    }

    private List<SelectStockBean> stockList = new ArrayList<SelectStockBean>();

    // private List<DragListItem> dataList = new ArrayList<DragListItem>();
    // public StockDragAdapter(Context context, List<DragListItem> stockList, DragListView mDragListView) {
    // // parseToDragItem(stockList);
    // // super(context, dataList, mDragListView);
    // }
    private List<DataEntry> dataList = new ArrayList<DataEntry>();

    private void parseToDragItem(List<SelectStockBean> stockList) {
        for (SelectStockBean stockBean : stockList) {
            DataEntry<SelectStockBean> dataBean = new DataEntry<SelectStockBean>();
            dataBean.elment = stockBean;
            dataList.add(dataBean);
        }
    }

    //
    // for (SelectStockBean stockBean : stockList) {
    // DragListItem dragItem = new DragListItem();
    // dragItem.setAlert(stockBean.is_alert);
    // dragItem.setDesc(stockBean.code);
    // dragItem.setId(stockBean.id + "");
    // dragItem.setName(stockBean.name);
    // dragItem.setSortId(stockBean.sortId);
    // getDataList().add(dragItem);
    // }
    // }

    // public StockDragAdapter(Context context, List<DragListItem> dataList, DragListView mDragListView) {
    // super(context, dataList, mDragListView);
    // }

    @Override
    public void onDeleteClick(final int position) {
        PromptManager.showProgressDialog(context, null);
        SelectStockBean stockBean = stockList.get(position);
        if (PortfolioApplication.getInstance().hasUserLogin()) {
            mQuotesEngine.delfollow(stockBean.id, baseListener);
            // station = position;
            setStation(position);
        } else {
            new VisitorDataEngine().delOptionalStock(stockList.get(position));
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    PromptManager.closeProgressDialog();
                    stockList.remove(stockList.get(position));
                    notifyDataSetChanged();
                }
            }, 200);
        }

    }

    @Override
    public void onAlertClick(int position, boolean isCheck) {
        if (isCheck) {
            PromptManager.showToast("添加提醒");
        } else {
            PromptManager.showToast("取消提醒");

        }

    }

    public List<SelectStockBean> getStockList() {
        // return stockList;
        List<SelectStockBean> stockList = new ArrayList<SelectStockBean>();
        for (DataEntry entry : getList()) {
            stockList.add((SelectStockBean) entry.elment);
        }
        return stockList;
    }

    @Override
    public void setViewDate(int position, TextView tvName, TextView tvDesc, CheckBox cbTixing) {
        DataEntry entry = getList().get(position);
        SelectStockBean item = (SelectStockBean) entry.elment;
        System.out.println("setViewDate position:" + position + " name:" + item.getName());

        tvName.setText(item.getName());
        tvDesc.setText(item.getItemDesc());
        cbTixing.setOnCheckedChangeListener(null);
        cbTixing.setTag(position);
        if (item.isItemTixing()) {
            cbTixing.setChecked(true);
        } else {
            cbTixing.setChecked(false);
        }
        cbTixing.setOnCheckedChangeListener(this);

    }
}
