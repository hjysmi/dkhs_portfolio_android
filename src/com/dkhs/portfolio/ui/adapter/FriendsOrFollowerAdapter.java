package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.lidroid.xutils.BitmapUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FriendsOrFollowerAdapter
 * @date 2015/4/23.14:07
 * @Description TODO(这里用一句话描述这个类的作用)
 */
public class FriendsOrFollowerAdapter extends AutoAdapter {
    private final BitmapUtils bitmapUtils;

    public FriendsOrFollowerAdapter(Context context, List<?> list) {
        super(context, list, R.layout.layout_friends_or_follower);
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public void getView33(int position, View v, ViewHolderUtils.ViewHolder vh) {

        PeopleBean peopleBean= (PeopleBean) list.get(position);

        if (null != peopleBean.getAvatar_md() && peopleBean.getAvatar_md().length() > 35) {
            bitmapUtils.display(vh.getImageView(R.id.im_avatar), peopleBean.getAvatar_md());
        }

        vh.setTextView(R.id.tv_user_name,peopleBean.getUsername());
        vh.setTextView(R.id.tv_followers,context.getResources().getString(R.string.followers)+":"+ StringFromatUtils.handleNumber(peopleBean.getFollowed_by_count()));
        vh.setTextView(R.id.tv_friends,context.getResources().getString(R.string.following)+":"+StringFromatUtils.handleNumber(peopleBean.getFriends_count()));

    }



}
