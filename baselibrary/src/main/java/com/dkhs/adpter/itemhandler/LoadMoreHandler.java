package com.dkhs.adpter.itemhandler;

import com.dkhs.R;
import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;

/**
 * @author zwm
 * @version 2.0
 * @ClassName LoadMoreHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/17.
 */
public class LoadMoreHandler implements ItemHandler {
    @Override
    public int getLayoutResId() {
        return R.layout.layout_load_foot_view;
    }

    @Override
    public void onBindView(ViewHolder vh, Object data, int position) {

    }

    @Override
    public Class<?> getDataClass() {
        return null;
    }
}
