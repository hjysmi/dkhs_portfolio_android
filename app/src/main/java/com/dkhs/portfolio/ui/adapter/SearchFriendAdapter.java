package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.adapter.SingleAutoAdapter;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by zjz on 2015/9/1.
 */
public class SearchFriendAdapter extends SingleAutoAdapter {

    private final BitmapUtils bitmapUtils;

    public SearchFriendAdapter(Context context, List<?> data) {
        super(context, data);
        bitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_select_friend;
    }

    @Override
    public void onBindView(ViewHolder vh, Object data, int position) {

        UserEntity item = (UserEntity) data;
        vh.get(R.id.catalog).setVisibility(View.GONE);

        vh.getTextView(R.id.tv_username).setText(item.getUsername());

        if (null != item.getAvatar_md() && item.getAvatar_md().length() > 35) {
            bitmapUtils.display(vh.getImageView(R.id.iv_avatar), item.getAvatar_md(), R.drawable.ic_user_head, R.drawable.ic_user_head);
        } else {
            vh.getImageView(R.id.iv_avatar).setImageResource(R.drawable.ic_user_head);
        }

    }

    public void updateData(List<?> data){
        this.mData = data;
        notifyDataSetChanged();
    }
}
