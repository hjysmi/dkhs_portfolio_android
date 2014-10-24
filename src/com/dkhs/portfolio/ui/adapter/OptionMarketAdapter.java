package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;

public class OptionMarketAdapter extends BaseAdapter{
	private Context mContext;
	private List<OptionNewsBean> mDataList;
	private OptionNewsBean mOptionNewsBean;
	public OptionMarketAdapter(Context mContext,List<OptionNewsBean> mDataList){
		this.mContext = mContext;
		this.mDataList = mDataList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler viewHolder = null;
		mOptionNewsBean = mDataList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.adapter_opition_news, null);
            viewHolder.tvTextName = (TextView) convertView.findViewById(R.id.adapter_market_title);
            viewHolder.tvTextNameNum = (TextView) convertView.findViewById(R.id.adapter_market_title_num);
            viewHolder.tvTextDate = (TextView) convertView.findViewById(R.id.option_news_text_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }
        viewHolder.tvTextName.setText(mOptionNewsBean.getTitle());
        viewHolder.tvTextNameNum.setText(mOptionNewsBean.getSymbols().get(0).getSymbol());
        viewHolder.tvTextDate.setText(mOptionNewsBean.getCreatedTime());
        return convertView;
	}
	final static class ViewHodler {
        TextView tvTextName;
        TextView tvTextNameNum;
        TextView tvTextDate;
    }
}