package com.dkhs.portfolio.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.ui.adapter.InfoOptionAdapter.ViewHodler;
import com.dkhs.portfolio.utils.TimeUtils;

public class ReportNewsAdapter extends BaseAdapter {
    private Context mContext;
    private List<OptionNewsBean> mDataList;
    private OptionNewsBean mOptionNewsBean;
    private DisplayMetrics dm;
    private Rect rects;

    public ReportNewsAdapter(Context mContext, List<OptionNewsBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        dm = new DisplayMetrics();
        WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);
        Paint p = new Paint();
        rects = new Rect();
        String text = "正正正正正";
        p.setTextSize(mContext.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
        p.getTextBounds(text, 0, text.length(), rects);

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
        ViewHodler viewHolder = null;
        // try {
        // mOptionNewsBean = mDataList.get(position);
        // if (convertView == null) {
        // viewHolder = new ViewHodler();
        // convertView = View.inflate(mContext, R.layout.adapter_report_news, null);
        // viewHolder.tvTextName = (TextView) convertView.findViewById(R.id.adapter_market_title);
        // viewHolder.tvTextNameNum = (TextView) convertView.findViewById(R.id.adapter_market_title_num);
        // viewHolder.tvTextDate = (TextView) convertView.findViewById(R.id.option_news_text_date);
        // viewHolder.tvTextDates = (TextView) convertView.findViewById(R.id.option_news_text_dates);
        // viewHolder.tvTextFrom = (TextView) convertView.findViewById(R.id.adapter_market_from);
        // convertView.setTag(viewHolder);
        // } else {
        // viewHolder = (ViewHodler) convertView.getTag();
        // }
        // viewHolder.tvTextName.setText(mOptionNewsBean.getTitle().replaceAll("\n", "").replaceAll("\r",
        // "").replaceAll("\t", ""));
        // if(null != mOptionNewsBean.getSymbols() && mOptionNewsBean.getSymbols().size() > 0){
        // viewHolder.tvTextNameNum.getLayoutParams().width=rects.width();
        // viewHolder.tvTextNameNum.setText(mOptionNewsBean.getSymbols().get(0).getAbbrName());
        // Paint p= new Paint();
        // Rect rect = new Rect();
        // p.setTextSize( mContext.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
        // p.getTextBounds(mOptionNewsBean.getTitle(), 0, mOptionNewsBean.getTitle().length(), rect);
        // if(dm.widthPixels - 50 < rect.width()){
        // int le = (int) (mOptionNewsBean.getTitle().length() - mOptionNewsBean.getTitle().length() *(rect.width() -
        // dm.widthPixels + 50)/rect.width() - 3);
        // String text = mOptionNewsBean.getTitle().substring(0, le).replaceAll("\n", "").replaceAll("\r",
        // "").replaceAll("\t", "");
        // viewHolder.tvTextName.setText(text + "...");
        // }
        // if(null != mOptionNewsBean.getSource()){
        // viewHolder.tvTextFrom.setText(mOptionNewsBean.getSource().getTitle());
        // }
        // }
        // Calendar old = TimeUtils.toCalendarAddHour(mOptionNewsBean.getPublish());
        // if(TimeUtils.compareTime(old)){
        // viewHolder.tvTextDate.setText((old.get(Calendar.HOUR_OF_DAY) < 10 ? ("0" + old.get(Calendar.HOUR_OF_DAY)) :
        // old.get(Calendar.HOUR_OF_DAY) ) + ":" + (old.get(Calendar.MINUTE) < 10 ? ("0" + old.get(Calendar.MINUTE)) :
        // old.get(Calendar.MINUTE)));
        // }else{
        // int t = old.get(Calendar.MONTH) + 1;
        // viewHolder.tvTextDate.setText((t < 10 ? ("0" + t) : t) +"-" + (old.get(Calendar.DAY_OF_MONTH) < 10 ? ("0" +
        // old.get(Calendar.DAY_OF_MONTH)) :old.get(Calendar.DAY_OF_MONTH)));
        // }
        // if(null != mOptionNewsBean.getSource() && !(null != mOptionNewsBean.getSymbols() &&
        // mOptionNewsBean.getSymbols().size() > 0)){
        // viewHolder.tvTextNameNum.getLayoutParams().width=rects.width();
        // viewHolder.tvTextNameNum.setText(mOptionNewsBean.getSource().getTitle());
        // Paint p= new Paint();
        // Rect rect = new Rect();
        // p.setTextSize( mContext.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
        // p.getTextBounds(mOptionNewsBean.getTitle(), 0, mOptionNewsBean.getTitle().length(), rect);
        // if(dm.widthPixels * 3/2- 50 < rect.width()){
        // int le = (int) (mOptionNewsBean.getTitle().length() - mOptionNewsBean.getTitle().length() *(rect.width() -
        // dm.widthPixels* 3/2 + 50)/rect.width() - 3);
        // String text = mOptionNewsBean.getTitle().substring(0, le);
        // viewHolder.tvTextName.setText(text + "...");
        // }else{
        // viewHolder.tvTextName.setText( mOptionNewsBean.getTitle());
        // }
        // /*viewHolder.tvTextDate.setVisibility(View.GONE);
        // viewHolder.tvTextDates.setVisibility(View.VISIBLE);
        // if(TimeUtils.compareTime(old)){
        // viewHolder.tvTextDates.setText((old.get(Calendar.HOUR_OF_DAY) < 10 ? ("0" + old.get(Calendar.HOUR_OF_DAY)) :
        // old.get(Calendar.HOUR_OF_DAY) ) + ":" + (old.get(Calendar.MINUTE) < 10 ? ("0" + old.get(Calendar.MINUTE)) :
        // old.get(Calendar.MINUTE)));
        // }else{
        // int t = old.get(Calendar.MONTH) + 1;
        // viewHolder.tvTextDates.setText((t < 10 ? ("0" + t) : t) +"-" + (old.get(Calendar.DAY_OF_MONTH) < 10 ? ("0" +
        // old.get(Calendar.DAY_OF_MONTH)) :old.get(Calendar.DAY_OF_MONTH)));
        // }*/
        // /*android.widget.RelativeLayout.LayoutParams params =
        // (RelativeLayout.LayoutParams)viewHolder.tvTextDate.getLayoutParams();
        // params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        // viewHolder.tvTextDate.setLayoutParams(params);*/
        // }

        try {
            mOptionNewsBean = mDataList.get(position);
            if (null == convertView) {
                viewHolder = new ViewHodler();
                convertView = View.inflate(mContext, R.layout.adapter_info_optional, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.adapter_market_title);
                viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stockname);
                viewHolder.tvTextDate = (TextView) convertView.findViewById(R.id.option_news_text_date);
                viewHolder.text = (TextView) convertView.findViewById(R.id.zhengquan);
                viewHolder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHodler) convertView.getTag();
            }
            if (null != mOptionNewsBean.getSource() && !TextUtils.isEmpty(mOptionNewsBean.getSource().getTitle())) {

                viewHolder.text.setVisibility(View.VISIBLE);
                viewHolder.text.setText(mOptionNewsBean.getSource().getTitle());
            } else {

                viewHolder.text.setVisibility(View.GONE);
            }

