/**
 * @Title StockDragAdapter.java
 * @Package com.dkhs.portfolio.ui.draglist
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-9 下午3:23:45
 * @version V1.0
 */
package com.dkhs.portfolio.ui.draglist;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.CompoundButton;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.DataEntry;
import com.dkhs.portfolio.engine.FollowComEngineImpl;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.ui.StockRemindActivity;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName StockDragAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015-4-9 下午3:23:45
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
        if (PortfolioApplication.hasUserLogin()) {
            mQuotesEngine.defFollowCombinations(conBean.getId(), baseListener);
            setStation(position);
        } else {
            new VisitorDataEngine().delCombinationBean(conBean);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    PromptManager.closeProgressDialog();
                    // conbinList.remove(conBean);
                    if (getDataList().size() == 1 && mDelCallBack != null) {
                        mDelCallBack.removeLast();
                    } else {
                        getDataList().remove(position);
                        notifyDataSetChanged();
                    }
                }
            }, 200);
        }

    }

    @Override
    public void onAlertClick(CompoundButton buttonView, int position, boolean isCheck) {
        if (UIUtils.iStartLoginActivity(context)) {// 如果当前是游客模式，无法设置提醒，需要跳转到登陆页
            return;
        }

        CombinationBean conBean = (CombinationBean) getDataList().get(position).elment;
        UIUtils.startAnimationActivity((Activity) context, StockRemindActivity.newCombinatIntent(context, conBean));

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
