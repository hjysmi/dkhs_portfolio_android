package com.dkhs.portfolio.ui.widget.ViewBean;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.LinkedList;

/**
 * Created by zjz on 2015/7/3.
 */
public class MarkIndexViewPool {
    // in order to avoiding memory leak, this heap should be shrink after the root RecyclerView been idle.
    private LinkedList<View> mViewHeap = new LinkedList<View>();
    public int STAT_GAME_VIDEO_ITEM_COUNT;

    public synchronized View getView(GridLayout gridVideo) {
        View view = mViewHeap.poll();
        if (view == null) {
            view = LayoutInflater.from(gridVideo.getContext()).inflate(R.layout.item_mark_center, gridVideo, false);
            Log.e("MarkIndexViewPool", String.format("Video item createViewCount : %d", ++STAT_GAME_VIDEO_ITEM_COUNT));
            view.setTag(new VideoHolder(view));
        }
        return view;
    }

    public synchronized void recycleView(View view) {
        mViewHeap.add(view);
    }

    public int size() {
        return mViewHeap.size();
    }

    public class VideoHolder {
        public TextView tvStockName;
        public TextView tvTitleName;
        public TextView tvCurrentValue;
        public TextView tvIncrease;
        public TextView tvPercent;
//        private boolean isPlate;

        public VideoHolder(View itemView) {
//            itemView
            Log.e("MarkIndexViewPool", String.format("itemview width:%d,itemview height:%d", itemView.getLayoutParams().width, itemView.getLayoutParams().height));
            tvStockName = (TextView) itemView.findViewById(R.id.tv_stock_name);
            tvTitleName = (TextView) itemView.findViewById(R.id.tv_title_name);
            tvCurrentValue = (TextView) itemView.findViewById(R.id.tv_main_value);
            tvIncrease = (TextView) itemView.findViewById(R.id.tv_incease_value);
            tvPercent = (TextView) itemView.findViewById(R.id.tv_incease_ratio);
            itemView.getLayoutParams().width = UIUtils.getDisplayMetrics().widthPixels / 3;
            itemView.getLayoutParams().height = (int)(itemView.getLayoutParams().width * 0.8f);
        }

        public void bindView(StockQuotesBean item) {

            tvStockName.setVisibility(View.GONE);


            float change = item.getPercentage();
            tvCurrentValue.setTextColor(ColorTemplate.getUpOrDrownCSL(change));
            if (change > 0) {
                tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(
                        PortfolioApplication.getInstance().getResources().getDrawable(R.drawable.ic_grow_up), null, null, null);
            } else if (change < 0) {
                tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(
                        PortfolioApplication.getInstance().getResources().getDrawable(R.drawable.ic_grow_down), null, null, null);
            } else {
                tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            tvTitleName.setText(item.getAbbrName());
            tvCurrentValue.setText(StringFromatUtils.get2Point(item.getCurrent()));
            tvPercent.setText(StringFromatUtils.get2PointPercentPlus(item.getPercentage()));
            tvIncrease.setText(StringFromatUtils.get2PointPlus(item.getChange()));
        }

        public void bindView(SectorBean item) {
            tvStockName.setVisibility(View.VISIBLE);
            float change = item.getPercentage();
            tvCurrentValue.setTextColor(ColorTemplate.getUpOrDrownCSL(change));
            tvStockName.setText(item.getTop_symbol_name());
            tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            tvTitleName.setText(item.getAbbr_name());
            tvTitleName.setVisibility(View.VISIBLE);
            tvCurrentValue.setText(StringFromatUtils.get2PointPercentPlus(item.getPercentage()));
            tvPercent.setText(StringFromatUtils.get2PointPercentPlus(item.getTop_symbol_percentage()));
            tvIncrease.setText(StringFromatUtils.get2Point(item.getTop_symbol_current()));

        }
    }
}
