package com.dkhs.portfolio.ui.widget.ViewBean;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;

/**
 * Created by zhangcm on 2015/11/18.
 */
public class SelectTitleViewBean extends ViewBean{
    private static final int TYPE = 1;

    public SelectTitleViewBean() {
    }

    public SelectTitleViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }
    private int titleResId;

    public SelectTitleViewBean(int titleResId) {
        this.titleResId = titleResId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.item_select_title));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ((ViewHolder) itemHolder).bindView(titleResId);
        ViewUitls.fullSpanView(itemHolder);

    }


    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);

        }

        public void bindView(final int titleResId) {
            tvTitle.setText(titleResId);
        }


    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
