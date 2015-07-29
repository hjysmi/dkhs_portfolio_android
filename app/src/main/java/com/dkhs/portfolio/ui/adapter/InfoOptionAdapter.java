package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.OptionNewsBean;
import com.dkhs.portfolio.utils.TimeUtils;

import java.util.List;

public class InfoOptionAdapter extends BaseAdapter {
    private Context mContext;
    private List<OptionNewsBean> mDataList;
    private boolean isSecondNotice;

    public InfoOptionAdapter(Context mContext, List<OptionNewsBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
//        DisplayMetrics dm = new DisplayMetrics();
//        WindowManager m = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
//        m.getDefaultDisplay().getMetrics(dm);
//        Paint p = new Paint();
//        Rect rect = new Rect();
//        String text = "正正正正正";
//        p.setTextSize(mActivity.getResources().getDimensionPixelOffset(R.dimen.list_text_size));
//        p.getTextBounds(text, 0, text.length(), rect);
    }

    public void setSecondNotice(boolean isSecondNotice) {
        this.isSecondNotice = isSecondNotice;
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
        try {
            OptionNewsBean mOptionNewsBean = mDataList.get(position);
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

            switch (mOptionNewsBean.getContentType()) {
                case "20":
                    if (isSecondNotice) {
                        viewHolder.tvStockName.setVisibility(View.GONE);
                        viewHolder.tvType.setVisibility(View.GONE);
                    } else {

                        viewHolder.tvType.setText("【公告】");
                    }
                    viewHolder.text.setVisibility(View.GONE);
                    break;
                case "30":

                    viewHolder.tvType.setText("【研报】");
                    viewHolder.text.setVisibility(View.VISIBLE);
                    break;
                default:
                    viewHolder.tvType.setText("");
                    break;
            }

            viewHolder.tv.setText(mOptionNewsBean.getTitle());
            viewHolder.tvStockName.setText(mOptionNewsBean.getSymbols().get(0).getAbbrName());

            if (null != mOptionNewsBean.getSource()) {
                viewHolder.text.setText(mOptionNewsBean.getSource().getTitle());
            }
            viewHolder.tvTextDate.setText(TimeUtils.getMMDDString(mOptionNewsBean.getPublish()));
//
//            Calendar old = TimeUtils.toCalendarAddHour(mOptionNewsBean.getPublish());
//            int t = old.get(Calendar.MONTH) + 1;
//            viewHolder.tvTextDate.setText((t < 10 ? ("0" + t) : t)
//                    + "-"
//                    + (old.get(Calendar.DAY_OF_MONTH) < 10 ? ("0" + old.get(Calendar.DAY_OF_MONTH)) : old
//                    .get(Calendar.DAY_OF_MONTH)));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertView;
    }

    final static class ViewHodler {
        TextView tvTextName;
        TextView tvStockName;
        TextView tvTextDate;
        TextView text;
        TextView tv;
        TextView tvType;
    }
}
