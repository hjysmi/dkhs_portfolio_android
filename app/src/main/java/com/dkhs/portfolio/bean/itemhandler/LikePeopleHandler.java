package com.dkhs.portfolio.bean.itemhandler;

import android.text.TextUtils;
import android.view.View;

import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.PeopleBean;
import com.dkhs.portfolio.bean.UserEntity;
import com.dkhs.portfolio.ui.UserHomePageActivity;

/**
 * @author zwm
 * @version 2.0
 * @ClassName LikePeopeleHanlder
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/8/27.
 */
public class LikePeopleHandler extends SimpleItemHandler<UserEntity>  {


    @Override
    public int getLayoutResId() {
        return R.layout.layout_like;
    }

    @Override
    public void onBindView(ViewHolder vh, final UserEntity data, int position) {
        super.onBindView(vh, data, position);


        if (TextUtils.isEmpty(data.getAvatar_md())) {

            vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.default_head);

        } else {
            com.dkhs.portfolio.utils.ImageLoaderUtils.setHeanderImage(data.getAvatar_md(), vh.getImageView(R.id.iv_avatar));
        }

        vh.getTextView(R.id.flowedCount).setText("粉丝:" + data.getFollowed_by_count());
        vh.getTextView(R.id.flowingCount).setText("关注:" + data.getFriends_count());


        vh.getTextView(R.id.name).setText(data.getUsername());

        vh.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mContext.startActivity(
                            UserHomePageActivity.getIntent(mContext, data.getUsername(), data.getId() + ""));

            }
        });
    }


}