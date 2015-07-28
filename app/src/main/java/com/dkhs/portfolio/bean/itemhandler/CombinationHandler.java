package com.dkhs.portfolio.bean.itemhandler;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;

/**
 * @author zwm
 * @version 2.0
 * @ClassName CombinationHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/27.
 */
public class CombinationHandler implements ItemHandler {
    @Override
    public int getLayoutResId() {
        return R.layout.item_combination;
    }

    @Override
    public void onBindView(ViewHolder vh, Object data, int position) {

    }
}
