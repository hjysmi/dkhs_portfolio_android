package com.dkhs.portfolio.ui;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.mingle.ui.PhotoViewListFragment;
//import com.mingle.ui.PhotoViewListFragment;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends ModelAcitivity  {


    public static void startPhotoViewActivity(Context context,ArrayList<String> urls,View view,int  defaultIndex){
        Intent intent=new Intent(context,PhotoViewActivity.class);
        intent.putExtra(PhotoViewListFragment.INDEX,0);
        intent.putStringArrayListExtra(PhotoViewListFragment.URL_LIST,urls);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(view, //The View that the new activity is animating from
                        (int)view.getWidth()/2, (int)view.getHeight()/2, //拉伸开始的坐标
                        0, 0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏
        ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        hideHead();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new PhotoViewListFragment()).commitAllowingStateLoss();
    }


}
