package com.dkhs.portfolio.ui.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;
import com.dkhs.portfolio.ui.PositionAdjustActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

public class MarketCenterItemAdapter extends BaseAdatperSelectStockFund {
    private boolean isDefColor = false;

    public MarketCenterItemAdapter(Context context, List<SelectStockBean> datas) {
        super(context, datas);
        // TODO Auto-generated constructor stub
    }

    public MarketCenterItemAdapter(Context context, List<SelectStockBean> datas, boolean isDefColor) {
        super(context, datas);
        this.isDefColor = isDefColor;
    }

    private SelectStockBean mStockQuotesBean;

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHodler viewHolder = null;
        mStockQuotesBean = mDataList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.market_layout_market, null);
            viewHolder.tvTextName = (TextView) convertView.findViewById(R.id.market_text_name);
            viewHolder.tvTextNameNum = (TextView) convertView.findViewById(R.id.market_text_name_num);
            viewHolder.tvTextItemIndex = (TextView) convertView.findViewById(R.id.market_list_item_index);
            viewHolder.tvTextPercent = (TextView) convertView.findViewById(R.id.market_list_item_percent);
            viewHolder.tvLayoutTitle = (LinearLayout) convertView.findViewById(R.id.market_layout_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }
        viewHolder.tvTextName.setText(mStockQuotesBean.name);
        viewHolder.tvTextNameNum.setText(mStockQuotesBean.code);

        float change = mStockQuotesBean.percentage;
        ColorStateList textCsl = null;
        if (isDefColor) {
            textCsl = ColorTemplate.getTextColor(R.color.theme_color);
        } else if (mStockQuotesBean.isStop || StockUitls.isDelistStock(mStockQuotesBean.list_status)) {
            textCsl = ColorTemplate.getTextColor(R.color.theme_color);
        } else {
            textCsl = ColorTemplate.getUpOrDrownCSL(change);
        }
        // viewHolder.tvTextPercent.setTextColor(textCsl);
        viewHolder.tvTextItemIndex.setTextColor(textCsl);
        if (StockUitls.isShangZhengB(mStockQuotesBean.code)) {
            viewHolder.tvTextItemIndex.setText(StringFromatUtils.get3Point(mStockQuotesBean.currentValue));
        } else {
            viewHolder.tvTextItemIndex.setText(StringFromatUtils.get2Point(mStockQuotesBean.currentValue));
        }
        // viewHolder.tvTextPercent.setText(StringFromatUtils.get2PointPercent(change));
        viewHolder.tvLayoutTitle.setOnClickListener(new OnItemListener(position));

        if (StockUitls.isDelistStock(mStockQuotesBean.list_status)) {
            viewHolder.tvTextPercent.setText("退市");
            viewHolder.tvTextPercent.setTypeface(Typeface.DEFAULT);
            viewHolder.tvTextPercent.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));
        } else if (mStockQuotesBean.isStop) {
            viewHolder.tvTextPercent.setText("停牌");
            viewHolder.tvTextPercent.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));
            viewHolder.tvTextPercent.setTypeface(Typeface.DEFAULT);
        } else {
            viewHolder.tvTextPercent.setTextColor(textCsl);
            viewHolder.tvTextPercent.setTypeface(Typeface.DEFAULT_BOLD);
            viewHolder.tvTextPercent.setText(StringFromatUtils.get2PointPercent(change));
        }

        return convertView;
    }

    final static class ViewHodler {
        LinearLayout tvLayoutTitle;
        TextView tvTextIndex;
        TextView tvTextEdition;
        TextView tvTextChange;
        TextView tvTextName;
        TextView tvTextNameNum;
        TextView tvTextItemIndex;
        TextView tvTextPercent;
        ImageView tvUpDown;
    }

    class OnItemListener implements OnClickListener {
        private int position;

        public OnItemListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            SelectStockBean itemStock = mDataList.get(position);
            UIUtils.startAminationActivity((Activity) mContext, StockQuotesActivity.newIntent(mContext, itemStock));

            // mContext.startActivity(StockQuotesActivity.newIntent(mContext, itemStock));
        }

    }
}