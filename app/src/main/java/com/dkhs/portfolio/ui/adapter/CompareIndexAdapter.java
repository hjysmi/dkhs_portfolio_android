/**
 * @Title SelectFundAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-8-28 下午3:36:46
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.ColorTemplate;

import org.parceler.Parcel;

import java.util.List;

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
        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mContext.getResources()
                .getDimensionPixelOffset(R.dimen.gridview_height));

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
            // viewHolder.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHodler) convertView.getTag();
        }

        CompareFundItem item = mDataList.get(position);
        int colorId = ColorTemplate.COMPARE[position];
        if (position + 1 == mDataList.size()) {
            colorId = R.color.blue_line;
        }
        // viewHolder.cbSelect.setBackgroundColor(mContext.getResources().getColor(colorId));
        // viewHolder.cbSelect.setOnCheckedChangeListener(itemSelectChangeListener);
        // viewHolder.cbSelect.setTag(position);
        if (item.iSelect && (!(position + 1 == mDataList.size()))) {
            colorId = R.color.compare_select_gray;
        }

        // convertView.setLayoutParams(mItemViewLayoutParams);
        convertView.setBackgroundColor(mContext.getResources().getColor(colorId));

        viewHolder.tvName.setText(item.name);
        viewHolder.tvValue.setText(item.value);

        return convertView;
    }

    OnCheckedChangeListener itemSelectChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (Integer) buttonView.getTag();
            if (isChecked) {

                buttonView.setBackgroundColor(mContext.getResources().getColor(R.color.compare_select_gray));
            } else {
                int colorId = ColorTemplate.COMPARE[position];
                buttonView.setBackgroundColor(mContext.getResources().getColor(colorId));

            }

        }
    };

    final static class ViewHodler {
        TextView tvName;
        TextView tvValue;
        public CheckBox cbSelect;
    }

    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mItemViewLayoutParams = new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
        notifyDataSetChanged();

    }

    @Parcel
    public static class CompareFundItem  {
        public String name;
        public String value;
        public boolean iSelect;

    }

}
