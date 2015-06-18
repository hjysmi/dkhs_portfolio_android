package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.InvitationBean;
import com.dkhs.portfolio.utils.TimeUtils;

import org.parceler.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author zwm
 * @version 2.0
 * @ClassName InvitetionsAdapter
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/6/19.
 */
public class InvitationsAdapter extends  AutoAdapter {
    public InvitationsAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public int setLayoutID() {
        return R.layout.item_invite_history;
    }

    @Override
    public void onViewCreated(int position, View v, ViewHolderUtils.ViewHolder vh) {


        InvitationBean invitationBean = (InvitationBean) list.get(position);
        vh.setTextView(R.id.tv_name, invitationBean.getUsername());

        vh.setTextView(R.id.tv_date, TimeUtils.getYYYYMMDDString(invitationBean.getDate_joined()));
    }



}
