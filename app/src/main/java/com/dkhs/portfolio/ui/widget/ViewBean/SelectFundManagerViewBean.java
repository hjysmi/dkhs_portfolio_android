package com.dkhs.portfolio.ui.widget.ViewBean;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ImageView;
import com.dkhs.portfolio.bean.FundManagerBean;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.ui.FundManagerActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zhangcm on 2015/11/19.
 */
public class SelectFundManagerViewBean extends ViewBean {
    private static final int TYPE = 8;

    private FundManagerBean mFundManagerBean;
    public SelectFundManagerViewBean() {
    }
    public SelectFundManagerViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    public SelectFundManagerViewBean(FundManagerBean mFundManagerBean) {
        this.mFundManagerBean = mFundManagerBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.item_select_user_manager));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ((ViewHolder) itemHolder).bindView(mFundManagerBean);
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        TextView tv_user_name;
        ImageView user_head;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            user_head = (ImageView) itemView.findViewById(R.id.user_head);
        }

        public void bindView(final FundManagerBean mFundManagerBean) {
            tv_user_name.setText(mFundManagerBean.name);
            ImageLoaderUtils.setHeanderImage(mFundManagerBean.avatar_md, user_head);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.startAnimationActivity((Activity) itemView.getContext(), FundManagerActivity.newIntent((Activity) itemView.getContext(),mFundManagerBean));
                }
            });
        }
    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
