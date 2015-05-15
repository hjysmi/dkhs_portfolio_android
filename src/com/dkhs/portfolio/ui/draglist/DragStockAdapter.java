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
import com.dkhs.portfolio.bean.DragListItem;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.ui.StockRemindActivity;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * @ClassName StockDragAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-9 下午3:23:45
 * @version 1.0
 */
public class DragStockAdapter extends DragListAdapter {

    private QuotesEngineImpl mQuotesEngine;

    public DragStockAdapter(Context context, DragListView mDragListView) {
        super(context, mDragListView);
        mQuotesEngine = new QuotesEngineImpl();

    }

    public void setAdapterData(List<SelectStockBean> stockList) {

        parseToDragItem(stockList);
        notifyDataSetChanged();

    }

    // private List<SelectStockBean> stockList = new ArrayList<SelectStockBean>();

    private void parseToDragItem(List<SelectStockBean> stockList) {
        List<DataEntry> dataList = new ArrayList<DataEntry>();
        for (SelectStockBean stockBean : stockList) {
            DataEntry<SelectStockBean> dataBean = new DataEntry<SelectStockBean>();
            dataBean.elment = stockBean;
            dataList.add(dataBean);
        }
        setDataList(dataList);
    }

    @Override
    public void onDeleteClick(final int position) {
        PromptManager.showProgressDialog(context, null);
        SelectStockBean stockBean = (SelectStockBean) getDataList().get(position).elment;
        if (PortfolioApplication.getInstance().hasUserLogin()) {
            mQuotesEngine.delfollow(stockBean.id, baseListener);
            setStation(position);
        } else {
            new VisitorDataEngine().delOptionalStock(stockBean);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    PromptManager.closeProgressDialog();
                    getDataList().remove(position);
                    notifyDataSetChanged();
                }
            }, 200);
        }

    }

    @Override
    public void onAlertClick(CompoundButton buttonView, int position, boolean isCheck) {

        if (UIUtils.iStartLoginActivity(context)) {// 如果当前是游客模式，无法设置提醒，需要跳转到登陆页
            return;
        }

        SelectStockBean stockBean = (SelectStockBean) getDataList().get(position).elment;
        // if (isCheck) {
        // PromptManager.showToast("添加提醒");
        // } else {
        // PromptManager.showToast("取消提醒");
        //
        // }
        startRemindActivity(stockBean);
    }

    private void startRemindActivity(SelectStockBean stockBean) {
        UIUtils.startAminationActivity((Activity) context, StockRemindActivity.newStockIntent(context, stockBean));
    }

    public List<SelectStockBean> getStockList() {
        List<SelectStockBean> stockList = new ArrayList<SelectStockBean>();
        for (DataEntry entry : getList()) {
            stockList.add((SelectStockBean) entry.elment);
        }
        return stockList;
    }

}
