package com.mingle.ui;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mingle.bean.PhotoBean;
import com.mingle.library.R;
import com.mingle.widget.CircularProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import uk.co.senab.photoviewi.PhotoView;
import uk.co.senab.photoviewi.PhotoViewAttacher;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class PhotoViewFragment extends Fragment {


    public PhotoBean mPhotoBean;
    public CircularProgressBar mProgressBar;
    private ImageView mPreviewImage;
    private ImageLoader mLoader;

    public PhotoViewFragment() {
    }


    public static PhotoViewFragment newInstance(PhotoBean photoBean) {
        PhotoViewFragment fragment = new PhotoViewFragment();
        Bundle args = new Bundle();
        args.putParcelable("photoBean", photoBean);
        fragment.setArguments(args);
        return fragment;
    }


    private PhotoView mPhotoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPhotoBean = getArguments().getParcelable("photoBean");
        mLoader = ImageLoader.getInstance();
        View view = inflater.inflate(R.layout.fragment_photo_view, container, false);
        mPhotoView = (PhotoView) view.findViewById(R.id.photoIm);
        mPreviewImage = (ImageView) view.findViewById(R.id.previewImage);
        mProgressBar = (CircularProgressBar) view.findViewById(R.id.progressBar);

        if (mLoader.getDiskCache().get(mPhotoBean.loadingURl).exists()) {
            mLoader.displayImage(mPhotoBean.loadingURl, mPreviewImage);
        } else {
            mPreviewImage.setImageResource(R.drawable.ic_img_thumbnail_large);
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .showImageOnFail(R.drawable.ic_img_failure)

                .build();
        mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                getActivity().finish();
            }
        });

        mPhotoView.setMinimumScale(0.6f);


//        mPhotoView.setAdjustViewBounds(true);
        mLoader.displayImage(mPhotoBean.imgUrl, mPhotoView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, final Bitmap loadedImage) {
                if (imageUri.equals(mPhotoBean.imgUrl)) {
                    mProgressBar.setVisibility(View.GONE);
                    mPreviewImage.setVisibility(View.GONE);
                }
                mPhotoView.post(new Runnable() {
                    @Override
                    public void run() {

                        if (mPhotoView != null && mPhotoView.getDisplayRect()!= null  &&mPhotoView.getDisplayRect().width() < mPhotoView.getWidth()) {
                            float scale = mPhotoView.getWidth() * 1.0f / mPhotoView.getDisplayRect().width();
                            mPhotoView.setMaximumScale(Math.max(scale, mPhotoView.getMaximumScale()));
                            mPhotoView.setScale(scale, mPhotoView.getWidth() / 2.0f, 0, false);
                        }
                    }
                });

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                if (mLoader.getDiskCache().get(mPhotoBean.loadingURl).exists()) {
                    mLoader.displayImage(mPhotoBean.loadingURl, mPhotoView);
                    mProgressBar.setVisibility(View.GONE);
                    mPreviewImage.setVisibility(View.GONE);
                } else {

                    mPhotoView.setImageResource(R.drawable.ic_img_thumbnail_large);
                }
                mProgressBar.setVisibility(View.GONE);
                mPreviewImage.setVisibility(View.GONE);

                super.onLoadingFailed(imageUri, view, failReason);
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int i, int i1) {
                mProgressBar.setProgressPecentage(i * 1.0f / i1);
            }
        });
        return view;
    }


}
