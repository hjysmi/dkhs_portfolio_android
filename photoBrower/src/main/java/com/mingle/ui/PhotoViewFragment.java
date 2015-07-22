package com.mingle.ui;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mingle.library.R;
import com.mingle.widget.CircularProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import uk.co.senab.photoviewi.PhotoView;
import uk.co.senab.photoviewi.PhotoViewAttacher;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class PhotoViewFragment extends Fragment {



    private String url;
    private CircularProgressBar progressBar;

    public PhotoViewFragment() {
    }



    public static PhotoViewFragment newInstance(String param1) {
        PhotoViewFragment fragment = new PhotoViewFragment();
        Bundle args = new Bundle();
        args.putString("url", param1);
        fragment.setArguments(args);
        return fragment;
    }


    private PhotoView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        url=getArguments().getString("url");
        View view =inflater.inflate(R.layout.fragment_photo_view, container, false);
        imageView = (PhotoView) view.findViewById(R.id.photoIm);
        progressBar= (CircularProgressBar) view.findViewById(R.id.progressBar);

        ImageLoader loader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
                .build();

        imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                getActivity().finish();
            }
        });

        loader.displayImage(url, imageView, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (imageUri.equals(url)) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }






}
