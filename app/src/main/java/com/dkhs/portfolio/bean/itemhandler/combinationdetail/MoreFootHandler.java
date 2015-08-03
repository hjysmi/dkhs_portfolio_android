package com.dkhs.portfolio.bean.itemhandler.combinationdetail;

import android.app.Activity;
import android.view.View;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreFootBean;
import com.dkhs.portfolio.ui.CombinationListActivity;
import com.dkhs.portfolio.ui.ReplyActivity;
import com.dkhs.portfolio.ui.UserTopicsActivity;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MoreFootHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/28.
 */
public class MoreFootHandler  extends SimpleItemHandler<MoreFootBean> implements View.OnClickListener {
    @Override
    public int getLayoutResId() {
        return R.layout.item_more_foot;
    }

    @Override
    public void onBindView(ViewHolder vh, MoreFootBean data, int position) {

        super.onBindView(vh, data, position);
        vh.get(R.id.moreBtn).setOnClickListener(this);
        vh.get(R.id.moreBtn).setTag(data);
    }


    @Override
    public void onClick(View v) {

        MoreFootBean moreFootBean= (MoreFootBean) v.getTag();

        if(moreFootBean.userEntity ==null ){
            return;
        }

        if(moreFootBean!= null){
            switch (moreFootBean.index){
                case 0:

                    //他的组合界面
                    CombinationListActivity.startActivity(mContext, moreFootBean.userEntity.getId() + "");
                    break;
                case 1:
                    //他的主贴界面

                    UserTopicsActivity.starActivity(mContext, moreFootBean.userEntity.getId() + "", moreFootBean.userEntity.getUsername());

                    break;
                case 2:
                    //他的回复
                    UIUtils.startAnimationActivity((Activity) mContext, ReplyActivity.getIntent(mContext, moreFootBean.userEntity.getId() + ""));
                    break;
            }
        }



    }
}
