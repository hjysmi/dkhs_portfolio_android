package com.dkhs.portfolio.bean.itemhandler.homepage;

import android.content.Context;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;

/**
 * Created by xuetong on 2016/1/11.
 */
public class SpaceHandler implements ItemHandler {
    private Context context;

    public SpaceHandler(Context context) {
        this.context = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_homepage_space;
    }

    @Override
    public void onBindView(ViewHolder vh, Object data, int position) {

    }
}