            if (mOptionNewsBean.getContentType().equals("20")) {
                viewHolder.tvType.setText("【公告】");
                // viewHolder.text.setVisibility(View.GONE);
            } else {
                viewHolder.tvType.setText("【研报】");
            }
            viewHolder.text.setVisibility(View.VISIBLE);
            // } else {
            // viewHolder.tvType.setText("");
            // }

            viewHolder.tv.setText(mOptionNewsBean.getTitle());
            // viewHolder.tvStockName.setText(mOptionNewsBean.getSymbols().get(0).getAbbrName());
            viewHolder.tvStockName.setVisibility(View.GONE);
            // viewHolder.tvTextNameNum.getLayoutParams().width = rect.width();
            // viewHolder.tvTextNameNum.setText(mOptionNewsBean.getSymbols().get(0).getAbbrName());
            Calendar old = TimeUtils.toCalendarAddHour(mOptionNewsBean.getPublish());
            if (null != mOptionNewsBean.getSource()) {
                viewHolder.text.setText(mOptionNewsBean.getSource().getTitle());
            }
            int t = old.get(Calendar.MONTH) + 1;
            viewHolder.tvTextDate.setText((t < 10 ? ("0" + t) : t)
                    + "-"
                    + (old.get(Calendar.DAY_OF_MONTH) < 10 ? ("0" + old.get(Calendar.DAY_OF_MONTH)) : old
                            .get(Calendar.DAY_OF_MONTH)));
            // if (TimeUtils.compareTime(old)) {
            // viewHolder.tvTextDate
            // .setText((old.get(Calendar.HOUR_OF_DAY) < 10 ? ("0" + old.get(Calendar.HOUR_OF_DAY)) : old
            // .get(Calendar.HOUR_OF_DAY))
            // + ":"
            // + (old.get(Calendar.MINUTE) < 10 ? ("0" + old.get(Calendar.MINUTE)) : old
            // .get(Calendar.MINUTE)));
            // } else {
            // int t = old.get(Calendar.MONTH) + 1;
            // viewHolder.tvTextDate.setText((t < 10 ? ("0" + t) : t)
            // + "-"
            // + (old.get(Calendar.DAY_OF_MONTH) < 10 ? ("0" + old.get(Calendar.DAY_OF_MONTH)) : old
            // .get(Calendar.DAY_OF_MONTH)));
            // }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    // final static class ViewHodler {
    // TextView tvTextName;
    // TextView tvTextNameNum;
    // TextView tvTextDate;
    // TextView tvTextDates;
    // TextView tvTextFrom;
    // }

    final static class ViewHodler {
        TextView tvTextName;
        TextView tvStockName;
        TextView tvTextDate;
        TextView text;
        TextView tv;
        TextView tvType;
    }
}
