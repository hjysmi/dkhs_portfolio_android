package com.dkhs.portfolio.bean.itemhandler;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;

/**
 * Created by xuetong on 2016/1/7.
 */
public class FooterHandler implements ItemHandler {
    @Override
    public int getLayoutResId() {
        return R.layout.layout_userhomepage_footer;
    }

    @Override
    public void onBindView(ViewHolder vh, Object data, int position) {

    }
}
