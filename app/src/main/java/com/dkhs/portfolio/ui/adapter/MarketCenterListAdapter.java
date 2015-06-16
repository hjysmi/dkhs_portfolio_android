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
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MarketCenterGridItem;

import java.util.List;

/**
 * @author zjz
 * @version 1.0
 * @ClassName MarketCenterGridAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2014-12-18 下午5:24:37
 */
public class MarketCenterListAdapter extends BaseAdapter {

    private List<MarketCenterGridItem> hasHeaderIdList;
    private LayoutInflater mInflater;
    // private GridView mGridView;
    private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象

    public MarketCenterListAdapter(Context context, List<MarketCenterGridItem> hasHeaderIdList) {
        mInflater = LayoutInflater.from(context);
        // this.mGridView = mGridView;
        this.hasHeaderIdList = hasHeaderIdList;
    }

    @Override
    public int getCount() {
        // return hasHeaderIdList.size();
        return 30;
    }

    @Override
    public Object getItem(int position) {
        return hasHeaderIdList.get(position);

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
            convertView = mInflater.inflate(R.layout.item_optional_stock_price, parent, false);
            // mViewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.grid_item);
            convertView.setTag(mViewHolder);
            //
            // // 用来监听ImageView的宽和高
            // mViewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
            //
            // @Override
            // public void onMeasureSize(int width, int height) {
            // mPoint.set(width, height);
            // }
            // });

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

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

    // @Override
    // public View getHeaderView(int position, View convertView, ViewGroup parent) {
    // HeaderViewHolder mHeaderHolder;
    //
    // if (convertView == null) {
    // mHeaderHolder = new HeaderViewHolder();
    // convertView = mInflater.inflate(R.layout.layout_marketcenter_header, parent, false);
    // // mHeaderHolder.mTextView = (TextView) convertView.findViewById(R.id.header);
    // convertView.setTag(mHeaderHolder);
    // } else {
    // mHeaderHolder = (HeaderViewHolder) convertView.getTag();
    // }
    // // mHeaderHolder.mTextView.setText(hasHeaderIdList.get(position).getTime());
    //
    // return convertView;
    // }
    //
    // /**
    // * 获取HeaderId, 只要HeaderId不相等就添加一个Header
    // */
    // @Override
    // public long getHeaderId(int position) {
    // return hasHeaderIdList.get(position).getHeaderId();
    // }

    public static class ViewHolder {
        public ImageView mImageView;
    }

    public static class HeaderViewHolder {
        public TextView mTextView;
    }

}
