package com.dkhs.portfolio.ui.widget.ViewBean;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.bean.UserEntity;

/**
 * Created by zhangcm on 2015/11/18.
 */
public class SelectFundManagerViewBean extends ViewBean {
    private static final int TYPE = 4;

    private UserEntity user;
    public SelectFundManagerViewBean() {
    }
    public SelectFundManagerViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    public SelectFundManagerViewBean(UserEntity user) {
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

    public enum MoreType {
        //更多股票
        MORE_STOCK,
        //更多基金
        MORE_FUND,
        //更多基金经理
        MORE_FUND_MANAGER,
        //更多用户
        MORE_USER,
        //更多组合
        MORE_COMBINATION
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        TextView tv_stock_name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_stock_name = (TextView) itemView.findViewById(R.id.tv_stock_name);

        }

        public void bindView(SearchStockBean mSearchStockBean) {
        }

        public void bindView(UserEntity user) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UIUtils.startAnimationActivity((Activity)itemView.getContext());
                }
            });
        }
    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
