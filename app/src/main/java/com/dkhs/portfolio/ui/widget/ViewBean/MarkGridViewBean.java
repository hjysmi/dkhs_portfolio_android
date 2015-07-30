package com.dkhs.portfolio.ui.widget.ViewBean;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.utils.AnimationHelper;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zjz on 2015/7/3.
 */
public class MarkGridViewBean extends ViewBean {
    private static final int TYPE = 2;

    private StockQuotesBean mMarkStockBean;


    public MarkGridViewBean() {
    }

    public MarkGridViewBean(StockQuotesBean markStockBean) {
        this.mMarkStockBean = markStockBean;
    }


    public void setStockQuotes(StockQuotesBean stockBean) {
        this.mMarkStockBean = stockBean;
    }

    public MarkGridViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {

        return new ViewHolder(inflate(container, R.layout.item_mark_center));
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private View contentView;
        public ImageView mImageView;
        public TextView tvStockName;
        public TextView tvTitleName;
        public TextView tvCurrentValue;
        public TextView tvIncrease;
        public TextView tvPercent;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            contentView = itemView.findViewById(R.id.item_content_view);
            tvStockName = (TextView) itemView.findViewById(R.id.tv_stock_name);
            tvTitleName = (TextView) itemView.findViewById(R.id.tv_title_name);
            tvCurrentValue = (TextView) itemView.findViewById(R.id.tv_main_value);
            tvIncrease = (TextView) itemView.findViewById(R.id.tv_incease_value);
            tvPercent = (TextView) itemView.findViewById(R.id.tv_incease_ratio);
            AnimationHelper.rotate90Animation(itemView);
        }


        public void bindView(final StockQuotesBean markStockBean, int position) {

            int defPadding = itemView.getResources().getDimensionPixelOffset(R.dimen.widget_margin_small);
//            if (position == 2) {
//                contentView.setPadding(0, defPadding, 0, defPadding);
//            } else {
//                contentView.setPadding(defPadding, defPadding, defPadding, defPadding);
//            }
            contentView.setPadding(defPadding, defPadding, defPadding, defPadding);

            tvStockName.setVisibility(View.GONE);


            SelectStockBean item = SelectStockBean.copy(markStockBean);

            float change = item.percentage;
            tvCurrentValue.setTextColor(ColorTemplate.getUpOrDrownCSL(change));
            if (change > 0) {
                tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(
                        itemView.getResources().getDrawable(R.drawable.ic_grow_up), null, null, null);
            } else if (change < 0) {
                tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(
                        itemView.getResources().getDrawable(R.drawable.ic_grow_down), null, null, null);
            } else {
                tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            tvTitleName.setText(item.name);
            tvCurrentValue.setText(StringFromatUtils.get2Point(item.currentValue));
            tvPercent.setText(StringFromatUtils.get2PointPercentPlus(item.percentage));
            tvIncrease.setText(StringFromatUtils.get2PointPlus(item.change));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.startAnimationActivity((Activity) itemView.getContext(), StockQuotesActivity.newIntent(itemView.getContext(), SelectStockBean.copy(markStockBean)));

                }
            });


        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        int position = itemHolder.getAdapterPosition();
        Log.d("MarkGridViewBean", "getAdapterPosition:" + position);
        //position ==2 no left and right padding
        ((ViewHolder) itemHolder).bindView(mMarkStockBean, position);


    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
