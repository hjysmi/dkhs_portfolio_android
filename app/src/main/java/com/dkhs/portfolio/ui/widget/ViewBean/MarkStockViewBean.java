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

    public MarkStockViewBean() {
    }

    public MarkStockViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    private StockQuotesBean mStockQuotesBean;
    private boolean isDefColor = false;

    public MarkStockViewBean(StockQuotesBean stockQuotesBean, boolean isDefColor) {
        this.mStockQuotesBean = stockQuotesBean;
        this.isDefColor = isDefColor;
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
        ((ViewHolder) itemHolder).bindView(mStockQuotesBean, isDefColor);
        ViewUitls.fullSpanView(itemHolder);
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        LinearLayout tvLayoutTitle;
        TextView tvTextName;
        TextView tvTextNameNum;
        TextView tvTextItemIndex;
        TextView tvTextPercent;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            tvTextName = (TextView) itemView.findViewById(R.id.market_text_name);
            tvTextNameNum = (TextView) itemView.findViewById(R.id.market_text_name_num);
            tvTextItemIndex = (TextView) itemView.findViewById(R.id.market_list_item_index);
            tvTextPercent = (TextView) itemView.findViewById(R.id.market_list_item_percent);
//            tvLayoutTitle = (LinearLayout) itemView.findViewById(R.id.market_layout_item);

        }

        public void bindView(final StockQuotesBean mStockQuotesBean, boolean isDefColor) {

            tvTextName.setText(mStockQuotesBean.getAbbrName());
//            Log.e("MarkStockViewBean","StockQuotesBean.name:"+ mStockQuotesBean.name);
            tvTextNameNum.setText(mStockQuotesBean.getSymbol());

            float change = mStockQuotesBean.getPercentage();
            ColorStateList textCsl = null;
            if (isDefColor) {
                textCsl = ColorTemplate.getTextColor(R.color.theme_color);
            } else if (mStockQuotesBean.getIs_stop() == 1 || StockUitls.isDelistStock(mStockQuotesBean.getList_status() + "")) {
                textCsl = ColorTemplate.getTextColor(R.color.theme_color);
            } else {
                textCsl = ColorTemplate.getUpOrDrownCSL(change);
            }
            tvTextItemIndex.setTextColor(textCsl);
            if (StockUitls.isShangZhengB(mStockQuotesBean.getSymbol())) {
                tvTextItemIndex.setText(StringFromatUtils.get3Point(mStockQuotesBean.getCurrent()));
            } else {
                tvTextItemIndex.setText(StringFromatUtils.get2Point(mStockQuotesBean.getCurrent()));
            }
            //  tvTextPercent.setText(StringFromatUtils.get2PointPercent(change));

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
