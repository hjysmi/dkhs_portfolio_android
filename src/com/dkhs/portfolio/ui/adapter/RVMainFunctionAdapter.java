/**
 * @Title RVMainFunctionAdapter.java
 * @Package com.dkhs.portfolio.ui.adapter
 * @Description TODO(用一句话描述该文件做什么)
 * @author zjz
 * @date 2015-1-19 上午10:53:44
 * @version V1.0
 */
package com.dkhs.portfolio.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;

/**
 * @ClassName RVMainFunctionAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author zjz
 * @date 2015-1-19 上午10:53:44
 * @version 1.0
 */
public class RVMainFunctionAdapter extends RecyclerView.Adapter<RVMainFunctionAdapter.ViewHolder> {

    private int[] titleRes = { R.string.function_combination, R.string.function_optional, R.string.function_market,
            R.string.function_notice, R.string.function_yanbao, R.string.function_qidai };
    private int[] iconRes = { R.drawable.bg_f_combination_selector, R.drawable.bg_f_optinal_selector,
            R.drawable.bg_f_market_selector, R.drawable.bg_f_notice_selector, R.drawable.bg_f_report_selector,
            R.drawable.ic_qidai };

    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return titleRes.length;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int postion) {
        viewHolder.tvTitle.setText(titleRes[postion]);
        viewHolder.ivIcon.setImageResource(iconRes[postion]);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int postion) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_main_function, null);
        // 创建一个Viewholder
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        ImageView ivIcon;

        public ViewHolder(View itemview) {
            super(itemview);
            tvTitle = (TextView) itemview.findViewById(R.id.tv_function_text);
            ivIcon = (ImageView) itemview.findViewById(R.id.iv_function_icon);
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != mItemClickListener) {
                mItemClickListener.onItemClick(v, getPosition());
            }

        }
    }

    OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
