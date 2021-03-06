/**
 * @Title OptionalStockAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-25 下午3:35:49
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.PositionAdjustBean;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.TimeUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName OptionalStockAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-8-25 下午3:35:49
 */
public class AdjustHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<AdjustListBean> mDataList;

    class AdjustListBean {
        String time;
        String content;

    }

    // public AdjustHistoryAdapter(Context mActivity) {
    // this.mActivity = mActivity;
    //
    // }

    /**
     * @param mAdjustList
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     */
    public AdjustHistoryAdapter(Context mContext, List<PositionAdjustBean> mAdjustList) {
        this.mContext = mContext;
        // this.mDataList = mAdjustList;
        convertPositionBeanToTimeFow(mAdjustList);
    }

    public void setList(List<PositionAdjustBean> datalist) {
        // this.mDataList = datalist;
        convertPositionBeanToTimeFow(datalist);
        notifyDataSetChanged();
    }

    private void convertPositionBeanToTimeFow(List<PositionAdjustBean> mAdjustList) {
        this.mDataList = new ArrayList<AdjustHistoryAdapter.AdjustListBean>();
        if (null != mAdjustList) {

            for (PositionAdjustBean position : mAdjustList) {
                if (isCreateItem(position)) {
                    AdjustListBean bean = new AdjustListBean();
                    // bean.content = position.get
                    bean.time = position.getModifyTime();
                    StringBuilder sb = new StringBuilder();
//                    sb.append(String.format("%-6s", position.getStockName()));
//                    // sb.append(position.getStockName());
//                    if (position.getStockName().length() < 4) {
//                        sb.append("   ");
//                    }
                    sb.append(position.getStockName());
                    try {
                        byte[] bytes = position.getStockName().getBytes("UTF-8");
                        switch (bytes.length) {
                            case 6:
                                sb.append("        ");
                                break;
                            case 7:
                            case 8:
                            case 9:
                                sb.append("     ");
                                break;
                            case 10:
                            case 11:
                                sb.append("  ");
                                break;

                            default:
                                break;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (position.getStockName().contains("*")) {
                        sb.append("\t从");
                    } else {
                        sb.append("\t\t从");
                    }
                    // int percent = (int) (position.getFromPercent());
                    sb.append(StringFromatUtils.get2PointPercent(position.getFromPercent()));
                    // sb.append("%");
                    sb.append("调至");
                    sb.append(StringFromatUtils.get2PointPercent(position.getToPercent()));

                    // sb.append((int) (position.getToPercent()) + "%");
                    bean.content = sb.toString();
                    this.mDataList.add(bean);
                } else {

                    StringBuilder sb = new StringBuilder(getLastBean().content + "\n");
//                    sb.append(String.format("%-6s", position.getStockName()));
//                    // sb.append(position.getStockName());
//                    if (position.getStockName().length() < 4) {
//                        sb.append("   ");
//                    }
//                    sb.append(" 从");
                    sb.append(position.getStockName());
                    try {
                        byte[] bytes = position.getStockName().getBytes("UTF-8");
                        switch (bytes.length) {
                            case 6:
                                sb.append("        ");
                                break;
                            case 7:
                            case 8:
                            case 9:
                                sb.append("     ");

                                break;
                            case 10:
                            case 11:
                                sb.append("  ");
                                break;

                            default:
                                break;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if (position.getStockName().contains("*")) {
                        sb.append("\t从");
                    } else {
                        sb.append("\t\t从");
                    }
                    // int percent = (int) (position.getFromPercent());
                    // sb.append(percent);
                    // sb.append("%");
                    sb.append(StringFromatUtils.get2PointPercent(position.getFromPercent()));
                    sb.append("调至");
                    sb.append(StringFromatUtils.get2PointPercent(position.getToPercent()));
                    // sb.append((int) (position.getToPercent()) + "%");
                    getLastBean().content = sb.toString();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_position_adjust_history, parent, false);
            viewHolder.tvAdjustTime = (TextView) convertView.findViewById(R.id.tv_adjust_time);
            viewHolder.tvAdjustContent = (TextView) convertView.findViewById(R.id.tv_adjust_history);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AdjustListBean item = mDataList.get(position);
        viewHolder.tvAdjustTime.setText(TimeUtils.getHourString(item.time));
        viewHolder.tvAdjustContent.setText(item.content);
        // viewHolder.colorView.setBackgroundColor(item.getDutyColor());

        return convertView;
    }

    public final static class ViewHolder {

        TextView tvAdjustTime;
        TextView tvAdjustContent;

    }

}
