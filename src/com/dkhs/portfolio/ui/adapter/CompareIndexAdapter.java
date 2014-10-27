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
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.ColorTemplate;

/**
 * @ClassName SelectFundAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version 1.0
 */
public class CompareIndexAdapter extends BaseAdapter {

    private Context mContext;
    private int mItemHeight = 0;
    private GridView.LayoutParams mItemViewLayoutParams;
    private List<CompareFundItem> mDataList;

    public CompareIndexAdapter(Context context, List<CompareFundItem> mCompareItemList) {
        this.mContext = context;
        this.mDataList = mCompareItemList;
        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

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
            convertView = View.inflate(mContext, R.layout.item_compare_index, null);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_comparename);
            viewHolder.tvValue = (TextView) convertView.findViewById(R.id.tv_compare_index);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        CompareFundItem item = mDataList.get(position);
        int colorId = ColorTemplate.DEFAULTCOLORS[position];
        if (item.iSelect) {
            colorId = R.color.theme_gray_press;
        }

        convertView.setLayoutParams(mItemViewLayoutParams);
        convertView.setBackgroundColor(mContext.getResources().getColor(colorId));

        viewHolder.tvName.setText(item.name);
        viewHolder.tvValue.setText(item.value);

        return convertView;
    }

    final static class ViewHodler {
        TextView tvName;
        TextView tvValue;
    }

    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
        notifyDataSetChanged();

    }

    public class CompareFundItem {
        public String name;
        public String value;
        public boolean iSelect;
    }

}
