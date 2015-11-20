package com.dkhs.portfolio.ui.widget.ViewBean;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.SearchStockBean;

/**
 * Created by zhangcm on 2015/11/18.
 */
public class SelectCombinationViewBean extends ViewBean{
    private static final int TYPE = 2;
    private SearchStockBean mSearchStockBean;
    public SelectCombinationViewBean(SparseArray<ViewBean> viewDatas) {
        super(viewDatas);
    }
    public SelectCombinationViewBean(SearchStockBean mSearchStockBean) {
        this.mSearchStockBean = mSearchStockBean;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.item_select_stock_fund));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ((ViewHolder) itemHolder).bindView(mSearchStockBean);
        ViewUitls.fullSpanView(itemHolder);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        CheckBox cb_select_stock;
        TextView tv_stock_name;
        TextView tv_stock_num;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            cb_select_stock = (CheckBox) itemView.findViewById(R.id.cb_select_stock);
            tv_stock_name = (TextView) itemView.findViewById(R.id.tv_stock_name);
            tv_stock_num = (TextView) itemView.findViewById(R.id.tv_stock_num);

        }

        public void bindView(SearchStockBean mSearchStockBean) {
            tv_stock_name.setText(mSearchStockBean.getStockName());
            tv_stock_num.setText(String.valueOf(mSearchStockBean.getId()));
            cb_select_stock.setChecked(mSearchStockBean.isFollowed());
            cb_select_stock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){

                    }else{

                    }
                }
            });
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
