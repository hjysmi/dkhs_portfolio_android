package com.dkhs.portfolio.ui.adapter;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.InvitationBean;
import com.dkhs.portfolio.utils.TimeUtils;

/**
 * @author zwm
 * @version 2.0
 * @ClassName InvitetionsAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/19.
 */
public class InvitationsItemHandler extends SimpleItemHandler<InvitationBean> {


    @Override
    public int getLayoutResId() {
        return R.layout.item_invite_history;
    }

    @Override
    public void onBindView(ViewHolder vh, InvitationBean data, int position) {
        InvitationBean invitationBean = data;
        vh.setTextView(R.id.tv_name, invitationBean.getUsername());

        vh.setTextView(R.id.tv_date, TimeUtils.getSimpleDay(invitationBean.getDate_joined()));
    }
}
