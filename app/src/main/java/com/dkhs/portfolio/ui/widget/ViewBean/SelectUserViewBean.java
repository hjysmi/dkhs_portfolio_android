package com.dkhs.portfolio.ui.widget.ViewBean;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.base.widget.ImageView;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.ui.UserHomePageActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zhangcm on 2015/11/18.
 */
public class SelectUserViewBean extends ViewBean {
    private static final int TYPE = 4;

    private UserEntity user;

    public SelectUserViewBean() {
    }

    public SelectUserViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    public SelectUserViewBean(UserEntity user) {
        this.user = user;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.item_select_user_manager));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ((ViewHolder) itemHolder).bindView(user);
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

        public void bindView(final UserEntity user) {
            tv_user_name.setText(user.getUsername());
            ImageLoaderUtils.setHeanderImage(user.getAvatar_md(), user_head);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.startAnimationActivity((Activity) itemView.getContext(), UserHomePageActivity.getIntent(itemView.getContext(), user.getUsername(), user.getId() + ""));
                }
            });
        }
    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
