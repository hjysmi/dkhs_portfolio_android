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

public class OptionlistAdapter extends BaseAdapter {
    private Context mContext;
    private List<OptionNewsBean> mDataList;
    private DisplayMetrics dm;

    public OptionlistAdapter(Context mContext, List<OptionNewsBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        dm = new DisplayMetrics();
        WindowManager m = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        m.getDefaultDisplay().getMetrics(dm);
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
            ViewHodler viewHolder;
            OptionNewsBean mOptionNewsBean = mDataList.get(position);
            if (null == convertView) {
                viewHolder = new ViewHodler();
                convertView = View.inflate(mContext, R.layout.adapter_opition_list, null);
                viewHolder.tvTextName = (TextView) convertView.findViewById(R.id.adapter_market_title);
                viewHolder.tvTextNameNum = (TextView) convertView.findViewById(R.id.adapter_market_title_num);
                viewHolder.tvTextDate = (TextView) convertView.findViewById(R.id.option_news_text_date);
                viewHolder.zhengquan = (TextView) convertView.findViewById(R.id.zhengquan);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHodler) convertView.getTag();
            }
            Paint p = new Paint();
            Rect rect = new Rect();
            p.setTextSize(mContext.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
            p.getTextBounds(mOptionNewsBean.getTitle() + "正正正正正", 0, mOptionNewsBean.getTitle().length() + 5, rect);
            int soL = 0;
            if (null != mOptionNewsBean.getSource()) {
                soL = mOptionNewsBean.getSource().getTitle().length() + 2;
            }
            if (dm.widthPixels * 3 / 2 - 40 < rect.width()) {
                int le = (int) (mOptionNewsBean.getTitle().length() - mOptionNewsBean.getTitle().length() * (rect.width() - dm.widthPixels * 3 / 2 + 50) / rect.width() - 3);
                String text = mOptionNewsBean.getTitle().substring(0, le - soL);
                if (soL != 0) {
                    viewHolder.tvTextName.setText("[" + mOptionNewsBean.getSource().getTitle() + "]" + text + "...");
                } else {
                    viewHolder.tvTextName.setText(text + "...");
                }
            } else {
                viewHolder.tvTextName.setText("[" + mOptionNewsBean.getSource().getTitle() + "]" + mOptionNewsBean.getTitle());
            }
            //ViewTreeObserver observer = tv.getViewTreeObserver();
            viewHolder.tvTextNameNum.setText(mOptionNewsBean.getSymbols().get(0).getAbbrName());
            if (null != mOptionNewsBean.getSource()) {
                viewHolder.zhengquan.setText(mOptionNewsBean.getSource().getTitle());
            }


            if (TimeUtils.isSameDay(mOptionNewsBean.getPublish())) {
                viewHolder.tvTextDate.setText(TimeUtils.getTimeString(mOptionNewsBean.getPublish()));
            } else {
                viewHolder.tvTextDate.setText(TimeUtils.getMMDDString(mOptionNewsBean.getPublish()));
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
        TextView zhengquan;
    }
}
