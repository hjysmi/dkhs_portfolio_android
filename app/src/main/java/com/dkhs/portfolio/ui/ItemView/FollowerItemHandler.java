package com.dkhs.portfolio.ui.ItemView;

import android.content.Context;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.WaterMarkUtil;
import com.lidroid.xutils.BitmapUtils;

/**
 * @author zwm
 * @version 1.0
 * @ClassName FriendsOrFollowerAdapter
 * @date 2015/4/23.14:07
 * @Description TODO(这里用一句话描述这个类的作用)
 */
public class FollowerItemHandler extends SimpleItemHandler<PeopleBean> {

    private final BitmapUtils bitmapUtils;
    private Context mContext;

    public FollowerItemHandler(Context context) {
        this.mContext = context;
        bitmapUtils = new BitmapUtils(context);
    }


    @Override
    public int getLayoutResId() {
        return R.layout.layout_friends_or_follower;
    }

    @Override
    public void onBindView(ViewHolder vh, PeopleBean data, int position) {
        PeopleBean peopleBean = data;

        if (null != peopleBean.getAvatar_md() && peopleBean.getAvatar_md().length() > 35) {
            bitmapUtils.display(vh.getImageView(R.id.iv_avatar), peopleBean.getAvatar_md(), R.drawable.ic_user_head, R.drawable.ic_user_head);
        } else {
            vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_user_head);
        }
        WaterMarkUtil.calWaterMarkImage(vh.getImageView(R.id.iv_water_mark), peopleBean.verified, peopleBean.verified_type == UserEntity.VERIFIEDTYPE.EXPERT.getTypeid() ? WaterMarkUtil.TYPE_RED : WaterMarkUtil.TYPE_BLUE);

        vh.setTextView(R.id.tv_user_name, peopleBean.getUsername());
        vh.setTextView(R.id.tv_followers, mContext.getResources().getString(R.string.followers) + ":"
                + StringFromatUtils.handleNumber(peopleBean.getFollowed_by_count()));
        vh.setTextView(
                R.id.tv_friends,
                mContext.getResources().getString(R.string.following) + ":"
                        + StringFromatUtils.handleNumber(peopleBean.getFriends_count()));
    }


}
