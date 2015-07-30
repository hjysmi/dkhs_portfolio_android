package com.dkhs.portfolio.ui.widget.ViewBean;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.ui.MarketListActivity;
import com.dkhs.portfolio.utils.AnimationHelper;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zjz on 2015/7/3.
 */
public class MarkPlateGridViewBean extends ViewBean {
    private static final int TYPE = 21;

    private SectorBean mMarkSectorBean;

    public MarkPlateGridViewBean() {
    }


    public MarkPlateGridViewBean(SectorBean markSectorBean) {
        this.mMarkSectorBean = markSectorBean;
    }

    public void setPlateBean(SectorBean markSectorBean) {
        this.mMarkSectorBean = markSectorBean;
    }

    public MarkPlateGridViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {

        return new ViewHolder(inflate(container, R.layout.item_mark_center), mViewPool);

    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        public TextView tvStockName;
        public TextView tvTitleName;
        public TextView tvCurrentValue;
        public TextView tvIncrease;
        public TextView tvPercent;

        public ViewHolder(final View itemView, MarkIndexViewPool mViewPool) {
            super(itemView);
            this.itemView = itemView;

            tvStockName = (TextView) itemView.findViewById(R.id.tv_stock_name);
            tvTitleName = (TextView) itemView.findViewById(R.id.tv_title_name);
            tvCurrentValue = (TextView) itemView.findViewById(R.id.tv_main_value);
            tvIncrease = (TextView) itemView.findViewById(R.id.tv_incease_value);
            tvPercent = (TextView) itemView.findViewById(R.id.tv_incease_ratio);
            AnimationHelper.rotate90Animation(itemView);
        }

        public void bindView(final SectorBean item) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.startAnimationActivity((Activity) itemView.getContext(), MarketListActivity.newIntent(itemView.getContext(), MarketListActivity.LoadViewType.PlateList, item.getId(),
                            item.getAbbr_name()));
                }
            });
            tvStockName.setVisibility(View.VISIBLE);
            float change = item.getPercentage();
            tvCurrentValue.setTextColor(ColorTemplate.getUpOrDrownCSL(change));
            tvStockName.setText(item.getTop_symbol_name());
            tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            tvTitleName.setText(item.getAbbr_name());
            tvCurrentValue.setText(StringFromatUtils.get2PointPercentPlus(item.getPercentage()));
            tvPercent.setText(StringFromatUtils.get2PointPercentPlus(item.getTop_symbol_percentage()));
            tvIncrease.setText(StringFromatUtils.get2Point(item.getTop_symbol_current()));


        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {

        int position = itemHolder.getAdapterPosition();
        Log.d("MarkPlateGridViewBean", "getAdapterPosition:" + position);
        //position =6 and 9,no right left padding
        //position > 7 no top padding
        ((ViewHolder) itemHolder).bindView(mMarkSectorBean);

    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
