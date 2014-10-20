/**
 * @Title MainFunctionAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-10-20 下午4:57:15
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;

/**
 * @ClassName MainFunctionAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-10-20 下午4:57:15
 * @version 1.0
 */
public class MainFunctionAdapter extends BaseAdapter {

    private Context mContext;
    private int[] titleRes = { R.string.function_ace, R.string.function_optional, R.string.function_notice,
            R.string.function_report, R.string.function_meeting, R.string.function_news };
    private int[] iconRes = { R.drawable.bg_f_ace_selector, R.drawable.bg_f_optinal_selector,
            R.drawable.bg_f_notice_selector, R.drawable.bg_f_report_selector, R.drawable.bg_f_meeting_selector,
            R.drawable.bg_f_news_selector };

    public MainFunctionAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @return
     * @return
     */
    @Override
    public int getCount() {
        return titleRes.length;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param position
     * @return
     * @return
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param position
     * @return
     * @return
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @Title
     * @Description TODO: (用一句话描述这个方法的功能)
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_main_function, null);

            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_function_text);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_function_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        viewHolder.tvTitle.setText(titleRes[position]);
        viewHolder.ivIcon.setImageResource(iconRes[position]);

        return convertView;
    }

    final static class ViewHodler {
        TextView tvTitle;
        ImageView ivIcon;

    }
}
