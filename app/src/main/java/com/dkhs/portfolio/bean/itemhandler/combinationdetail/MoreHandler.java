package com.dkhs.portfolio.bean.itemhandler.combinationdetail;

import android.app.Activity;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.MoreBean;
import com.dkhs.portfolio.ui.CombinationListActivity;
import com.dkhs.portfolio.ui.ReplyActivity;
import com.dkhs.portfolio.ui.UserTopicsActivity;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.utils.UIUtils;

/**
 * @author zwm
 * @version 2.0
 * @ClassName MoerHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/28.
 */
public class MoreHandler extends SimpleItemHandler<MoreBean> implements View.OnClickListener {
    @Override
    public int getLayoutResId() {
        return R.layout.item_more_ll;
    }

    @Override
    public void onBindView(ViewHolder vh, final MoreBean data, int position) {
        super.onBindView(vh, data, position);
        vh.setTextView(R.id.leftTv, data.title);
        vh.get(R.id.moreBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(data);
            }
        });
        vh.get(R.id.moreBtn).setOnClickListener(this);
        vh.get(R.id.moreBtn).setTag(data);

        if(data.index==0){
            vh.get(R.id.moreBtn).setVisibility(View.GONE);
        }else{
            vh.get(R.id.moreBtn).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        MoreBean moreBean = (MoreBean) v.getTag();

        if (moreBean.userEntity == null) {
            return;
        }
        switch (moreBean.index) {
            case 0:

                //他的组合界面
                CombinationListActivity.startActivity(mContext, moreBean.userEntity.getId() + "");
                break;
            case 1:
                //他的主贴界面
                UserTopicsActivity.starActivity(mContext, moreBean.userEntity.getId() + "", moreBean.userEntity.getUsername());

                break;
            case 2:
                //他的回复

                UIUtils.startAnimationActivity((Activity) mContext, ReplyActivity.getIntent(mContext, moreBean.userEntity.getId() + ""));


                break;
        }


    }
}
