/**
 * @Title SelectFundAdatper.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-5 下午2:24:13
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.app.Activity;
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
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.UIUtils;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName SelectFundAdatper
 * @Description 添加自选股列表
 * @date 2014-9-5 下午2:24:13
 */
public class SelectStockAdatper extends BaseAdatperSelectStockFund {
    private boolean isDefColor;
    //    VisitorDataEngine mVisitorDataEngine;
    //    private List<SelectStockBean> localList;
    private boolean isAddNewStock;


    public SelectStockAdatper(Context context) {
        super(context);
        init();
    }

    public SelectStockAdatper(Context context, boolean isdefcolor) {
        super(context);
        this.isDefColor = isdefcolor;
        init();
    }

    public SelectStockAdatper(Context context, List<SelectStockBean> datas) {
        super(context, datas);
        init();
    }

    public SelectStockAdatper(Context context, List<SelectStockBean> datas, boolean isdefcolor) {
        super(context, datas);
        this.isDefColor = isdefcolor;
        init();
    }

    private void init() {
//        mVisitorDataEngine = new VisitorDataEngine();
//        if (!PortfolioApplication.hasUserLogin()) {
//            localList = mVisitorDataEngine.getOptionalStockList();
//        }
    }


    protected void setAddNewStock(boolean isNewstockable) {
        this.isAddNewStock = isNewstockable;
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



        viewHolder.mCheckbox.setOnCheckedChangeListener(null);

        if (this instanceof AddStockItemAdapter) {// 如果是添加自选股界面

            // 如果是游客模式
            if (!PortfolioApplication.hasUserLogin()) {
                if (null != SelectAddOptionalActivity.mFollowList) {
                    boolean isFollowed = SelectAddOptionalActivity.mFollowList.contains(item);
                    mDataList.get(position).setFollowed(isFollowed);
                    viewHolder.mCheckbox.setChecked(isFollowed);
                }
            } else {

                viewHolder.mCheckbox.setChecked(item.isFollowed);
            }

            // viewHolder.mCheckbox.setChecked(SelectAddOptionalActivity.mFollowList.contains(item));
        } else {
            viewHolder.mCheckbox.setChecked(BaseSelectActivity.mSelectList.contains(item));
        }
        viewHolder.mCheckbox.setTag(item);
        viewHolder.mCheckbox.setOnCheckedChangeListener(this);
        // }
        // viewHolder.mCheckbox.setOnClickListener(new OnCheckListener(viewHolder.mCheckbox,position));
        viewHolder.tvStockName.setText(item.name);
        viewHolder.tvStockNum.setText(item.symbol);

        if (StockUitls.isDelistStock(item.list_status)) {
            viewHolder.tvSuspend.setVisibility(View.VISIBLE);
            viewHolder.tvSuspend.setText(R.string.exit_stock);
            viewHolder.mCheckbox.setVisibility(View.GONE);
            // viewHolder.tvIncreaseValue.setVisibility(View.INVISIBLE);
        } else if (item.isStop) {
            viewHolder.tvSuspend.setVisibility(View.VISIBLE);
            viewHolder.tvSuspend.setText(R.string.stop_stock);
            viewHolder.mCheckbox.setVisibility(View.GONE);
            // viewHolder.tvIncreaseValue.setText("—");
            // viewHolder.tvIncreaseValue.setVisibility(View.INVISIBLE);
        } else if (StockUitls.isNewStock(item.list_status) && !isAddNewStock) {
            viewHolder.tvSuspend.setVisibility(View.VISIBLE);
            viewHolder.tvSuspend.setText(R.string.new_stock);
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
                if (StockUitls.isFundType(itemStock.symbol_type)) {
                    UIUtils.startAnimationActivity((Activity) mContext, FundDetailActivity.newIntent(mContext, itemStock));
                } else {

                    UIUtils.startAnimationActivity((Activity) mContext, StockQuotesActivity.newIntent(mContext, itemStock));
                }

            }
        }
    }
}
