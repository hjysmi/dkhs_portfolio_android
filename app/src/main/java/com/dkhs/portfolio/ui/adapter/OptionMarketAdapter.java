package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.utils.TimeUtils;

import java.util.List;

public class OptionMarketAdapter extends BaseAdapter {
    private Context mContext;
    private List<OptionNewsBean> mDataList;
    private Rect rect;

    public OptionMarketAdapter(Context mContext, List<OptionNewsBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);
        Paint p = new Paint();
        rect = new Rect();
        String text = "正正正正正";
        p.setTextSize(mContext.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
        p.getTextBounds(text, 0, text.length(), rect);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (null == mDataList) {
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
            ViewHodler viewHolder = null;
            OptionNewsBean mOptionNewsBean = mDataList.get(position);
            if (null == convertView) {
                viewHolder = new ViewHodler();
                convertView = View.inflate(mContext, R.layout.adapter_opition_news, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.adapter_market_title);
             //   viewHolder.tvTextNameNum = (TextView) convertView.findViewById(R.id.adapter_market_title_num);
                viewHolder.tvTextDate = (TextView) convertView.findViewById(R.id.option_news_text_date);
             //   viewHolder.text = (TextView) convertView.findViewById(R.id.zhengquan);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHodler) convertView.getTag();
            }
       //     viewHolder.text.setVisibility(View.GONE);
                /*Paint p= new Paint();
                Rect rect = new Rect();
				p.setTextSize( mActivity.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
				p.getTextBounds(mOptionNewsBean.getSimpleTitle(), 0, mOptionNewsBean.getSimpleTitle().length(), rect);
				if(dm.widthPixels - 50   < rect.width()){
					int le = (int) (mOptionNewsBean.getSimpleTitle().length() -  mOptionNewsBean.getSimpleTitle().length() *(rect.width() - dm.widthPixels + 50)/rect.width() - 3);
					String text = mOptionNewsBean.getSimpleTitle().substring(0, le);
					tv.setText(text + "...");
				}else{*/
            viewHolder.tv.setText(mOptionNewsBean.getTitle());
            //}
            //ViewTreeObserver observer = tv.getViewTreeObserver();

         //   viewHolder.tvTextNameNum.getLayoutParams().width = rect.width();
         //   viewHolder.tvTextNameNum.setText(mOptionNewsBean.getSymbols().get(0).getAbbrName());
           /* if (null != mOptionNewsBean.getSource()) {
                viewHolder.text.setText(mOptionNewsBean.getSource().getTitle());
            }
*/
            viewHolder.tvTextDate.setText(TimeUtils.getBriefTimeString(mOptionNewsBean.getPublish()));
//            if (TimeUtils.isSameDay(mOptionNewsBean.getPublish())) {
//                viewHolder.tvTextDate.setText(TimeUtils.getTimeString(mOptionNewsBean.getPublish()));
//            } else {
//                viewHolder.tvTextDate.setText(TimeUtils.getMMDDString(mOptionNewsBean.getPublish()));
//            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    final static class ViewHodler {
       // TextView tvTextName;
     //   TextView tvTextNameNum;
        TextView tvTextDate;
       // TextView text;
        TextView tv;
    }
}
