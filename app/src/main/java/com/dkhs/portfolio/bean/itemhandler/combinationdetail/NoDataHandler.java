package com.dkhs.portfolio.bean.itemhandler.combinationdetail;

import android.text.TextUtils;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.NoDataBean;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MoerHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/28.
 */
public class NoDataHandler extends SimpleItemHandler<NoDataBean> {
    @Override
    public int getLayoutResId() {
        return R.layout.layout_empty;
    }

    @Override
    public void onBindView(ViewHolder vh, NoDataBean data, int position) {

        if(data != null && !TextUtils.isEmpty(data.noData))

        vh.setTextView(R.id.tv_empty,data.noData);

    }
}
