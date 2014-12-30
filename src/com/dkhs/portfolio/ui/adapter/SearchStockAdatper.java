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
import com.dkhs.portfolio.utils.StockUitls;

/**
 * @ClassName SelectFundAdatper
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-5 下午2:24:13
 * @version 1.0
 */
public class SearchStockAdatper extends BaseAdatperSelectStockFund {

    private boolean isCombination;

    public SearchStockAdatper(Context context, List<SelectStockBean> datas, boolean isCombination) {
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
            viewHolder.tvSuspend = (TextView) convertView.findViewById(R.id.tv_suspend);
            viewHolder.mCheckbox = (CheckBox) convertView.findViewById(R.id.cb_select_stock);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        SelectStockBean item = mDataList.get(position);

        viewHolder.mCheckbox.setOnCheckedChangeListener(null);
        viewHolder.mCheckbox.setTag(item);
        if (isCombination) {
            viewHolder.mCheckbox.setChecked(BaseSelectActivity.mSelectList.contains(item));
        } else {
            viewHolder.mCheckbox.setChecked(SelectAddOptionalActivity.mFollowList.contains(item));
        }
        viewHolder.mCheckbox.setOnCheckedChangeListener(this);

        System.out.println("SelectStockBean list status:" + item.list_status);
        if (isCombination) {
            if (item.isStop) {
                viewHolder.tvSuspend.setVisibility(View.VISIBLE);
                viewHolder.tvSuspend.setText("停牌");
                viewHolder.mCheckbox.setVisibility(View.GONE);
                // viewHolder.tvIncreaseValue.setText("—");
                // viewHolder.tvIncreaseValue.setVisibility(View.INVISIBLE);
            } else if (StockUitls.isNewStock(item.list_status)) {
                viewHolder.tvSuspend.setVisibility(View.VISIBLE);
                viewHolder.tvSuspend.setText("新股");
                viewHolder.mCheckbox.setVisibility(View.GONE);
            }

        }
        viewHolder.tvStockName.setText(item.name);
        viewHolder.tvStockNum.setText(mContext.getString(R.string.quotes_format, item.code));

        return convertView;
    }

    final static class ViewHodler {
        TextView tvStockName;
        CheckBox mCheckbox;
        TextView tvStockNum;
        TextView tvSuspend;

    }

}
