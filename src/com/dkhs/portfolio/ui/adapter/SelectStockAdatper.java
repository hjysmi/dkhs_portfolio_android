/**
 * @Title SelectFundAdatper.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-5 下午2:24:13
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName SelectFundAdatper
 * @Description 添加自选股列表
 * @author zjz
 * @date 2014-9-5 下午2:24:13
 * @version 1.0
 */
public class SelectStockAdatper extends BaseAdatperSelectStockFund {
    private Context context;
    private boolean isDefColor;
    private VisitorDataEngine mVisitorDataEngine;

    public SelectStockAdatper(Context context, List<SelectStockBean> datas) {
        super(context, datas);
        this.context = context;
        mVisitorDataEngine = new VisitorDataEngine();
    }

    public SelectStockAdatper(Context context, List<SelectStockBean> datas, boolean isdefcolor) {
        super(context, datas);
        this.context = context;
        this.isDefColor = isdefcolor;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_select_conbin_stock, null);

            viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            viewHolder.tvStockNum = (TextView) convertView.findViewById(R.id.tv_stock_num);
            viewHolder.mCheckbox = (CheckBox) convertView.findViewById(R.id.cb_select_stock);
            viewHolder.tvCurrentValue = (TextView) convertView.findViewById(R.id.tv_current_value);
            viewHolder.tvIncreaseValue = (TextView) convertView.findViewById(R.id.tv_increase_value);
            viewHolder.tvSuspend = (TextView) convertView.findViewById(R.id.tv_suspend);
            viewHolder.tvStockLayout = (LinearLayout) convertView.findViewById(R.id.tv_stock_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        final SelectStockBean item = mDataList.get(position);

        if (!PortfolioApplication.hasUserLogin()) {
            final CheckBox cbBox = viewHolder.mCheckbox;
            viewHolder.mCheckbox.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // cbBox.setChecked(false);
                    // UIUtils.iStartLoginActivity(context);
                    item.isFollowed = true;
                    item.sortId = 9999;
                    mVisitorDataEngine.saveOptionalStock(item);
                }
            });
        } else {

            viewHolder.mCheckbox.setOnCheckedChangeListener(null);
            viewHolder.mCheckbox.setTag(item);
            if (this instanceof AddStockItemAdapter) {
                viewHolder.mCheckbox.setChecked(item.isFollowed);
                // viewHolder.mCheckbox.setChecked(SelectAddOptionalActivity.mFollowList.contains(item));
            } else {
                viewHolder.mCheckbox.setChecked(BaseSelectActivity.mSelectList.contains(item));
            }
            viewHolder.mCheckbox.setOnCheckedChangeListener(this);
        }
        // viewHolder.mCheckbox.setOnClickListener(new OnCheckListener(viewHolder.mCheckbox,position));
        viewHolder.tvStockName.setText(item.name);
        viewHolder.tvStockNum.setText(item.code);

        if (StockUitls.isDelistStock(item.list_status)) {
            viewHolder.tvSuspend.setVisibility(View.VISIBLE);
            viewHolder.tvSuspend.setText("退市");
            viewHolder.mCheckbox.setVisibility(View.GONE);
            // viewHolder.tvIncreaseValue.setVisibility(View.INVISIBLE);
        } else if (item.isStop) {
            viewHolder.tvSuspend.setVisibility(View.VISIBLE);
            viewHolder.tvSuspend.setText("停牌");
            viewHolder.mCheckbox.setVisibility(View.GONE);
            // viewHolder.tvIncreaseValue.setText("—");
            // viewHolder.tvIncreaseValue.setVisibility(View.INVISIBLE);
        } else if (StockUitls.isNewStock(item.list_status)) {
            viewHolder.tvSuspend.setVisibility(View.VISIBLE);
            viewHolder.tvSuspend.setText("新股");
            viewHolder.mCheckbox.setVisibility(View.GONE);
        } else {
            viewHolder.tvSuspend.setVisibility(View.GONE);
            viewHolder.mCheckbox.setVisibility(View.VISIBLE);
            // viewHolder.tvIncreaseValue.setText(StringFromatUtils.get2PointPercent(item.percentage));
            // viewHolder.tvIncreaseValue.setVisibility(View.VISIBLE);
        }
        ColorStateList textCsl = null;
        if (StockUitls.isDelistStock(item.list_status) || item.isStop || isDefColor) {
            textCsl = ColorTemplate.getTextColor(R.color.theme_color);
        } else {
            textCsl = ColorTemplate.getUpOrDrownCSL(item.percentage);
        }

        viewHolder.tvIncreaseValue.setText(StringFromatUtils.get2PointPercent(item.percentage));
        viewHolder.tvCurrentValue.setTextColor(textCsl);
        viewHolder.tvIncreaseValue.setTextColor(textCsl);
        if (StockUitls.isNewStock(item.list_status) || StockUitls.isDelistStock(item.list_status) || item.isStop) {
            viewHolder.tvSuspend.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));

        } else {

            viewHolder.tvSuspend.setTextColor(textCsl);
        }
        viewHolder.tvCurrentValue.setText(StringFromatUtils.get2Point(item.currentValue));

        return convertView;
    }

    final static class ViewHodler {
        TextView tvStockName;
        CheckBox mCheckbox;
        TextView tvStockNum;
        TextView tvCurrentValue;
        TextView tvIncreaseValue;
        TextView tvSuspend;

        LinearLayout tvStockLayout;
    }

    // class OnItemListener implements OnClickListener {
    // private int position;
    //
    // public OnItemListener(int position) {
    // this.position = position;
    // }
    //
    // @Override
    // public void onClick(View v) {
    // // TODO Auto-generated method stub
    // SelectStockBean itemStock = mDataList.get(position);
    //
    // context.startActivity(StockQuotesActivity.newIntent(context, itemStock));
    // }
    //
    // }

    class OnCheckListener implements OnClickListener {
        private int position;
        private CheckBox mCheckbox;

        public OnCheckListener(CheckBox mCheckbox, int position) {
            this.position = position;
            this.mCheckbox = mCheckbox;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (mCheckbox.isChecked()) {
                SelectStockBean itemStock = mDataList.get(position);
                itemStock.isFollowed = true;
                context.startActivity(StockQuotesActivity.newIntent(context, itemStock));
            }
        }
    }
}
