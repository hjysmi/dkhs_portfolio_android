package com.dkhs.portfolio.ui.widget.ViewBean;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zjz on 2015/7/3.
 */
public class MarkStockViewBean extends ViewBean {
    private static final int TYPE = 3;

    public static final int SUB_TYPE_TURNOVER = 10;
    public static final int SUB_TYPE_AMPLITUDE = 20;

    public MarkStockViewBean() {
    }

    public MarkStockViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    private StockQuotesBean mStockQuotesBean;
    private int mSubViewType = -1;

    public MarkStockViewBean(StockQuotesBean stockQuotesBean, int subType) {
        this.mStockQuotesBean = stockQuotesBean;
        this.mSubViewType = subType;
    }

    public MarkStockViewBean(StockQuotesBean stockQuotesBean) {
        this.mStockQuotesBean = stockQuotesBean;

    }

    public void setStockQuotesBean(StockQuotesBean stockQuotesBean) {
        this.mStockQuotesBean = stockQuotesBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.item_market_stock_list));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ((ViewHolder) itemHolder).bindView(mStockQuotesBean, mSubViewType);
        ViewUitls.fullSpanView(itemHolder);
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        LinearLayout tvLayoutTitle;
        TextView tvStockName;
        TextView tvStockCode;
        TextView tvCurrent;
        TextView tvTextPercent;
        View divider;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            tvStockName = (TextView) itemView.findViewById(R.id.market_text_name);
            tvStockCode = (TextView) itemView.findViewById(R.id.market_text_name_num);
            tvCurrent = (TextView) itemView.findViewById(R.id.market_list_item_index);
            tvTextPercent = (TextView) itemView.findViewById(R.id.market_list_item_percent);
            divider = (View) itemView.findViewById(R.id.divider);
        }

        public void bindView(final StockQuotesBean mStockQuotesBean, int subType) {

            tvStockName.setText(mStockQuotesBean.getAbbrName());
            tvStockCode.setText(mStockQuotesBean.getSymbol());
            divider.setBackgroundColor(itemView.getResources().getColor(R.color.drivi_line));

            float change;
            if (subType == SUB_TYPE_TURNOVER) {
                change = mStockQuotesBean.getTurnover_rate();//换手率
            } else if (subType == SUB_TYPE_AMPLITUDE) {
                change = mStockQuotesBean.getAmplitude(); //振幅
            } else {
                change = mStockQuotesBean.getPercentage();//涨跌幅
            }
//            change = mStockQuotesBean.getTurnover_rate();  //换手率
            ColorStateList textCsl = null;
            if (subType > 0) {
                textCsl = ColorTemplate.getTextColor(R.color.theme_color);
            } else if (mStockQuotesBean.getIs_stop() == 1 || StockUitls.isDelistStock(mStockQuotesBean.getList_status() + "")) {
                textCsl = ColorTemplate.getTextColor(R.color.theme_color);
            } else {
                textCsl = ColorTemplate.getUpOrDrownCSL(change);
            }
            tvCurrent.setTextColor(textCsl);

            if (StockUitls.isShangZhengB(mStockQuotesBean.getSymbol())) {
                tvCurrent.setText(StringFromatUtils.get3Point(mStockQuotesBean.getCurrent()));
            } else {
                tvCurrent.setText(StringFromatUtils.get2Point(mStockQuotesBean.getCurrent()));
            }


            if (StockUitls.isDelistStock(mStockQuotesBean.getList_status() + "")) {
                tvTextPercent.setText(R.string.exit_stock);
                tvTextPercent.setTypeface(Typeface.DEFAULT);
                tvTextPercent.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));
            } else if (mStockQuotesBean.getIs_stop() == 1) {
                tvTextPercent.setText(R.string.stop_stock);
                tvTextPercent.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));
                tvTextPercent.setTypeface(Typeface.DEFAULT);
            } else {
                tvTextPercent.setTextColor(textCsl);
                tvTextPercent.setTypeface(Typeface.DEFAULT_BOLD);
                tvTextPercent.setText(StringFromatUtils.get2PointPercent(change));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.startAnimationActivity((Activity) itemView.getContext(), StockQuotesActivity.newIntent(itemView.getContext(), SelectStockBean.copy(mStockQuotesBean)));

                }
            });


        }


    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
