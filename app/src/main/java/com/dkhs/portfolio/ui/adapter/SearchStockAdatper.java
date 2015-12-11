/**
 * @Title SelectFundAdatper.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-9-5 下午2:24:13
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.SelectAddOptionalActivity;
import com.dkhs.portfolio.utils.StockUitls;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName SelectFundAdatper
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-9-5 下午2:24:13
 */
public class SearchStockAdatper extends BaseAdatperSelectStockFund {

    private boolean isCombination;
    VisitorDataEngine mVisitorDataEngine;

    public SearchStockAdatper(Context context, List<SelectStockBean> datas, boolean isCombination) {
        super(context, datas);
        this.isCombination = isCombination;
        mVisitorDataEngine = new VisitorDataEngine();
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

        if (isCombination) {
            viewHolder.mCheckbox.setChecked(BaseSelectActivity.mSelectList.contains(item));
        } else {
            boolean isFollowed = SelectAddOptionalActivity.mFollowList.contains(item);
            item.setFollowed(isFollowed);
            viewHolder.mCheckbox.setChecked(isFollowed);
        }
        viewHolder.mCheckbox.setOnCheckedChangeListener(this);
        viewHolder.mCheckbox.setTag(item);


        if(item.name.contains("丰原药业") || item.code.contains("丰原药业")){

        }
        // System.out.println("SelectStockBean list status:" + item.list_status);
        if (isCombination) {
            if (StockUitls.isDelistStock(item.list_status)) {
                viewHolder.tvSuspend.setVisibility(View.VISIBLE);
                viewHolder.tvSuspend.setText("退市");
                viewHolder.mCheckbox.setVisibility(View.GONE);
            } else if (StockUitls.isListed(item.list_status) && item.is_stop == 1) {
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
            }
        }
        viewHolder.tvStockName.setText(item.name);
        viewHolder.tvStockNum.setText(item.getSymbol());
        if (!TextUtils.isEmpty(item.name) && item.name.length() > 10) {
            viewHolder.tvStockName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//            viewHolder.tvStockNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else {
            viewHolder.tvStockName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//            viewHolder.tvStockNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }

        return convertView;
    }

    final static class ViewHodler {
        TextView tvStockName;
        CheckBox mCheckbox;
        TextView tvStockNum;
        TextView tvSuspend;

    }

}
