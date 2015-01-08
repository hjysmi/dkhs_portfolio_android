/**
 * @Title OptionalStockAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.PositionDetail.PositionAdjustBean;
import com.dkhs.portfolio.ui.widget.PositionItemView;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @ClassName OptionalStockAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version 1.0
 */
public class PositionAdjustHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<AdjustListBean> mDataList;

    class AdjustListBean {
        String time;
        LinearLayout contentView;

    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param activity
     * @param mAdjustList
     */
    public PositionAdjustHistoryAdapter(Context mContext, List<PositionAdjustBean> mAdjustList) {
        this.mContext = mContext;
        // this.mDataList = mAdjustList;
        convertPositionBeanToTimeFow(mAdjustList, mContext);
    }

    public void setList(List<PositionAdjustBean> datalist) {
        // this.mDataList = datalist;
        convertPositionBeanToTimeFow(datalist, mContext);
        notifyDataSetChanged();
    }

    private void convertPositionBeanToTimeFow(List<PositionAdjustBean> mAdjustList, Context context) {
        this.mDataList = new ArrayList<PositionAdjustHistoryAdapter.AdjustListBean>();
        if (null != mAdjustList) {

            for (PositionAdjustBean position : mAdjustList) {
                AdjustListBean bean = new AdjustListBean();
                if (isCreateItem(position)) {
                    // bean.content = position.get
                    bean.time = position.getModifyTime();
                    bean.contentView = new LinearLayout(mContext);
                    bean.contentView.setOrientation(LinearLayout.VERTICAL);
                    bean.contentView.addView(new PositionItemView(mContext, position).getContentView());
                    this.mDataList.add(bean);
                } else {
                    getLastBean().contentView.addView(new PositionItemView(mContext, position).getContentView());
                }
            }
        }
    }

    private AdjustListBean getLastBean() {
        int dataSize = this.mDataList.size();
        return this.mDataList.get(dataSize - 1);
    }

    private boolean isCreateItem(PositionAdjustBean position) {
        if (null == mDataList || mDataList.size() < 1) {
            return true;
        }
        return (!position.getModifyTime().equals(getLastBean().time));
    }

    @Override
    public int getCount() {
        if (null == mDataList) {
            return 0;
        }
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_position_adjust_history_new, null);
            viewHolder.tvAdjustTime = (TextView) convertView.findViewById(R.id.tv_adjust_time);
            viewHolder.llAdjustContent = (LinearLayout) convertView.findViewById(R.id.ll_adjust_history);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AdjustListBean item = mDataList.get(position);
        viewHolder.tvAdjustTime.setText(TimeUtils.getHourString(item.time));
        viewHolder.llAdjustContent.removeAllViews();
        viewHolder.llAdjustContent.addView(item.contentView);
        // viewHolder.colorView.setBackgroundColor(item.getDutyColor());

        return convertView;
    }

    public final static class ViewHolder {

        TextView tvAdjustTime;
        LinearLayout llAdjustContent;

    }

}
