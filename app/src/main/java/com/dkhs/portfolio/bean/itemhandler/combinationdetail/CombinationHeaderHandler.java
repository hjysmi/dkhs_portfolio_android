package com.dkhs.portfolio.bean.itemhandler.combinationdetail;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dkhs.adpter.handler.ItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.ui.FriendsOrFollowersActivity;
import com.dkhs.portfolio.ui.OptionalTabActivity;
import com.dkhs.portfolio.utils.StringFromatUtils;
import com.dkhs.portfolio.utils.WaterMarkUtil;
import com.lidroid.xutils.BitmapUtils;

/**
 * @author zwm
 * @version 2.0
 * @ClassName CombinationHeaderHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/7/28.
 */
public class CombinationHeaderHandler implements ItemHandler<UserEntity> ,View.OnClickListener{

    private UserEntity userEntity;

    @Override
    public int getLayoutResId() {
        return R.layout.layout_combination_headler;
    }

    @Override
    public void onBindView(ViewHolder vh, UserEntity data, int position) {
        BitmapUtils bitmapUtils = new BitmapUtils(vh.getContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_user_head);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_user_head);
        userEntity=data;
        if (null != data.getAvatar_md() && data.getAvatar_md().length() > 35) {
            bitmapUtils.display(vh.getImageView(R.id.iv_avatar), data.getAvatar_md());
        }
        //TODO 根据返回值判断加V图片的显示隐藏
        WaterMarkUtil.calWaterMarkImage(vh.getImageView(R.id.iv_water_mark),true,WaterMarkUtil.TYPE_RED);
        vh.getTextView(R.id.tv_user_name).setText(data.getUsername());
        if (TextUtils.isEmpty(data.getDescription())) {
            vh.getTextView(R.id.tv_user_desc).setText(vh.getContext().getResources().getString(R.string.nodata_user_description));
        } else {

            vh.getTextView(R.id.tv_user_desc).setText(data.getDescription());
        }
        handleNumber(  vh.getTextView(R.id.tv_followers), data.getFollowed_by_count());
        handleNumber(vh.getTextView(R.id.tv_following), data.getFriends_count());
        handleNumber(vh.getTextView(R.id.tv_symbols), data.getSymbols_count() + data.getPortfolios_following_count());
        vh.get(R.id.ll_followers).setOnClickListener(this);
        vh.get(R.id.ll_following).setOnClickListener(this);
        vh.get(R.id.ll_symbols).setOnClickListener(this);
    }

    private void handleNumber(TextView tv, int count) {
        tv.setText(StringFromatUtils.handleNumber(count));
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_followers:
                if (null == userEntity) {
                    return;
                }
                Intent intent1 = new Intent(v.getContext(), FriendsOrFollowersActivity.class);
                intent1.putExtra(FriendsOrFollowersActivity.KEY, FriendsOrFollowersActivity.FOLLOWER);
                intent1.putExtra(FriendsOrFollowersActivity.USER_ID, userEntity.getId() + "");
                v.getContext(). startActivity(intent1);
                break;
            case R.id.ll_following:
                if (null == userEntity) {
                    return;
                }
                Intent intent = new Intent(v.getContext(), FriendsOrFollowersActivity.class);
                intent.putExtra(FriendsOrFollowersActivity.KEY, FriendsOrFollowersActivity.FRIENDS);
                intent.putExtra(FriendsOrFollowersActivity.USER_ID, userEntity.getId() + "");
                v.getContext().startActivity(intent);
                break;
            case R.id.ll_symbols: {
                v.getContext(). startActivity(OptionalTabActivity.newIntent(  v.getContext(), userEntity.getId()+""));
            }
            break;
            default:
                break;
        }
    }
}
