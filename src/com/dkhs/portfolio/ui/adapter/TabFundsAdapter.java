/**
 * @Title SelectFundAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.CombinationBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName SelectFundAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version 1.0
 */
public class TabFundsAdapter extends BaseAdapter {

    public Context mContext;
    public List<CombinationBean> mDataList = new ArrayList<CombinationBean>();

    public TabFundsAdapter(Context context, List<CombinationBean> datas) {
        this.mContext = context;
        this.mDataList = datas;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_optional_stock_price, null);

            viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            viewHolder.tvStockNum = (TextView) convertView.findViewById(R.id.tv_stock_num);
            viewHolder.tvCurrentValue = (TextView) convertView.findViewById(R.id.tv_current_value);
            viewHolder.tvPercentValue = (TextView) convertView.findViewById(R.id.tv_percent_value);
            // viewHolder.tvIncearseValue = (TextView) convertView.findViewById(R.id.tv_increase_value);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        final CombinationBean item = mDataList.get(position);
        viewHolder.tvStockName.setText(item.getName());
        // if (TextUtils.isEmpty(item.getDescription().trim())) {
        // viewHolder.tvStockNum.setText(mContext.getString(R.string.desc_format,
        // mContext.getString(R.string.desc_def_text)));
        // } else {

        viewHolder.tvStockNum.setText(item.getUser().getUsername());
        // }

        viewHolder.tvPercentValue.setBackgroundColor(ColorTemplate.getUpOrDrowBgColor(item.getAddUpValue()));
        viewHolder.tvPercentValue.setText(StringFromatUtils.get2PointPercentPlus(item.getAddUpValue()));
        // if (StockUitls.isDelistStock(item.list_status)) {
        // viewHolder.tvPercentValue.setText("退市");
        // viewHolder.tvPercentValue.setTypeface(Typeface.DEFAULT);
        // // viewHolder.tvIncearseValue.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));
        // } else if (item.isStop) {
        // viewHolder.tvPercentValue.setText("停牌");
        // // viewHolder.tvPercentValue.setTextColor(ColorTemplate.getTextColor(R.color.theme_gray_press));
        // viewHolder.tvPercentValue.setTypeface(Typeface.DEFAULT);
        // } else {
        // // viewHolder.tvIncearseValue.setTextColor(textCsl);
        // viewHolder.tvPercentValue.setTypeface(Typeface.DEFAULT_BOLD);
        // // viewHolder.tvIncearseValue.setText(StringFromatUtils.get2Point(item.change));
        // viewHolder.tvPercentValue.setVisibility(View.VISIBLE);
        // viewHolder.tvPercentValue.setText(StringFromatUtils.get2PointPercent(item.percentage));
        // // viewHolder.tvIncearseValue.setText(StringFromatUtils.get2Point(item.change));
        // }
        // if (StockUitls.isShangZhengB(item.code)) {
        // viewHolder.tvCurrentValue.setText(StringFromatUtils.get3Point(item.currentValue));
        //
        // } else {
        // viewHolder.tvCurrentValue.setText(StringFromatUtils.get2Point(item.currentValue));
        // }
        // viewHolder.tvPercentValue.setText(StringFromatUtils.get2PointPercent(item.percentage));
        viewHolder.tvCurrentValue.setTextColor(ColorTemplate.getTextColor(R.color.black));
        viewHolder.tvCurrentValue.setText(StringFromatUtils.get4Point(item.getNetvalue()));
        return convertView;
    }

    final static class ViewHodler {
        TextView tvStockName;

        TextView tvStockNum;
        TextView tvCurrentValue;
        TextView tvPercentValue;
        // TextView tvIncearseValue;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDataList.size();
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param position
     * @return
     * @return
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param position
     * @return
     * @return
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
}
