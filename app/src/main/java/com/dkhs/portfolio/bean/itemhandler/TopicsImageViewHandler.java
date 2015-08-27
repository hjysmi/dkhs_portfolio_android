package com.dkhs.portfolio.bean.itemhandler;

import android.content.Context;
import android.view.View;

import com.dkhs.adpter.handler.ItemHandlerClickListenerImp;
import com.dkhs.adpter.util.ViewHolder;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.LikeBean;
import com.dkhs.portfolio.ui.PhotoViewActivity;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.mingle.bean.PhotoBean;

import java.util.ArrayList;

/**
 * @author zwm
 * @version 2.0
 * @ClassName TopicsImageViewHandler
 * @Description TODO(这里用一句话描述这个类的作用)
 * @date 2015/8/27.
 */
public class TopicsImageViewHandler {




    public  void  handleMedias(ViewHolder vh, LikeBean data){

        vh.getImageView(R.id.v1).setVisibility(View.INVISIBLE);
        vh.getImageView(R.id.v2).setVisibility(View.INVISIBLE);
        vh.getImageView(R.id.v3).setVisibility(View.INVISIBLE);
        vh.getImageView(R.id.v4).setVisibility(View.INVISIBLE);
        vh.getImageView(R.id.v5).setVisibility(View.INVISIBLE);
        vh.getImageView(R.id.v6).setVisibility(View.INVISIBLE);
        vh.getImageView(R.id.v7).setVisibility(View.INVISIBLE);
        vh.getImageView(R.id.v8).setVisibility(View.INVISIBLE);
        vh.getImageView(R.id.v9).setVisibility(View.INVISIBLE);
        vh.getImageView(R.id.iv).setVisibility(View.INVISIBLE);
        vh.get(R.id.row1).setVisibility(View.GONE);
        vh.get(R.id.row2).setVisibility(View.GONE);
        vh.get(R.id.row3).setVisibility(View.GONE);

        if (data.medias != null && data.medias.size() > 0) {
            vh.get(R.id.iv).setVisibility(View.VISIBLE);

            if(data.medias.size()==1) {
                ImageLoaderUtils.setImagDefault(data.medias.get(0).getImage_sm(), vh.getImageView(R.id.iv));
            }else{
                vh.getImageView(R.id.iv).setVisibility(View.GONE);

                if(data.medias.size()==4){
                    vh.get(R.id.row1).setVisibility(View.VISIBLE);
                    vh.get(R.id.row2).setVisibility(View.VISIBLE);
                    ImageLoaderUtils.setImagDefault(data.medias.get(0).getImage_sm(), vh.getImageView(R.id.v1));
                    vh.getImageView(R.id.v1).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,1));
                    ImageLoaderUtils.setImagDefault(data.medias.get(1).getImage_sm(), vh.getImageView(R.id.v2));
                    vh.getImageView(R.id.v2).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,2));
                    ImageLoaderUtils.setImagDefault(data.medias.get(2).getImage_sm(), vh.getImageView(R.id.v4));
                    vh.getImageView(R.id.v3).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,3));
                    ImageLoaderUtils.setImagDefault(data.medias.get(3).getImage_sm(), vh.getImageView(R.id.v5));
                    vh.getImageView(R.id.v4).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,4));

                }else{
                    switch (data.medias.size()){
                        case 9:
                            vh.getImageView(R.id.v9).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,8));
                            ImageLoaderUtils.setImagDefault(data.medias.get(8).getImage_sm(), vh.getImageView(R.id.v9));
                        case 8:
                            vh.getImageView(R.id.v8).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,7));
                            ImageLoaderUtils.setImagDefault(data.medias.get(7).getImage_sm(), vh.getImageView(R.id.v8));
                        case 7:
                            vh.getImageView(R.id.v7).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,6));
                            vh.get(R.id.row3).setVisibility(View.VISIBLE);
                            ImageLoaderUtils.setImagDefault(data.medias.get(6).getImage_sm(), vh.getImageView(R.id.v7));
                        case 6:

                            vh.getImageView(R.id.v6).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,5));
                            ImageLoaderUtils.setImagDefault(data.medias.get(5).getImage_sm(), vh.getImageView(R.id.v6));
                        case 5:
                            vh.get(R.id.row2).setVisibility(View.VISIBLE);
                            vh.getImageView(R.id.v5).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,4));
                            ImageLoaderUtils.setImagDefault(data.medias.get(4).getImage_sm(), vh.getImageView(R.id.v5));
                            vh.getImageView(R.id.v4).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,3));
                            ImageLoaderUtils.setImagDefault(data.medias.get(3).getImage_sm(), vh.getImageView(R.id.v4));

                        case 3:
                            vh.getImageView(R.id.v3).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,2));
                            ImageLoaderUtils.setImagDefault(data.medias.get(2).getImage_sm(), vh.getImageView(R.id.v3));
                        case 2:

                            vh.getImageView(R.id.v2).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,1));
                            ImageLoaderUtils.setImagDefault(data.medias.get(1).getImage_sm(), vh.getImageView(R.id.v2));
                            vh.getImageView(R.id.v1).setOnClickListener(new ImageViewClickListenerImp(vh.getConvertView().getContext()).setDate(data,0));
                            ImageLoaderUtils.setImagDefault(data.medias.get(0).getImage_sm(), vh.getImageView(R.id.v1));
                            vh.get(R.id.row1).setVisibility(View.VISIBLE);
                    }
                }

            }

        } else {
            vh.get(R.id.iv).setVisibility(View.GONE);

        }

    }

    class ImageViewClickListenerImp implements View.OnClickListener {

        private LikeBean likeBean;
        private int mIndex;
        private Context mContext;

        public ImageViewClickListenerImp(Context context) {
            mContext = context;
        }

        public View.OnClickListener setDate(LikeBean o,int index) {
            this.likeBean = o;
            mIndex = index;
            return this;
        }

        @Override
        public void onClick(View v) {

            ArrayList<PhotoBean> arrayList = new ArrayList<>();


            for (int i = 0; i < likeBean.medias.size(); i++) {
                PhotoBean photoBean = new PhotoBean();
                photoBean.title = likeBean.id + "";
                photoBean.loadingURl = likeBean.medias.get(i).getImage_sm();
                photoBean.imgUrl = likeBean.medias.get(i).getImage_md();
                arrayList.add(photoBean);
            }

            PhotoViewActivity.startPhotoViewActivity(mContext, arrayList, v, mIndex);
        }
    }


}
