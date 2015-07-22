package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.PromptManager;
import com.google.gson.Gson;
import com.mingle.ui.PhotoViewListFragment;
//import com.mingle.ui.PhotoViewListFragment;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends ModelAcitivity  {


    public static void startPhotoViewActivity(Context context,ArrayList<String> urls,int  defaultIndex){
        Intent intent=new Intent(context,PhotoViewActivity.class);
        intent.putExtra(PhotoViewListFragment.INDEX,0);
        intent.putStringArrayListExtra(PhotoViewListFragment.URL_LIST,urls);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        hideHead();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFL,new PhotoViewListFragment()).commitAllowingStateLoss();
    }


}
