package com.dkhs.portfolio.ui.adapter;

import android.content.Context;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.InvitationBean;
import com.dkhs.portfolio.utils.TimeUtils;
import com.dkhs.adpter.adapter.SingleAutoAdapter;
import com.dkhs.adpter.util.ViewHolder;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName InvitetionsAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/19.
 */
public class InvitationsAdapter extends SingleAutoAdapter {
    public InvitationsAdapter(Context context, List<?> list) {
        super(context, list);
    }


    @Override
    public int getLayoutResId() {
        return  R.layout.item_invite_history;
    }

    @Override
    public void onBindView(ViewHolder vh, Object data, int position) {
        InvitationBean invitationBean = (InvitationBean) mData.get(position);
        vh.setTextView(R.id.tv_name, invitationBean.getUsername());

        vh.setTextView(R.id.tv_date, TimeUtils.getSimpleDay(invitationBean.getDate_joined()));
    }
}
