package com.dkhs.portfolio.bean.itemhandler.combinationdetail;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.LoadingBean;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MoerHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/28.
 */
public class LoadingHandler  implements ItemHandler<LoadingBean>{
    @Override
    public int getLayoutResId() {
        return R.layout.item_loading;
    }

    @Override
    public void onBindView(ViewHolder vh, LoadingBean data, int position) {

    }
}
