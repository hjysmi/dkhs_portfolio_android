package com.dkhs.portfolio.ui.widget.ViewBean;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.QuotesBean;
import com.dkhs.portfolio.bean.SearchHistoryBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.engine.VisitorDataEngine;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.widget.ChangeFollowView;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * Created by zhangcm on 2015/11/18.
 */
public class SelectStockFundViewBean extends ViewBean {
    private static final int TYPE = 2;
    private static ChangeFollowView changeFollowView;
    private QuotesBean mQuotesBean;

    public SelectStockFundViewBean() {
    }

    public SelectStockFundViewBean(SparseArray<ViewBean> viewDatas, ChangeFollowView changeFollowView) {
        super(viewDatas);
        this.changeFollowView = changeFollowView;
    }

    public SelectStockFundViewBean(QuotesBean mQuotesBean) {
        this.mQuotesBean = mQuotesBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup container, MarkIndexViewPool mViewPool) {
        return new ViewHolder(inflate(container, R.layout.item_select_stock_fund));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder itemHolder) {
        ((ViewHolder) itemHolder).bindView(mQuotesBean);
        ViewUitls.fullSpanView(itemHolder);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        CheckBox cb_select_stock;
        TextView tv_stock_name;
        TextView tv_stock_num;
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SelectStockBean csBean = (SelectStockBean) buttonView.getTag();
                if (!NetUtil.checkNetWork() && PortfolioApplication.hasUserLogin()) {
                    buttonView.setChecked(!isChecked);
                    PromptManager.showNoNetWork();
                } else if (changeFollowView != null && csBean != null) {
                    changeFollowView.changeFollowNoDialog(csBean);
                }
            }
        };

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            cb_select_stock = (CheckBox) itemView.findViewById(R.id.cb_select_stock);
            tv_stock_name = (TextView) itemView.findViewById(R.id.tv_stock_name);
            tv_stock_num = (TextView) itemView.findViewById(R.id.tv_stock_num);
        }

        public void bindView(final QuotesBean mQuotesBean) {
            tv_stock_name.setText(mQuotesBean.getAbbrName());
            tv_stock_num.setText(String.valueOf(mQuotesBean.getSymbol()));
            cb_select_stock.setOnCheckedChangeListener(null);
            cb_select_stock.setChecked(mQuotesBean.isFollowed());
            SelectStockBean stockBean = SelectStockBean.copy(mQuotesBean);
            cb_select_stock.setTag(stockBean);
            cb_select_stock.setOnCheckedChangeListener(onCheckedChangeListener);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectStockBean itemStock = SelectStockBean.copy(mQuotesBean);
                    VisitorDataEngine.saveHistory(itemStock.parseHistoryBean());
                    BusProvider.getInstance().post(itemStock);
                    if (StockUitls.isFundType(itemStock.symbol_type)) {
                        UIUtils.startAnimationActivity((Activity) itemView.getContext(), FundDetailActivity.newIntent((Activity) itemView.getContext(), itemStock));
                    } else {
                        UIUtils.startAnimationActivity((Activity) itemView.getContext(), StockQuotesActivity.newIntent((Activity) itemView.getContext(), itemStock));
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
