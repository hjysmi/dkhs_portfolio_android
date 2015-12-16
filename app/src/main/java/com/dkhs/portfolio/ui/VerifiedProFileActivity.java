package com.dkhs.portfolio.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.ui.fragment.VerifiedProFileFragment;

/**
 * 个人签名
 *
 * @author weiting
 */
public class VerifiedProFileActivity extends ModelAcitivity implements OnClickListener {
    private static final String EXTRA_DESC = "extra_desc";
    private static final String EXTRA_IMG_ONE= "extra_img_one";
    private static final String EXTRA_IMG_TWO = "extra_img_two";
    private static final String EXTRA_IMG_THREE = "extra_desc_three";
    private static final String EXTRA_IMG_FOUR = "extra_desc_four";
    private static final String EXTRA_IMG_FIVE = "extra_desc_five";
    private static final String EXTRA_IMG_SIX = "extra_desc_six";

    public static Intent getInent(Context context,String desc,String ... urls){
        Intent it = new Intent(context,VerifiedProFileActivity.class);
        it.putExtra(EXTRA_DESC,desc);
        int length = urls.length;
        switch (length){
            case 6:
                it.putExtra(EXTRA_IMG_SIX,urls[5]);
            case 5:
                it.putExtra(EXTRA_IMG_FIVE,urls[4]);
            case 4:
                it.putExtra(EXTRA_IMG_FOUR,urls[3]);
            case 3:
                it.putExtra(EXTRA_IMG_THREE,urls[2]);
            case 2:
                it.putExtra(EXTRA_IMG_TWO,urls[1]);
            case 1:
                it.putExtra(EXTRA_IMG_ONE,urls[0]);
        }
        return it;
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setTitle(R.string.title_verified_profile);
        Intent it  = getIntent();
        if(it != null){
            Bundle args = new Bundle();
            String desc = it.getStringExtra(EXTRA_DESC);
            String img_one = it.getStringExtra(EXTRA_IMG_ONE);
            String img_two = it.getStringExtra(EXTRA_IMG_TWO);
            String img_three = it.getStringExtra(EXTRA_IMG_THREE);
            String img_four = it.getStringExtra(EXTRA_IMG_FOUR);
            String img_five = it.getStringExtra(EXTRA_IMG_FIVE);
            String img_six = it.getStringExtra(EXTRA_IMG_SIX);
            args.putString(EXTRA_DESC,desc);
            args.putString(EXTRA_IMG_ONE,img_one);
            args.putString(EXTRA_IMG_TWO,img_two);
            args.putString(EXTRA_IMG_THREE,img_three);
            args.putString(EXTRA_IMG_FOUR,img_four);
            args.putString(EXTRA_IMG_FIVE,img_five);
            args.putString(EXTRA_IMG_SIX,img_six);
            replaceContentFragment(VerifiedProFileFragment.newInstance(args));
        }else{
            throw new RuntimeException("not intent params request");
        }
    }


    @Override
    public void onClick(View v) {

    }
}
