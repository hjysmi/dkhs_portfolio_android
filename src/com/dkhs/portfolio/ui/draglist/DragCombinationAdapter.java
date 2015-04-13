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
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.DataEntry;
import com.dkhs.portfolio.bean.DragListItem;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.QuotesEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;

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
public class DragCombinationAdapter extends DragListAdapter {

    private FollowComEngineImpl mQuotesEngine;

    public DragCombinationAdapter(Context context, DragListView mDragListView) {
        super(context, mDragListView);
        mQuotesEngine = new FollowComEngineImpl();

    }

    public void setAdapterData(List<CombinationBean> stockList) {
        parseToDragItem(stockList);
        notifyDataSetChanged();
        // this.conbinList = stockList;

    }

    // private List<CombinationBean> conbinList = new ArrayList<CombinationBean>();

    // private List<DataEntry> dataList = new ArrayList<DataEntry>();

    private void parseToDragItem(List<CombinationBean> stockList) {
        List<DataEntry> entryList = new ArrayList<DataEntry>();
        for (CombinationBean stockBean : stockList) {
            DataEntry<CombinationBean> dataBean = new DataEntry<CombinationBean>();
            dataBean.elment = stockBean;
            entryList.add(dataBean);
        }
        setDataList(entryList);
    }

    @Override
    public void onDeleteClick(final int position) {
        PromptManager.showProgressDialog(context, null);
        CombinationBean conBean = (CombinationBean) getDataList().get(position).elment;
        if (PortfolioApplication.getInstance().hasUserLogin()) {
            mQuotesEngine.defFollowCombinations(conBean.getId(), baseListener);
            setStation(position);
        } else {
            new VisitorDataEngine().delCombinationBean(conBean);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    PromptManager.closeProgressDialog();
                    // conbinList.remove(conBean);
                    getDataList().remove(position);
                    notifyDataSetChanged();
                }
            }, 200);
        }

    }

    @Override
    public void onAlertClick(CompoundButton buttonView, int position, boolean isCheck) {
        if (UIUtils.iStartLoginActivity(context)) {// 如果当前是游客模式，无法设置提醒，需要跳转到登陆页
            buttonView.setChecked(!isCheck);
            return;
        }

        if (isCheck) {
            PromptManager.showToast("添加提醒");
        } else {
            PromptManager.showToast("取消提醒");

        }

    }

    public List<CombinationBean> getConList() {
        // return stockList;
        List<CombinationBean> stockList = new ArrayList<CombinationBean>();
        for (DataEntry entry : getList()) {
            stockList.add((CombinationBean) entry.elment);
        }
        return stockList;
    }

}
