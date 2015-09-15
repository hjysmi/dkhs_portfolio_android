package com.dkhs.portfolio.ui.ItemView;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.dkhs.adpter.handler.SimpleItemHandler;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;

import java.io.File;

public class TopicPicItemHandler extends SimpleItemHandler<String> {

    private Context mContext;

    public TopicPicItemHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.itemt_post_topic_pic;
    }

    @Override
    public void onBindView(ViewHolder vh, String data, int position) {

        if (!TextUtils.isEmpty(data) && data.equals("add_more")) {
            android.widget.ImageView icMore = vh.getImageView(R.id.iv_photo);
            icMore.setImageResource(R.drawable.bg_add_pic);
            icMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = PhotoPickerActivity.getIntent(this, selectedPhotos);
//                    startActivityForResult(intent, RCODE_PICK_PICTURE);
                }
            });

            vh.getImageView(R.id.iv_del_photo).setVisibility(View.GONE);
        } else {


            Uri uri = Uri.fromFile(new File(data));

            Glide.with(mContext)
                    .load(uri)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(me.iwf.photopicker.R.drawable.ic_photo_black_48dp)
                    .error(me.iwf.photopicker.R.drawable.ic_broken_image_black_48dp)
                    .into(vh.getImageView(R.id.iv_photo));
            vh.getImageView(R.id.iv_del_photo).setVisibility(View.VISIBLE);
//        vh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, PhotoPagerActivity.class);
//                intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, position);
//                intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, photoPaths);
//                if (mContext instanceof MainActivity) {
//                    ((MainActivity) mContext).previewPhoto(intent);
//                }
//            }
//        });

            vh.getImageView(R.id.iv_del_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

//        vh.setTextView(R.id.tv_name, invitationBean.getUsername());
//
//        vh.setTextView(R.id.tv_date, TimeUtils.getSimpleDay(invitationBean.getDate_joined()));
    }
}
