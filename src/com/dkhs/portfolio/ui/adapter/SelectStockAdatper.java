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
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.BaseSelectActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName SelectFundAdatper
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-9-5 下午2:24:13
 * @version 1.0
 */
public class SelectStockAdatper extends BaseAdatperSelectStockFund {
	private Context context;
    public SelectStockAdatper(Context context, List<SelectStockBean> datas) {
        super(context, datas);
        this.context = context;
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
            viewHolder.tvStockLayout = (LinearLayout) convertView.findViewById(R.id.tv_stock_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        SelectStockBean item = mDataList.get(position);

        //viewHolder.mCheckbox.setOnCheckedChangeListener(null);
        viewHolder.mCheckbox.setTag(item);
        viewHolder.tvStockLayout.setOnClickListener(new OnItemListener(position));
        if (this instanceof AddStockItemAdapter) {
            viewHolder.mCheckbox.setChecked(item.isFollowed);
        } else {
            viewHolder.mCheckbox.setChecked(BaseSelectActivity.mSelectList.contains(item));
        }
        viewHolder.mCheckbox.setOnCheckedChangeListener(this);
        viewHolder.mCheckbox.setOnClickListener(new OnCheckListener(viewHolder.mCheckbox,position));
        viewHolder.tvStockName.setText(item.name);
        viewHolder.tvStockNum.setText(item.code);

        ColorStateList textCsl;
        if (item.percentage >= 0) {
            textCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.red);

        } else {
            textCsl = (ColorStateList) mContext.getResources().getColorStateList(R.color.green);

        }
        viewHolder.tvCurrentValue.setTextColor(textCsl);
        viewHolder.tvCurrentValue.setText("" + item.currentValue);
        viewHolder.tvIncreaseValue.setTextColor(textCsl);
        viewHolder.tvIncreaseValue.setText(StringFromatUtils.getPercentValue(item.percentage));

        return convertView;
    }

    final static class ViewHodler {
        TextView tvStockName;
        CheckBox mCheckbox;
        TextView tvStockNum;
        TextView tvCurrentValue;
        TextView tvIncreaseValue;
        
        LinearLayout tvStockLayout;
    }
    class OnItemListener implements OnClickListener{
    	private int position;
    	public OnItemListener(int position){
    		this.position = position;
    	}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SelectStockBean itemStock = mDataList.get(position);
            itemStock.isFollowed = true;
           
            
            context.startActivity(StockQuotesActivity.newIntent(context, itemStock));
		}
    	
    }
    class OnCheckListener implements OnClickListener{
    	private int position;
    	private CheckBox mCheckbox;
    	public OnCheckListener(CheckBox mCheckbox,int position){
    		this.position = position;
    		this.mCheckbox = mCheckbox;
    	}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mCheckbox.isChecked()){
				SelectStockBean itemStock = mDataList.get(position);
	            itemStock.isFollowed = true;context.startActivity(StockQuotesActivity.newIntent(context, itemStock));
			}
		}
    }
}
