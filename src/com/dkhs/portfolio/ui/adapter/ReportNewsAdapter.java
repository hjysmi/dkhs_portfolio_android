package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;

public class ReportNewsAdapter extends BaseAdapter{
	private Context mContext;
	private List<OptionNewsBean> mDataList;
	private OptionNewsBean mOptionNewsBean;
	public ReportNewsAdapter(Context mContext,List<OptionNewsBean> mDataList){
		this.mContext = mContext;
		this.mDataList = mDataList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(null == mDataList){
			return 0;
		}
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
		try {
			mOptionNewsBean = mDataList.get(position);
			if (convertView == null) {
			    viewHolder = new ViewHodler();
			    convertView = View.inflate(mContext, R.layout.adapter_report_news, null);
			    viewHolder.tvTextName = (TextView) convertView.findViewById(R.id.adapter_market_title);
			    viewHolder.tvTextNameNum = (TextView) convertView.findViewById(R.id.adapter_market_title_num);
			    viewHolder.tvTextDate = (TextView) convertView.findViewById(R.id.option_news_text_date);
			    viewHolder.tvTextFrom = (TextView) convertView.findViewById(R.id.adapter_market_from);
			    convertView.setTag(viewHolder);
			} else {
			    viewHolder = (ViewHodler) convertView.getTag();
			}
			viewHolder.tvTextName.setText(mOptionNewsBean.getTitle());
			if(mOptionNewsBean.getSymbols().size() > 0){
				viewHolder.tvTextNameNum.setText(mOptionNewsBean.getSymbols().get(0).getAbbrName() + " " +mOptionNewsBean.getSymbols().get(0).getId());
			}else{
				viewHolder.tvTextNameNum.setText("");
			}
			viewHolder.tvTextDate.setText(mOptionNewsBean.getCreatedTime().replace("T", " ").substring(0, mOptionNewsBean.getCreatedTime().length()-4));
			if(null != mOptionNewsBean.getSource()){
				viewHolder.tvTextFrom.setText(mOptionNewsBean.getSource().getTitle());
			}else{
				viewHolder.tvTextFrom.setText("");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return convertView;
	}
	final static class ViewHodler {
        TextView tvTextName;
        TextView tvTextNameNum;
        TextView tvTextDate;
        TextView tvTextFrom;
    }
}