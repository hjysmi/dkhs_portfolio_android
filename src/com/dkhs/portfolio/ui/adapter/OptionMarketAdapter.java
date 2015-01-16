package com.dkhs.portfolio.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.ui.adapter.OptionForOnelistAdapter.ViewHodler;
import com.dkhs.portfolio.utils.TimeUtils;

public class OptionMarketAdapter extends BaseAdapter{
	private Context mContext;
	private List<OptionNewsBean> mDataList;
	private OptionNewsBean mOptionNewsBean;
	private ViewHodler viewHolder = null;
	private DisplayMetrics dm;
	private Rect rect;
	public OptionMarketAdapter(Context mContext,List<OptionNewsBean> mDataList){
		this.mContext = mContext;
		this.mDataList = mDataList;
		dm = new DisplayMetrics();
        WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);
        Paint p= new Paint(); 
		rect = new Rect();
		String text = "正正正正正";
		p.setTextSize( mContext.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
		p.getTextBounds(text, 0, text.length(), rect); 
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
		try {
			mOptionNewsBean = mDataList.get(position);
			if(null == convertView){
			    viewHolder = new ViewHodler();
			    convertView = View.inflate(mContext, R.layout.adapter_opition_news, null);
			    viewHolder.tv = (TextView) convertView.findViewById(R.id.adapter_market_title);
			    viewHolder.tvTextNameNum = (TextView) convertView.findViewById(R.id.adapter_market_title_num);
			    viewHolder.tvTextDate = (TextView) convertView.findViewById(R.id.option_news_text_date);
			    viewHolder.text = (TextView) convertView.findViewById(R.id.zhengquan);
			    convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHodler) convertView.getTag();
            }
			    viewHolder.text.setVisibility(View.GONE);
			    /*Paint p= new Paint(); 
				Rect rect = new Rect();
				p.setTextSize( mContext.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
				p.getTextBounds(mOptionNewsBean.getTitle(), 0, mOptionNewsBean.getTitle().length(), rect); 
				if(dm.widthPixels - 50   < rect.width()){
					int le = (int) (mOptionNewsBean.getTitle().length() -  mOptionNewsBean.getTitle().length() *(rect.width() - dm.widthPixels + 50)/rect.width() - 3);
					String text = mOptionNewsBean.getTitle().substring(0, le);
					tv.setText(text + "...");
				}else{*/
			    viewHolder.tv.setText(mOptionNewsBean.getTitle());
				//}
			//ViewTreeObserver observer = tv.getViewTreeObserver();
				
			viewHolder.tvTextNameNum.getLayoutParams().width=rect.width();
			viewHolder.tvTextNameNum.setText(mOptionNewsBean.getSymbols().get(0).getAbbrName());
			Calendar old = TimeUtils.toCalendarAddHour(mOptionNewsBean.getPublish());
			if(null != mOptionNewsBean.getSource()){
			    viewHolder.text.setText(mOptionNewsBean.getSource().getTitle());
			}
			if(TimeUtils.compareTime(old)){
				viewHolder.tvTextDate.setText((old.get(Calendar.HOUR_OF_DAY) < 10 ? ("0" + old.get(Calendar.HOUR_OF_DAY)) : old.get(Calendar.HOUR_OF_DAY) ) + ":" + (old.get(Calendar.MINUTE) < 10 ? ("0" + old.get(Calendar.MINUTE)) : old.get(Calendar.MINUTE)));
			}else{
				int t = old.get(Calendar.MONTH) + 1;
				viewHolder.tvTextDate.setText((t < 10 ? ("0" + t) : t) +"-" + (old.get(Calendar.DAY_OF_MONTH) < 10 ? ("0" + old.get(Calendar.DAY_OF_MONTH)) :old.get(Calendar.DAY_OF_MONTH)));
			}
			//viewHolder.tvTextDate.setText(mOptionNewsBean.getPublish().replace("T", " ").substring(0, mOptionNewsBean.getCreatedTime().length()-6) + "00");
			/*observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
				ViewTreeObserver obs = tv.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				if(tv.getLineCount() > 1){
					String text;
					try {
						int lineEndIndex = tv.getLayout().getLineEnd(0); //设置第六行打省略号
						if(tv.getLayout().getLineEnd(1) > (lineEndIndex / 2)){
							text = tv.getText().subSequence(0, lineEndIndex*3/2) +"...";
							tv.setText(text);
						}else{
							tv.setText(mOptionNewsBean.getTitle());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
				});*/
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
        TextView text;
        TextView tv;
    }
}
