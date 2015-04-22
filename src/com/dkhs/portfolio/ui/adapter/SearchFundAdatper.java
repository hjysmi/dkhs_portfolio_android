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
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;

/**
 * @ClassName SelectFundAdatper
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-5 下午2:24:13
 * @version 1.0
 */
public class SearchFundAdatper extends BaseAdatperSelectStockFund {

    private boolean isCombination;

    public SearchFundAdatper(Context context, List<SelectStockBean> datas, boolean isCombination) {
        super(context, datas);
        this.isCombination = isCombination;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_search_conbin_stock, null);

            viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            viewHolder.tvStockNum = (TextView) convertView.findViewById(R.id.tv_stock_num);
            viewHolder.mCheckbox = (CheckBox) convertView.findViewById(R.id.cb_select_stock);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        SelectStockBean item = mDataList.get(position);

        viewHolder.mCheckbox.setOnCheckedChangeListener(null);
        viewHolder.mCheckbox.setTag(item);
        // if (isCombination) {
        viewHolder.mCheckbox.setChecked(BaseSelectActivity.mSelectList.contains(item));
        // } else {
        // viewHolder.mCheckbox.setChecked(SelectAddOptionalActivity.mFollowList.contains(item));
        // }
        viewHolder.mCheckbox.setOnCheckedChangeListener(this);

        viewHolder.tvStockName.setText(item.name);
        viewHolder.tvStockNum.setText(mContext.getString(R.string.quotes_format, item.code));

        return convertView;
    }

    final static class ViewHodler {
        TextView tvStockName;
        CheckBox mCheckbox;
        TextView tvStockNum;

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param buttonView
     * @param isChecked
     * @return
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SelectStockBean csBean = (SelectStockBean) buttonView.getTag();

        if (isChecked) {
            if (BaseSelectActivity.mSelectList.size() == 5) {
                Toast.makeText(mContext, mContext.getString(R.string.max_fund_select_tip), Toast.LENGTH_SHORT).show();
                buttonView.setChecked(false);
                return;
            }
        }
        super.onCheckedChanged(buttonView, isChecked);
    }
}
