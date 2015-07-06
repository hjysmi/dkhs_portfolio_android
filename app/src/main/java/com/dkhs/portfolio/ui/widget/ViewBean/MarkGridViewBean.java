package com.dkhs.portfolio.ui.widget.ViewBean;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MarkSectorBean;
import com.dkhs.portfolio.bean.MarkStockBean;
import com.dkhs.portfolio.bean.SectorBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.MarketListActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.adapter.MarketCenterGridAdapter;
import com.dkhs.portfolio.ui.adapter.MarketPlateGridAdapter;
import com.dkhs.portfolio.utils.AnimationHelper;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zjz on 2015/7/3.
 */
public class MarkGridViewBean extends ViewBean {
    private static final int TYPE = 2;

    private MarkStockBean mMarkStockBean;
    private MarkSectorBean mMarkSectorBean;

    public MarkGridViewBean() {
    }

    public MarkGridViewBean(MarkStockBean markStockBean) {
        this.mMarkStockBean = markStockBean;
    }

    public MarkGridViewBean(MarkSectorBean markSectorBean) {
        this.mMarkSectorBean = markSectorBean;
    }

    public MarkGridViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.layout_mark_grid), mViewPool);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private GridView gridView;
        private MarkIndexViewPool mViewPool;

        public ViewHolder(View itemView, MarkIndexViewPool mViewPool) {
            super(itemView);
            this.itemView = itemView;
            this.mViewPool = mViewPool;

            gridView = (GridView) itemView.findViewById(R.id.gridmarket);


        }

        public void bindView(final MarkSectorBean markSectorBean) {

            gridView.setAdapter(new MarketPlateGridAdapter(itemView.getContext(), markSectorBean.getResults()));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SectorBean bean = markSectorBean.getResults().get(position);
                    UIUtils.startAminationActivity((Activity) itemView.getContext(), MarketListActivity.newIntent(itemView.getContext(), MarketListActivity.LoadViewType.PlateList, bean.getId(),
                            bean.getAbbr_name()));


                }
            });
            setDefTransittion();

        }

        public void bindView(final MarkStockBean markStockBean) {
            gridView.setAdapter(new MarketCenterGridAdapter(itemView.getContext(), markStockBean.getResults()));
            setDefTransittion();

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SelectStockBean bean = SelectStockBean.copy(markStockBean.getResults().get(position));
                    UIUtils.startAminationActivity((Activity) itemView.getContext(), StockQuotesActivity.newIntent(itemView.getContext(), bean));


                }
            });

        }


        private void setDefTransittion() {
            LayoutTransition mTransitioner = new LayoutTransition();
            AnimationHelper.setupCustomAnimations(mTransitioner, this);
            gridView.setLayoutTransition(mTransitioner);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        if (mMarkSectorBean != null) {

            ((ViewHolder) itemHolder).bindView(mMarkSectorBean);
        } else {
            ((ViewHolder) itemHolder).bindView(mMarkStockBean);

        }
    }

    @Override
    public int getViewType() {
        return TYPE;
    }
}
