package com.dkhs.portfolio.ui.adapter;

import android.content.Context;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.lidroid.xutils.BitmapUtils;
import com.dkhs.adpter.adapter.SingleAutoAdapter;
import com.dkhs.adpter.util.ViewHolder;

import java.util.List;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FriendsOrFollowerAdapter
 * @date 2015/4/23.14:07
 * @Description TODO(这里用一句话描述这个类的作用)
 */
public class FriendsOrFollowerAdapter extends SingleAutoAdapter {
    private final BitmapUtils bitmapUtils;

    public FriendsOrFollowerAdapter(Context context, List<?> list) {
        super(context, list);
        bitmapUtils = new BitmapUtils(context);
    }


    @Override
    public int getLayoutResId() {
        return  R.layout.layout_friends_or_follower;
    }

    @Override
    public void onBindView(ViewHolder vh, Object data, int position) {
        PeopleBean peopleBean = (PeopleBean) mData.get(position);

        if (null != peopleBean.getAvatar_md() && peopleBean.getAvatar_md().length() > 35) {
            bitmapUtils.display(vh.getImageView(R.id.im_avatar), peopleBean.getAvatar_md(), R.drawable.ic_user_head, R.drawable.ic_user_head);
        } else {
            vh.getImageView(R.id.im_avatar).setImageResource(R.drawable.ic_user_head);
        }

        vh.setTextView(R.id.tv_user_name, peopleBean.getUsername());
        vh.setTextView(R.id.tv_followers, mContext.getResources().getString(R.string.followers) + ":"
                + StringFromatUtils.handleNumber(peopleBean.getFollowed_by_count()));
        vh.setTextView(
                R.id.tv_friends,
                mContext.getResources().getString(R.string.following) + ":"
                        + StringFromatUtils.handleNumber(peopleBean.getFriends_count()));
    }
}
