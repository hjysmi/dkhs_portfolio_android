package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.dkhs.adpter.adapter.SingleItemAdapter;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.PostRewardActivity;
import com.dkhs.portfolio.ui.PostTopicActivity;

import java.io.File;
import java.util.List;

/**
 * Created by zjz on 2015/9/14.
 */
public class SelectPicAdapter extends SingleItemAdapter<String> {

    private List<String> datas;

    public SelectPicAdapter(Context context, List<String> data) {
        super(context, data);
        datas = data;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.itemt_post_topic_pic;
    }

    @Override
    public void onBindView(ViewHolder vh, final String data, int position) {

        if (!TextUtils.isEmpty(data) && data.equals(PostTopicActivity.ADD_PICTURE)) {
            android.widget.ImageView icMore = vh.getImageView(R.id.iv_additem);
            icMore.setVisibility(View.VISIBLE);
            icMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof PostTopicActivity) {
                        ((PostTopicActivity) mContext).pickMultiPicture();
                    }else if(mContext instanceof PostRewardActivity){
                        ((PostRewardActivity) mContext).pickMultiPicture();
                    }
                }
            });

            vh.getImageView(R.id.iv_del_photo).setVisibility(View.GONE);
            vh.getImageView(R.id.iv_photo).setVisibility(View.GONE);
        } else {


            Uri uri = Uri.fromFile(new File(data));

            Glide.with(mContext)
                    .load(uri)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.ic_img_thumbnail)
                    .error(R.drawable.ic_img_failure)
                    .into(vh.getImageView(R.id.iv_photo));
            vh.getImageView(R.id.iv_del_photo).setVisibility(View.VISIBLE);
            vh.getImageView(R.id.iv_photo).setVisibility(View.VISIBLE);
            vh.getImageView(R.id.iv_additem).setVisibility(View.GONE);


            vh.getImageView(R.id.iv_del_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mData.remove(data);
                    if (mData.size() > 0 && !mData.contains(PostTopicActivity.ADD_PICTURE)) {
                        datas.add(PostTopicActivity.ADD_PICTURE);
                    }
                    if (mData.size() == 1 && mData.contains(PostTopicActivity.ADD_PICTURE)) {
                        mData.remove(PostTopicActivity.ADD_PICTURE);
                        if (null != iDeletePicListenr) {
                            iDeletePicListenr.delFinish();
                        }
                    }
                    notifyDataSetChanged();
                }
            });

        }


    }

    private IDeletePicListenr iDeletePicListenr;

    public void setDeletePicListenr(IDeletePicListenr deletePicListenr) {
        this.iDeletePicListenr = deletePicListenr;
    }

    public interface IDeletePicListenr {
        void delFinish();
    }
}
