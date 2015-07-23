/**
 * @Title MarketCenterGridAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2014-12-18 下午5:24:37
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.utils.ColorTemplate;
import com.dkhs.portfolio.utils.StringFromatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MarketCenterGridAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-12-18 下午5:24:37
 */
public class MarketPlateGridAdapter extends BaseAdapter {

    private List<SectorBean> mDataList;
    private LayoutInflater mInflater;

    // private GridView mGridView;
    // private int mCount = 0;
    // private boolean isPlate;
    public MarketPlateGridAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = new ArrayList<>();
    }

    public MarketPlateGridAdapter(Context context, List<SectorBean> datalist) {
        mInflater = LayoutInflater.from(context);
        this.mDataList = datalist;
        // this.isPlate = isplate;

    }

    public void setDataList(List<SectorBean> datalist) {
        this.mDataList = datalist;
    }

    @Override
    public int getCount() {
        if (mDataList.size() > 6) {
            return 6;
        }
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_mark_center, parent, false);
            mViewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
            mViewHolder.tvTitleName = (TextView) convertView.findViewById(R.id.tv_title_name);
            mViewHolder.tvCurrentValue = (TextView) convertView.findViewById(R.id.tv_main_value);
            mViewHolder.tvIncrease = (TextView) convertView.findViewById(R.id.tv_incease_value);
            mViewHolder.tvPercent = (TextView) convertView.findViewById(R.id.tv_incease_ratio);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        // if (isPlate) {
        mViewHolder.tvStockName.setVisibility(View.VISIBLE);
        // } else {
        //
        // mViewHolder.tvStockName.setVisibility(View.GONE);
        // }

        SectorBean item = mDataList.get(position);

        float change = item.getPercentage();
        mViewHolder.tvCurrentValue.setTextColor(ColorTemplate.getUpOrDrownCSL(change));
        mViewHolder.tvStockName.setText(item.getTop_symbol_name());
        mViewHolder.tvCurrentValue.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        mViewHolder.tvTitleName.setText(item.getAbbr_name());
        mViewHolder.tvCurrentValue.setText(StringFromatUtils.get2PointPercentPlus(item.getPercentage()));
        mViewHolder.tvPercent.setText(StringFromatUtils.get2PointPercentPlus(item.getTop_symbol_percentage()));
        mViewHolder.tvIncrease.setText(StringFromatUtils.get2Point(item.getTop_symbol_current()));

        // String path = hasHeaderIdList.get(position).getPath();
        // mViewHolder.mImageView.setTag(path);
        //
        // Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageCallBack() {
        //
        // @Override
        // public void onImageLoader(Bitmap bitmap, String path) {
        // ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
        // if (bitmap != null && mImageView != null) {
        // mImageView.setImageBitmap(bitmap);
        // }
        // }
        // });
        //
        // if (bitmap != null) {
        // mViewHolder.mImageView.setImageBitmap(bitmap);
        // } else {
        // mViewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        // }

        return convertView;
    }

    public static class ViewHolder {
        public ImageView mImageView;
        public TextView tvStockName;
        public TextView tvTitleName;
        public TextView tvCurrentValue;
        public TextView tvIncrease;
        public TextView tvPercent;

    }

    public static class HeaderViewHolder {
        public TextView mTextView;
    }

}
