package com.dkhs.portfolio.bean.itemhandler.searchmoredetail;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.app.PortfolioApplication;
import com.dkhs.portfolio.bean.QuotesBean;
import com.dkhs.portfolio.bean.SelectStockBean;
import com.dkhs.portfolio.ui.FundDetailActivity;
import com.dkhs.portfolio.ui.StockQuotesActivity;
import com.dkhs.portfolio.ui.widget.ChangeFollowView;
import com.dkhs.portfolio.utils.NetUtil;
import com.dkhs.portfolio.utils.PromptManager;
import com.dkhs.portfolio.utils.StockUitls;
import com.dkhs.portfolio.utils.UIUtils;


/**
 * Created by zhangcm on 2015/11/20.
 */
public class SearchMoreStockFundHandler extends SimpleItemHandler<QuotesBean>{
    private ChangeFollowView changeFollowView;

    public SearchMoreStockFundHandler(Context context){
        this.changeFollowView = new ChangeFollowView(context);
    }
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

    @Override
    public int getLayoutResId() {
        return R.layout.item_select_stock_fund;
    }
    @Override
    public void onBindView(final ViewHolder vh, final QuotesBean mQuotesBean, int position) {
        final View itemView = vh.getConvertView();
        vh.getTextView(R.id.tv_stock_name).setText(mQuotesBean.getAbbrName());
        vh.getTextView(R.id.tv_stock_num).setText(String.valueOf(mQuotesBean.getSymbol()));
        CheckBox cb_select_stock = vh.getCheckBox(R.id.cb_select_stock);
        cb_select_stock.setOnCheckedChangeListener(null);
        cb_select_stock.setChecked(mQuotesBean.isFollowed());
        SelectStockBean stockBean = SelectStockBean.copy(mQuotesBean);
        cb_select_stock.setTag(stockBean);
        cb_select_stock.setOnCheckedChangeListener(onCheckedChangeListener);
        vh.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectStockBean itemStock = SelectStockBean.copy(mQuotesBean);
                if (StockUitls.isFundType(itemStock.symbol_type)) {
                    UIUtils.startAnimationActivity((Activity) itemView.getContext(), FundDetailActivity.newIntent((Activity) itemView.getContext(), itemStock));
                } else {
                    UIUtils.startAnimationActivity((Activity) itemView.getContext(), StockQuotesActivity.newIntent((Activity) itemView.getContext(), itemStock));
                }
            }
        });
    }
}
