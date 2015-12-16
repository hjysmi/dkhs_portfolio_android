package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.utils.ImageLoaderUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by wuyongsen on 2015/12/11.
 */
public class VerifiedProFileFragment extends BaseFragment {
    private static final String EXTRA_DESC = "extra_desc";
    private static final String EXTRA_IMG_ONE= "extra_img_one";
    private static final String EXTRA_IMG_TWO = "extra_img_two";
    private static final String EXTRA_IMG_THREE = "extra_desc_three";
    private static final String EXTRA_IMG_FOUR = "extra_desc_four";
    private static final String EXTRA_IMG_FIVE = "extra_desc_five";
    private static final String EXTRA_IMG_SIX = "extra_desc_six";

    @ViewInject(R.id.sign_edit_text)
    private TextView signText;
    @ViewInject(R.id.row1)
    private View row1;
    @ViewInject(R.id.row2)
    private View row2;
    @ViewInject(R.id.v1)
    private ImageView oneIv;
    @ViewInject(R.id.v2)
    private ImageView twoIv;
    @ViewInject(R.id.v3)
    private ImageView threeIv;
    @ViewInject(R.id.v4)
    private ImageView fourIv;
    @ViewInject(R.id.v5)
    private ImageView fiveIv;
    @ViewInject(R.id.v6)
    private ImageView sixIv;

    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_verified_profile;
    }

    public static VerifiedProFileFragment newInstance(Bundle args) {
        VerifiedProFileFragment fragment = new VerifiedProFileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleArgs(getArguments());
    }


    private void handleArgs(Bundle args){
        String desc = args.getString(EXTRA_DESC);
        String img_one = args.getString(EXTRA_IMG_ONE);
        String img_two = args.getString(EXTRA_IMG_TWO);
        String img_three = args.getString(EXTRA_IMG_THREE);
        String img_four = args.getString(EXTRA_IMG_FOUR);
        String img_five = args.getString(EXTRA_IMG_FIVE);
        String img_six = args.getString(EXTRA_IMG_SIX);

        if (TextUtils.isEmpty(desc)) {
            signText.setHint(R.string.desc_def_text);
        } else {
            signText.setText(desc);
        }
//        row1.setVisibility(View.GONE);
//        row2.setVisibility(View.GONE);
        handleImg(oneIv,img_one,row1);
        handleImg(twoIv,img_two,row1);
        handleImg(threeIv,img_three,row1);
        handleImg(fourIv,img_four,row2);
        handleImg(fiveIv,img_five,row2);
        handleImg(sixIv,img_six,row2);
    }

    /**
     *
     * @param iv
     * @param url
     * @param parent
     */
    private void handleImg(ImageView iv,String url,View parent){
        if(!TextUtils.isEmpty(url)){
            iv.setVisibility(View.VISIBLE);
            parent.setVisibility(View.VISIBLE);
            ImageLoaderUtils.setImagDefault(url, iv);
        }else{
            iv.setVisibility(View.GONE);
        }
    }






}
