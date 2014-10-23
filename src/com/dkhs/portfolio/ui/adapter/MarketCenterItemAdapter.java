package com.dkhs.portfolio.ui.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.bean.StockQuotesBean;

public class MarketCenterItemAdapter  extends BaseAdatperSelectStockFund{
	public MarketCenterItemAdapter(Context context, List<SelectStockBean> datas) {
		super(context, datas);
		// TODO Auto-generated constructor stub
	}
	private SelectStockBean mStockQuotesBean;

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler viewHolder = null;
		mStockQuotesBean = mDataList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.market_layout_market, null);
            viewHolder.tvTextName = (TextView) convertView.findViewById(R.id.market_text_name);
            viewHolder.tvTextNameNum = (TextView) convertView.findViewById(R.id.market_text_name_num);
            viewHolder.tvTextItemIndex = (TextView) convertView.findViewById(R.id.market_list_item_index);
            viewHolder.tvTextPercent = (TextView) convertView.findViewById(R.id.market_list_item_percent);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }
        viewHolder.tvTextName.setText(mStockQuotesBean.name);
        viewHolder.tvTextNameNum.setText(mStockQuotesBean.code);
        
        float change = mStockQuotesBean.percentage;
        if(change > 0){
        	viewHolder.tvTextPercent.setTextColor(mContext.getResources().getColor(R.color.red));
        	viewHolder.tvTextItemIndex.setTextColor(mContext.getResources().getColor(R.color.red));
        }else if(change == 0){
        	viewHolder.tvTextPercent.setTextColor(mContext.getResources().getColor(R.color.black));
        	viewHolder.tvTextItemIndex.setTextColor(mContext.getResources().getColor(R.color.black));
        }else{
        	viewHolder.tvTextPercent.setTextColor(mContext.getResources().getColor(R.color.green));
        	viewHolder.tvTextItemIndex.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        viewHolder.tvTextItemIndex.setText( new DecimalFormat("##0.00").format(mStockQuotesBean.currentValue));
        DecimalFormat fnum = new DecimalFormat("##0.00"); 
        String dd=fnum.format(change); 
        viewHolder.tvTextPercent.setText(dd+"%");
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
}
