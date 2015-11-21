package com.dkhs.portfolio.ui.widget.ViewBean;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.itemhandler.searchmoredetail.SearchMoreType;
import com.dkhs.portfolio.ui.SelectSearchMoreLoadMoreListActivity;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zhangcm on 2015/11/18.
 */
public class SelectRelatedViewBean extends ViewBean {
    private static final int TYPE = 6;

    private int relatedTitle;
    private String searchString;
    private RelatedType relatedTypeType;

    public SelectRelatedViewBean() {
    }

    public SelectRelatedViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    public SelectRelatedViewBean(String searchString, int relatedTitle, RelatedType relatedTypeType) {
        this.searchString = searchString;
        this.relatedTitle = relatedTitle;
        this.relatedTypeType = relatedTypeType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.item_select_related));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ((ViewHolder) itemHolder).bindView(searchString, relatedTitle, relatedTypeType);
        ViewUitls.fullSpanView(itemHolder);
    }

    public enum RelatedType {
        //搜索相关悬赏
        RELATED_REWARD,
        //搜索相关话题
        RELATED_TOPIC
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        TextView tv_related;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_related = (TextView) itemView.findViewById(R.id.tv_related);
        }

        public void bindView(final String searchString, int relatedTitle, final RelatedType relatedTypeType) {
            tv_related.setText(relatedTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (relatedTypeType){
                        case RELATED_REWARD:
                        UIUtils.startAnimationActivity((Activity) itemView.getContext(), SelectSearchMoreLoadMoreListActivity.getIntent(itemView.getContext(), searchString, SearchMoreType.MORE_REWARD));
                            break;
                        case RELATED_TOPIC:
                            UIUtils.startAnimationActivity((Activity) itemView.getContext(), SelectSearchMoreLoadMoreListActivity.getIntent(itemView.getContext(), searchString, SearchMoreType.MORE_TOPIC));
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
