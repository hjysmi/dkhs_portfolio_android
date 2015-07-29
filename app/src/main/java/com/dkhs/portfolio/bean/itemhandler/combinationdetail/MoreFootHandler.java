package com.dkhs.portfolio.bean.itemhandler.combinationdetail;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreFootBean;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MoreFootHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/28.
 */
public class MoreFootHandler implements ItemHandler<MoreFootBean> {
    @Override
    public int getLayoutResId() {
        return R.layout.item_more_foot;
    }

    @Override
    public void onBindView(ViewHolder vh, MoreFootBean data, int position) {

    }


}
