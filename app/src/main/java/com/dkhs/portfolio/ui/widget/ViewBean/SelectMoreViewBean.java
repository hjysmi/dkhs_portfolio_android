package com.dkhs.portfolio.ui.widget.ViewBean;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SearchStockBean;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreType;
import com.dkhs.portfolio.ui.SelectSearchMoreLoadMoreListActivity;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zhangcm on 2015/11/18.
 */
public class SelectMoreViewBean extends ViewBean {
    private static final int TYPE = 3;

    private String searchString;
    private int moreTitle;
    private MoreType moreType;
    public SelectMoreViewBean(){}

    public SelectMoreViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    public SelectMoreViewBean(String searchString, int moreTitle, MoreType moreType) {
        this.searchString = searchString;
        this.moreTitle = moreTitle;
        this.moreType = moreType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.item_select_more));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ((ViewHolder) itemHolder).bindView(moreTitle, searchString, moreType);
        ViewUitls.fullSpanView(itemHolder);
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
        TextView tv_more;
        View view_divider;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_more = (TextView) itemView.findViewById(R.id.tv_more);
            view_divider = itemView.findViewById(R.id.view_divider);


        }

        public void bindView(SearchStockBean mSearchStockBean) {
        }

        public void bindView(int moreTitle, final String searchString, final MoreType moreType) {
            switch (moreType) {
                case MORE_STOCK:
                case MORE_FUND:
                case MORE_COMBINATION:
                    view_divider.setVisibility(View.GONE);
                    break;
                case MORE_FUND_MANAGER:
                case MORE_USER:
                    view_divider.setVisibility(View.VISIBLE);
                    break;
            }
            tv_more.setText(moreTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (moreType) {
                        case MORE_STOCK:
                            UIUtils.startAnimationActivity((Activity)itemView.getContext(), SelectSearchMoreLoadMoreListActivity.getIntent(itemView.getContext(),searchString, SearchMoreType.MORE_STOCK));
                            break;
                        case MORE_FUND:
                            UIUtils.startAnimationActivity((Activity)itemView.getContext(), SelectSearchMoreLoadMoreListActivity.getIntent(itemView.getContext(),searchString, SearchMoreType.MORE_FUND));
                            break;
                        case MORE_COMBINATION:
                            UIUtils.startAnimationActivity((Activity)itemView.getContext(), SelectSearchMoreLoadMoreListActivity.getIntent(itemView.getContext(),searchString, SearchMoreType.MORE_COMBINATION));
                            break;
                        case MORE_FUND_MANAGER:
                            UIUtils.startAnimationActivity((Activity)itemView.getContext(), SelectSearchMoreLoadMoreListActivity.getIntent(itemView.getContext(),searchString, SearchMoreType.MORE_FUND_MANAGER));
                            break;
                        case MORE_USER:
                            UIUtils.startAnimationActivity((Activity)itemView.getContext(), SelectSearchMoreLoadMoreListActivity.getIntent(itemView.getContext(),searchString, SearchMoreType.MORE_USER));
                            view_divider.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            });
        }
    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
