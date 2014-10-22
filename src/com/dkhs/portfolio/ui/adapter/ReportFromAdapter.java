/**
 * @Title SelectFundAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.NetValueReportBean;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

/**
 * @ClassName SelectFundAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version 1.0
 */
public class ReportFromAdapter extends BaseAdapter {

    private Context mContext;
    private List<NetValueReportBean> mDataList;

    public ReportFromAdapter(Context context, List<NetValueReportBean> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

  
    @Override
    public int getCount() {

        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_report_form, null);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_report_date);
            viewHolder.tvNetValue = (TextView) convertView.findViewById(R.id.tv_report_netvalue);
            viewHolder.tvDayUP = (TextView) convertView.findViewById(R.id.tv_report_dayup);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        NetValueReportBean bean = mDataList.get(position);
        viewHolder.tvDate.setText(bean.getDate());
        viewHolder.tvNetValue.setText(StringFromatUtils.get4Point(bean.getNetValue()));
        viewHolder.tvDayUP.setTextColor(ColorTemplate.getUpOrDrownCSL(bean.getPercentage()));
        viewHolder.tvDayUP.setText(StringFromatUtils.getPercentValue(bean.getPercentage()));
        // if (bean.getNetValue() < 1) {
        // viewHolder.tvDayUP.setTextColor(ColorTemplate.DEF_GREEN);
        // } else {
        //
        // viewHolder.tvDayUP.setTextColor(ColorTemplate.DEF_RED);
        // }

        return convertView;
    }

    final static class ViewHodler {
        TextView tvDate;
        TextView tvNetValue;
        TextView tvDayUP;
    }
}
