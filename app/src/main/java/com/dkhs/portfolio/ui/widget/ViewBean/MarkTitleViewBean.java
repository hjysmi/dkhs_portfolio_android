package com.dkhs.portfolio.ui.widget.ViewBean;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.MarketListActivity;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zjz on 2015/7/3.
 */
public class MarkTitleViewBean extends ViewBean {
    private static final int TYPE = 1;

    public MarkTitleViewBean() {
    }

    public MarkTitleViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    private int titleResId;

    public MarkTitleViewBean(int titleText) {
        this.titleResId = titleText;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.layout_market_title));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ((ViewHolder) itemHolder).bindView(titleResId);
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView tvTitle;
        private ImageButton btnMore;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_mainindex);
            btnMore = (ImageButton) itemView.findViewById(R.id.btn_more_index);

        }

        public void bindView(final int titleResId) {

            tvTitle.setText(titleResId);
            btnMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    switch (titleResId){
                        case R.string.market_title_index: {
                            intent = MarketListActivity.newIntent(itemView.getContext(), MarketListActivity.LoadViewType.IndexUp);
                        }
                        break;
                        case R.string.market_title_hot: {
                            intent = MarketListActivity.newIntent(itemView.getContext(), MarketListActivity.LoadViewType.PlateHot);
                        }
                        break;
                        case R.string.market_title_up: {
                            intent = MarketListActivity.newIntent(itemView.getContext(), MarketListActivity.LoadViewType.StockIncease);
                        }
                        break;
                        case R.string.market_title_down: {
                            intent = MarketListActivity.newIntent(itemView.getContext(), MarketListActivity.LoadViewType.StockDown);
                        }
                        break;
                        case R.string.market_title_turnover: {
                            intent = MarketListActivity.newIntent(itemView.getContext(), MarketListActivity.LoadViewType.StockTurnOver);
                        }
                        break;
                        case R.string.market_title_ampli: {
                            intent = MarketListActivity.newIntent(itemView.getContext(), MarketListActivity.LoadViewType.StockAmplit);
                        }
                        break;
                    }
                    if (null != itemView.getContext()) {
                        UIUtils.startAnimationActivity((Activity)itemView.getContext(), intent);
                    }
                }
            });




        }


    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
