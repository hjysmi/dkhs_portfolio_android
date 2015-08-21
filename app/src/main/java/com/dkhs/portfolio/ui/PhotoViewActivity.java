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
import com.mingle.bean.PhotoBean;
import com.mingle.ui.PhotoViewListFragment;
//import com.mingle.ui.PhotoViewListFragment;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends ModelAcitivity  {


    public static void startPhotoViewActivity(Context context,ArrayList<PhotoBean> photoBeanList,View view,int  defaultIndex){
        Intent intent=new Intent(context,PhotoViewActivity.class);
        intent.putExtra(PhotoViewListFragment.INDEX,defaultIndex);
        intent.putParcelableArrayListExtra(PhotoViewListFragment.PHOTO_BEAN_LIST, photoBeanList);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(view,
                        (int)view.getWidth()/2, (int)view.getHeight()/2,
                        0, 0);
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
