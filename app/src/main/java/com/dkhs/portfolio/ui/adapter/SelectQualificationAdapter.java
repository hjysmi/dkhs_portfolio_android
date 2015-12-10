package com.dkhs.portfolio.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.dkhs.adpter.adapter.SingleItemAdapter;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.QualificationEventBean;
import com.dkhs.portfolio.ui.eventbus.BusProvider;
import com.dkhs.portfolio.ui.fragment.QualificationFragment;

import java.io.File;
import java.util.List;

/**
 * Created by xuetong on 2015/12/8.
 */
public class SelectQualificationAdapter extends SingleItemAdapter<String> {
    private List<String> datas;

    public SelectQualificationAdapter(Context context, List<String> data) {
        super(context, data);
        datas = data;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_qualification_pic;
    }

    @Override
    public void onBindView(ViewHolder vh, final String data, int position) {
        if (!TextUtils.isEmpty(data) && data.equals(QualificationFragment.ADD_PICTURE)) {
            android.widget.ImageView icMore = vh.getImageView(R.id.iv_additem);
            icMore.setVisibility(View.VISIBLE);
            icMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusProvider.getInstance().post(new QualificationEventBean());
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
                    if(mData.size() == 0){
                        datas.add(QualificationFragment.ADD_PICTURE);
                    }
                    if (mData.size() > 0 && !mData.contains(QualificationFragment.ADD_PICTURE)) {
                        datas.add(QualificationFragment.ADD_PICTURE);
                    }
                    if (mData.size() == 1 && mData.contains(QualificationFragment.ADD_PICTURE)) {
                      //  mData.remove(QualificationFragment.ADD_PICTURE);
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
