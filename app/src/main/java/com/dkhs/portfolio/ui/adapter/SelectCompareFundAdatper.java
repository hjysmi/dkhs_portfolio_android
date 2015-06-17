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
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName SelectFundAdatper
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-5 下午2:24:13
 * @version 1.0
 */
public class SelectCompareFundAdatper extends BaseAdatperSelectStockFund {

    public SelectCompareFundAdatper(Context context, List<SelectStockBean> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_select_compare_fund, null);

            viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_fund_name);
            viewHolder.mCheckbox = (CheckBox) convertView.findViewById(R.id.cb_select_stock);
            viewHolder.tvIncreaseValue = (TextView) convertView.findViewById(R.id.tv_increase_value);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        SelectStockBean item = mDataList.get(position);

        viewHolder.mCheckbox.setOnCheckedChangeListener(null);
        viewHolder.mCheckbox.setTag(item);
        viewHolder.mCheckbox.setChecked(BaseSelectActivity.mSelectList.contains(item));
        viewHolder.mCheckbox.setOnCheckedChangeListener(this);

        viewHolder.tvStockName.setText(item.name);

        ColorStateList textCsl;
        if (item.percentage > 0) {
            textCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.red);

        } else {
            textCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.green);

        }

        viewHolder.tvIncreaseValue.setTextColor(textCsl);
        viewHolder.tvIncreaseValue.setText(StringFromatUtils.get2PointPercent(item.percentage));
        return convertView;
    }

    final static class ViewHodler {
        TextView tvStockName;
        CheckBox mCheckbox;
        // TextView tvStockNum;
        // TextView tvCurrentValue;
        TextView tvIncreaseValue;
    }
}
