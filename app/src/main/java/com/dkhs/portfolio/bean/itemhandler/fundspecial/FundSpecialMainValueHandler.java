package com.dkhs.portfolio.bean.itemhandler.fundspecial;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.ui.MainActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by zcm on 2025/22/08.
 */
public class FundSpecialMainValueHandler extends SimpleItemHandler<List<StockQuotesBean>> {
    private Context mContext;

    public FundSpecialMainValueHandler(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.market_fund_main_value;
    }

    @Override
    public void onBindView(ViewHolder vh, final List<StockQuotesBean> lists, int position) {
        if(lists == null || lists.size() == 0)
            return;
        SelectStockBean item1 = SelectStockBean.copy(lists.get(0));
        final View itemView1 = vh.get(R.id.item_content_view1);
        float change1 = item1.percentage;
        vh.getTextView(R.id.tv_main_value1).setTextColor(ColorTemplate.getUpOrDrownCSL(change1));
        if (change1 > 0) {
            vh.getTextView(R.id.tv_main_value1).setCompoundDrawablesWithIntrinsicBounds(
                    itemView1.getResources().getDrawable(R.drawable.ic_grow_up), null, null, null);
        } else if (change1 < 0) {
            vh.getTextView(R.id.tv_main_value1).setCompoundDrawablesWithIntrinsicBounds(
                    itemView1.getResources().getDrawable(R.drawable.ic_grow_down), null, null, null);
        } else {
            vh.getTextView(R.id.tv_main_value1).setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        vh.getTextView(R.id.tv_title_name1).setText(item1.name);
        vh.getTextView(R.id.tv_main_value1).setText(StringFromatUtils.get2Point(item1.currentValue));
        vh.getTextView(R.id.tv_incease_ratio1).setText(StringFromatUtils.get2PointPercentPlus(item1.percentage));
        vh.getTextView(R.id.tv_incease_value1).setText(StringFromatUtils.get2PointPlus(item1.change));
        itemView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startAnimationActivity((Activity) itemView1.getContext(), StockQuotesActivity.newIntent(itemView1.getContext(), SelectStockBean.copy(lists.get(0))));

            }
        });

        SelectStockBean item2 = SelectStockBean.copy(lists.get(1));
        final View itemView2 = vh.get(R.id.item_content_view2);
        float change2 = item2.percentage;
        vh.getTextView(R.id.tv_main_value2).setTextColor(ColorTemplate.getUpOrDrownCSL(change2));
        if (change2 > 0) {
            vh.getTextView(R.id.tv_main_value2).setCompoundDrawablesWithIntrinsicBounds(
                    itemView2.getResources().getDrawable(R.drawable.ic_grow_up), null, null, null);
        } else if (change2 < 0) {
            vh.getTextView(R.id.tv_main_value2).setCompoundDrawablesWithIntrinsicBounds(
                    itemView2.getResources().getDrawable(R.drawable.ic_grow_down), null, null, null);
        } else {
            vh.getTextView(R.id.tv_main_value2).setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        vh.getTextView(R.id.tv_title_name2).setText(item2.name);
        vh.getTextView(R.id.tv_main_value2).setText(StringFromatUtils.get2Point(item2.currentValue));
        vh.getTextView(R.id.tv_incease_ratio2).setText(StringFromatUtils.get2PointPercentPlus(item2.percentage));
        vh.getTextView(R.id.tv_incease_value2).setText(StringFromatUtils.get2PointPlus(item2.change));
        itemView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startAnimationActivity((Activity) itemView2.getContext(), StockQuotesActivity.newIntent(itemView2.getContext(), SelectStockBean.copy(lists.get(1))));

            }
        });

        SelectStockBean item3 = SelectStockBean.copy(lists.get(2));
        final View itemView3 = vh.get(R.id.item_content_view3);
        float change3 = item3.percentage;
        vh.getTextView(R.id.tv_main_value3).setTextColor(ColorTemplate.getUpOrDrownCSL(change3));
        if (change3 > 0) {
            vh.getTextView(R.id.tv_main_value3).setCompoundDrawablesWithIntrinsicBounds(
                    itemView3.getResources().getDrawable(R.drawable.ic_grow_up), null, null, null);
        } else if (change3 < 0) {
            vh.getTextView(R.id.tv_main_value3).setCompoundDrawablesWithIntrinsicBounds(
                    itemView3.getResources().getDrawable(R.drawable.ic_grow_down), null, null, null);
        } else {
            vh.getTextView(R.id.tv_main_value3).setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        vh.getTextView(R.id.tv_title_name3).setText(item3.name);
        vh.getTextView(R.id.tv_main_value3).setText(StringFromatUtils.get2Point(item3.currentValue));
        vh.getTextView(R.id.tv_incease_ratio3).setText(StringFromatUtils.get2PointPercentPlus(item3.percentage));
        vh.getTextView(R.id.tv_incease_value3).setText(StringFromatUtils.get2PointPlus(item3.change));
        itemView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.startAnimationActivity((Activity) itemView3.getContext(), StockQuotesActivity.newIntent(itemView3.getContext(), SelectStockBean.copy(lists.get(2))));

            }
        });
    }
}
