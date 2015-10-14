package com.dkhs.portfolio.bean.itemhandler;

import android.content.Context;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.WalletChangeBean;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * Created by wuyongsen on 2015/10/14.
 */
public class WalletChangeHandler extends SimpleItemHandler<WalletChangeBean> {
    private Context mContext;
    @Override
    public int getLayoutResId() {
        return R.layout.item_wallet_change;
    }

    public WalletChangeHandler(Context context){
        mContext = context;
    }
    @Override
    public void onBindView(ViewHolder vh, WalletChangeBean data, int position) {
        String source = data.getChange_source().name;
        float change = data.getChange();
        vh.getTextView(R.id.tv_op).setText(source);
        if(change > 0){
            vh.getTextView(R.id.tv_amount).setText("+" + change);
            vh.getTextView(R.id.tv_amount).setTextColor(mContext.getResources().getColor(R.color.balance_in));
        }else{
            vh.getTextView(R.id.tv_amount).setText("" + change);
            vh.getTextView(R.id.tv_amount).setTextColor(mContext.getResources().getColor(R.color.balance_out));
        }
        vh.getTextView(R.id.tv_op_date).setText(TimeUtils.getDaySecondWITHOUTYEARString(data.getModified_at()));
        super.onBindView(vh, data, position);
    }
}
