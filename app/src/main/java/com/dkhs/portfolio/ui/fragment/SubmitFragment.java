package com.dkhs.portfolio.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dkhs.portfolio.R;
import com.dkhs.portfolio.bean.UserEntity;

/**
 * 牛人招募之提交成功
 * Created by xuetong on 2015/12/7.
 */
public class SubmitFragment extends BaseFragment {
    private final static String EXTRA_STATUS = "extra_status";
    private int verifiedStatus;
    private Button okBtn;
    @Override
    public int setContentLayoutId() {
        return R.layout.fragment_submit;
    }

    public static SubmitFragment newInstance(int type){
        SubmitFragment fg = new SubmitFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_STATUS,type);
        fg.setArguments(args);
        return fg;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        verifiedStatus = args.getInt(EXTRA_STATUS);
        ImageView statusIv = (ImageView) view.findViewById(R.id.iv_verified_status);
        if(verifiedStatus == UserEntity.VERIFIEDSTATUS.VERIFYING.getTypeid()){
            statusIv.setBackgroundResource(R.drawable.ic_betterrecruit_submit);
        }else{
            statusIv.setBackgroundResource(R.drawable.ic_betterrecruit_complete);
        }
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
