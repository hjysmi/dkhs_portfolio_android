package com.dkhs.portfolio.bean.itemhandler.searchmoredetail;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;


/**
 * Created by zhangcm on 2015/11/20.
 */
public class SearchMoreTitleHandler extends SimpleItemHandler<String>{
    @Override
    public int getLayoutResId() {
        return R.layout.item_select_title;
    }

    @Override
    public void onBindView(ViewHolder vh, String data, int position) {
        vh.getTextView(R.id.tv_title).setText(data);
    }
}
