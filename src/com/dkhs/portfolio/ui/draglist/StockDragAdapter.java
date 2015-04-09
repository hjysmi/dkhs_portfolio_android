/**
 * @Title StockDragAdapter.java
 * @Package com.dkhs.portfolio.ui.draglist
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-4-9 下午3:23:45
 * @version V1.0
 */
package com.dkhs.portfolio.ui.draglist;

import java.util.List;

import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.utils.PromptManager;

import android.content.Context;
import android.os.Handler;

/**
 * @ClassName StockDragAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-4-9 下午3:23:45
 * @version 1.0
 */
public class StockDragAdapter extends DragListAdapter {

    public StockDragAdapter(Context context, List<DragListItem> dataList, DragListView mDragListView) {
        super(context, dataList, mDragListView);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onDeleteClick(final int position) {
        PromptManager.showProgressDialog(context, null);
        if (PortfolioApplication.getInstance().hasUserLogin()) {

            mQuotesEngine.delfollow(dataList.get(position).id, baseListener);
            // station = position;
            setStation(position);
        } else {
            new VisitorDataEngine().delOptionalStock(dataList.get(position));
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    PromptManager.closeProgressDialog();
                    dataList.remove(dataList.get(position));
                    notifyDataSetChanged();
                }
            }, 200);
        }

    }

}
